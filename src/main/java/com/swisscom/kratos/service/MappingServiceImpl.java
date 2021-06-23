package com.swisscom.kratos.service;

import com.swisscom.kratos.model.DeviceConfig;
import com.swisscom.kratos.model.MappingLogic;
import com.swisscom.kratos.model.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Future;

@Service
@Slf4j
public class MappingServiceImpl implements MappingService {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private NetworkServiceConfigurer networkServiceConfigurer;

    @Autowired
    private List<DeviceConfigService> deviceConfigServices;

    private MappingLogic mappingLogic;

    private final Map<String, Future> tasks = new HashMap<>();

    @Value("${mapping.logic}")
    private String defaultCode;

    @PostConstruct
    void init() {
        mappingLogic = MappingLogic.builder()
                .code(defaultCode)
                .build();
    }

    @Override
    public MappingLogic getMappingLogic() {
        return mappingLogic;
    }

    @Override
    public void setMappingLogic(MappingLogic mappingLogic) {
        this.mappingLogic = mappingLogic;
    }

    @Override
    public Collection<NetworkService> dryRun(DeviceConfig config, MappingLogic logic) {
        return mapDeviceConfig(config, logic);
    }

    @Override
    public String startMappingTask() {
        String id = UUID.randomUUID().toString();

        Future<?> submit = taskScheduler.submit(createExecutionJob());
        tasks.put(id, submit);
        return id;
    }

    public Runnable createExecutionJob() {
        MappingLogic logic = getMappingLogic();
        return new Runnable() {
            @Override
            public void run() {
                deviceConfigServices.parallelStream()
                        .map(source -> {
                            log.info("Reading configuration from {}", source.serviceId());
                            Collection<String> devices = source.listDevices();
                            Map<String, DeviceConfig> config = source.readConfiguration(devices);
                            return config.values();
                        })
                        .flatMap(c -> c.stream())
                        .map(c -> {
                            log.info("Mapping device {}", c.getDeviceId());
                            return mapDeviceConfig(c, logic);
                        }).flatMap(c -> c.stream())
                        .forEach(ns -> {
                            log.info("Persisting network service {}", ns.getServiceId());
                            networkServiceConfigurer.apply(ns);
                        });
            }
        };
    }

    @Override
    public String fetchMappingTaskStatus(String taskId) {
        if (!tasks.containsKey(taskId)) {
            return "Not found";
        }
        return tasks.get(taskId).isDone() ? "Done" : "Cancelled";
    }

    @Override
    public void cancelMappingTask(String taskId) {
        if (!tasks.containsKey(taskId)) {
            throw new RuntimeException("Task " + taskId + " not found");
        }
        tasks.get(taskId).cancel(true);
    }

    private KieSession mapRules(MappingLogic mappingLogic) throws IOException {
        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kb.add(ResourceFactory.newByteArrayResource(mappingLogic.getCode().getBytes(StandardCharsets.UTF_8)), ResourceType.DRL);

        KnowledgeBuilderErrors errors = kb.getErrors();
        for (KnowledgeBuilderError error : errors) {
            log.error("{}", error);
        }
        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());
        return kBase.newKieSession();
    }


    private Collection<NetworkService> mapDeviceConfig(DeviceConfig config, MappingLogic logic) {
        KieSession kieSession = null;
        try {
            kieSession = mapRules(logic);

            KieServices kieServices = KieServices.Factory.get();
            kieServices.getLoggers().newFileLogger(kieSession, "log");

            Collection<NetworkService> results = new ArrayList<>();
            kieSession.setGlobal("results", results);

            kieSession.insert(config);

            kieSession.fireAllRules();
            log.info("Output {}", results);
            return results;
        } catch (IOException e) {
            log.error("Failed mapping evaluation", e);
            throw new RuntimeException(e);
        } finally {
            if (kieSession != null) {
                kieSession.destroy();
            }
        }
    }

}
