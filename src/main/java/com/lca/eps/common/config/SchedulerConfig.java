package com.lca.eps.common.config;

import org.quartz.CronTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class SchedulerConfig {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	private final ApplicationContext applicationContext;
	private final RequestUUIDJobListener requestUuidJobListener;

	@Value("${quartz.database.username}")
	private String quartzUsername;

	@Value("${quartz.database.password}")
	private String quartzPassword;

	@Value("${database.connection.string}")
	private String quartzConnectionString;

	@Autowired
	public SchedulerConfig(ApplicationContext applicationContext,
	                       RequestUUIDJobListener requestUuidJobListener) {
		this.applicationContext = applicationContext;
		this.requestUuidJobListener = requestUuidJobListener;
	}

	@Bean(name = "epsQuartzScheduler")
	public SchedulerFactoryBean quartzScheduler(CronTrigger... triggerList) throws IOException {
		var quartzScheduler = new SchedulerFactoryBean();

		quartzScheduler.setOverwriteExistingJobs(true);
		quartzScheduler.setSchedulerName("eps-job-scheduler");

		// custom job factory of spring with DI support for @Autowired!
		var jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		quartzScheduler.setJobFactory(jobFactory);

		quartzScheduler.setQuartzProperties(getQuartzProperties());
		quartzScheduler.setTriggers(triggerList);
		quartzScheduler.setGlobalJobListeners(requestUuidJobListener);
		quartzScheduler.setStartupDelay(5);
		return quartzScheduler;
	}

	private Properties getQuartzProperties() throws IOException {
		var resource = new ClassPathResource("quartz/%s.eps.quartz.properties".formatted(System.getProperty("com.labcorp.app.env")));
		var properties = new Properties();
		try (var inputStream = resource.getInputStream()) {
			properties.load(inputStream);
			for (var propertyEntry : properties.entrySet()) {
				logger.info("{}: {}", propertyEntry.getKey(), propertyEntry.getValue());
			}
			properties.setProperty("org.quartz.dataSource.quartzDataSource.user", quartzUsername);
			properties.setProperty("org.quartz.dataSource.quartzDataSource.password", quartzPassword);
			properties.setProperty("org.quartz.dataSource.quartzDataSource.URL", quartzConnectionString);
		}
		return properties;
	}
}
