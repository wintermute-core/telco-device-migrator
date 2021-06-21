package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NetworkService {

    @JsonProperty("network_service_type")
    private String networkServiceType;

}
