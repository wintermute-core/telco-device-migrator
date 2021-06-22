package com.swisscom.kratos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Network services common fields
 */
@Data
public abstract class NetworkService {

    @JsonIgnore
    private transient String serviceId;

    @JsonProperty("network_service_type")
    private final String serviceType;


}
