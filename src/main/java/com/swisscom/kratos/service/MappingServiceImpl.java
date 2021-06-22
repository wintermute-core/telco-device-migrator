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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class MappingServiceImpl implements MappingService {

    private MappingLogic mappingLogic;

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
    public Optional<NetworkService> dryRun(DeviceConfig config) {
        KieSession kieSession = null;
        try {
            kieSession = mapRules();

            KieServices kieServices = KieServices.Factory.get();
            kieServices.getLoggers().newFileLogger(kieSession, "log");

            Map<String, Object> results = new HashMap<>();
            kieSession.setGlobal("results", results);

            kieSession.insert(config);

            kieSession.fireAllRules();
            log.info("Output {}", results);
            return Optional.ofNullable((NetworkService)results.get("service"));
        } catch (IOException e) {
            log.error("Failed mapping evaluation", e);
            throw new RuntimeException(e);
        } finally {
            if (kieSession != null) {
                kieSession.destroy();
            }
        }
    }

    @Override
    public String startMappingTask() {
        return null;
    }

    @Override
    public String fetchMappingTaskStatus(String taskId) {
        return null;
    }

    @Override
    public void cancelMappingTask(String taskId) {

    }

    private KieSession mapRules() throws IOException {
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



}
