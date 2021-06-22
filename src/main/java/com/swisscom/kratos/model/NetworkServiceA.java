package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NetworkServiceA extends NetworkService {

    @JsonProperty("device_id")
    private String device;

    @JsonProperty("parameters")
    private Map<String, Object> configuration;
}
