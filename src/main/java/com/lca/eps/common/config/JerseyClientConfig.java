package com.lca.eps.common.config;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jdk.connector.JdkConnectorProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lca.eps.common.filter.DistributedRequestIdFilter;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

/**
 * Provides two Jersey client implementations.
 * <p>
 * 1. Default Jersey client implementation without any filter;
 * <p>
 * 2. Jersey client with Distributed Request ID filter registered.
 * <p>
 * To use any of these clients,
 * you should specify the name of it with qualifier annotation or by using component name as a variable name.
 */
@Configuration
public class JerseyClientConfig {

	@Bean
	public ClientConfig distributedClientConfig() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(DistributedRequestIdFilter.class);
		clientConfig.connectorProvider(new JdkConnectorProvider());
		return clientConfig;
	}

	/**
	 * Return a Jersey client without any filters registered.
	 *
	 * @return Client object created without any filter registered.
	 */
	@Bean("defaultClient")
	public Client defaultClient() {
		return ClientBuilder.newClient();
	}

	/**
	 * Returns a Jersey client which send eps-requestUUID field as an extra header to keep track of distributed transaction across other application;
	 * This client should be used for all cases when communicating with other LabCorp applications
	 *
	 * @param clientConfig client config with a specific list of filters
	 * @return Client object created with specific filters
	 */
	@Bean("distributedClient")
	public Client distributedClient(ClientConfig clientConfig) {
		return ClientBuilder.newClient(clientConfig);
	}
}
