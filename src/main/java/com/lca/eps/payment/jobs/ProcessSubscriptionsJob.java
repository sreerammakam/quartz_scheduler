package com.lca.eps.payment.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.lca.eps.common.util.JerseyClientHelper;

import jakarta.ws.rs.client.Client;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessSubscriptionsJob extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private Environment environment;
	private Client client;
	private JerseyClientHelper jerseyClientHelper;

	@Override
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			logger.info("ProcessSubscriptionsJob Job Started...");
			String targetUrl = environment.getProperty("processSubscriptionsJob.target.client");
			var response = jerseyClientHelper.doGETRequest(client, targetUrl);
			if (response.getStatus() == 200) {
				logger.info("ProcessSubscriptionsJob Job has been completed Successfully...");
			} else {
				logger.error("ProcessSubscriptionsJob Job Scheduler failed due to response: {}", response.getStatusInfo().getReasonPhrase());
			}
			logger.info("ProcessSubscriptionsJob Job Ended...");
		} catch (Exception exception) {
			logger.error("ProcessSubscriptionsJob Job Scheduler Exception: {}", Throwables.getStackTraceAsString(exception));
		}
	}

	@Autowired
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Qualifier("distributedClient")
	@Autowired
	public void setClient(Client client) {
		this.client = client;
	}

	@Autowired
	public void setJerseyClientHelper(JerseyClientHelper jerseyClientHelper) {
		this.jerseyClientHelper = jerseyClientHelper;
	}
}
