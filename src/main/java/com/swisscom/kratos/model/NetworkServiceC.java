package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NetworkServiceC extends NetworkService {

    @JsonProperty("dev")
    private String device;

    @JsonProperty("conf")
    private Map<String, Object> configuration = new HashMap<>();

    public NetworkServiceC() {
        super("C");
    }
}
