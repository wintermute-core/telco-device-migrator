package com.swisscom.kratos.service;

import com.swisscom.kratos.model.NetworkService;

/**
 * Service to apply network service configuration change
 */
public interface NetworkServiceConfigurer {

    void apply(NetworkService networkService);

}
