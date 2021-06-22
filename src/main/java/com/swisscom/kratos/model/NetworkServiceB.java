package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class NetworkServiceB extends NetworkService {

    @JsonProperty("device")
    private String device;

    @JsonProperty("configuration")
    private Map<String, Object> configuration;

    public NetworkServiceB() {
        super("B");
    }
}
