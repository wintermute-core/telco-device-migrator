package com.swisscom.kratos.model;

import lombok.Builder;
import lombok.Data;

/**
 * Logic for mapping Device config to Network service.
 * Current implementation: holds Drools code for mapping.
 */
@Data
@Builder
public class MappingLogic {
    private String code;
}
