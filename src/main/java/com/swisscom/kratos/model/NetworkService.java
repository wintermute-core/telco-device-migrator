package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Network services common fields
 */
@Data
public abstract class NetworkService {

    @JsonProperty("network_service_type")
    private String networkServiceType;

}
