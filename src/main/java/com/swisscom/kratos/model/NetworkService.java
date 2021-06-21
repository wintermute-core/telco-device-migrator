package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NetworkService {

    @JsonProperty("network_service_type")
    private String networkServiceType;

}
