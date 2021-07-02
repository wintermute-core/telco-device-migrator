package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NetworkServiceA extends NetworkService {

    @JsonProperty("device_id")
    private String device;

    @JsonProperty("parameters")
    private Map<String, Object> configuration = new HashMap<>();

    public NetworkServiceA() {
        super("A");
    }
}
