package com.lca.eps.jobs;

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
import jakarta.ws.rs.core.Response;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ProcessExceptionHandlingSchedulerJob extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private Environment environment;
	private Client client;
	private JerseyClientHelper jerseyClientHelper;

	@Override
	public void executeInternal(JobExecutionContext jeContext) throws JobExecutionException {
		try {
			logger.info("ProcessExceptionHandlingSchedulerJob Scheduler Job started...");
			String targetUrl = environment.getProperty("process-exception-handling.job.target.client.invoice");
			Response response = jerseyClientHelper.doGETRequest(client, targetUrl);
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				logger.info("ProcessExceptionHandlingSchedulerJob Scheduler Job has been completed successfully");
			} else {
				logger.error("ProcessExceptionHandlingSchedulerJob Scheduler Job Bad request due to response: {}", response.getStatusInfo().getReasonPhrase());
			}
			logger.info("ProcessExceptionHandlingSchedulerJob Scheduler Job Ended...");
		} catch (Exception e) {
			logger.error("ProcessExceptionHandlingSchedulerJob Scheduler Job raised an exception: {}", Throwables.getStackTraceAsString(e));
		}
	}

	@Autowired
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Autowired
	@Qualifier("distributedClient")
	public void setClient(Client client) {
		this.client = client;
	}

	@Autowired
	public void setJerseyClientHelper(JerseyClientHelper jerseyClientHelper) {
		this.jerseyClientHelper = jerseyClientHelper;
	}
}
