package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NetworkServiceB extends NetworkService {

    @JsonProperty("device")
    private String device;

    @JsonProperty("configuration")
    private Map<String, Object> configuration = new HashMap<>();

    public NetworkServiceB() {
        super("B");
    }
}
