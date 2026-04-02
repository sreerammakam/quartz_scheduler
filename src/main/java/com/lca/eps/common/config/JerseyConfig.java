package com.lca.eps.common.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.context.annotation.Configuration;

import com.lca.eps.common.filter.RequestResponseLoggingFilter;

import java.util.Set;

@Configuration
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		//controllers
		packages("com.lca.eps.common.controller");

		//list of uri to skip during logging, execution time measurement and authentication filters
		Set<String> skippedURLs = Set.of(
				"actuator/health/liveness",
				"actuator/health/readiness"
		);
		//filters
		register(new RequestResponseLoggingFilter(true, skippedURLs));

		//jsp rendering
		register(JspMvcFeature.class);
		property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/templates");
	}
}
