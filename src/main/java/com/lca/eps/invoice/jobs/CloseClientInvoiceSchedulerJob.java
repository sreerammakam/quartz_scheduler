package com.lca.eps.invoice.jobs;

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
public class CloseClientInvoiceSchedulerJob extends QuartzJobBean {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private Environment environment;
	private Client client;
	private JerseyClientHelper jerseyClientHelper;

	@Override
	public void executeInternal(JobExecutionContext jeContext) throws JobExecutionException {
		try {
			log.info("CloseClientInvoiceSchedulerJob Job Started...");
			String targetUrl = environment.getProperty("CloseClientInvoiceSchedulerJob.target.client");
			Response response = jerseyClientHelper.doGETRequest(client, targetUrl);
			if (response.getStatus() == 200) {
				log.info("CloseClientInvoiceSchedulerJob Job has been completed Successfully...");
				log.info("status={}", response.getStatusInfo().getReasonPhrase());
			} else {
				log.error("CloseClientInvoiceScheduler Job Scheduler Bad Request due to response: {}", response.getStatusInfo().getReasonPhrase());
			}
			log.info("CloseClientInvoiceScheduler Job Ended...");
		} catch (Exception e) {
			log.error("CloseClientInvoiceScheduler Job Scheduler Exception: {}", Throwables.getStackTraceAsString(e));
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
