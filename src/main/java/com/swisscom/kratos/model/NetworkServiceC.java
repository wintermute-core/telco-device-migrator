package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class NetworkServiceC extends NetworkService {

    @JsonProperty("dev")
    private String device;

    @JsonProperty("conf")
    private Map<String, Object> configuration;
}
