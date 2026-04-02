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

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class CheckFilesProcessedSchedulerJob extends QuartzJobBean {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private Client client;
	private Environment env;
	private JerseyClientHelper jerseyClientHelper;

	@Override
	public void executeInternal(JobExecutionContext jeContext) throws JobExecutionException {
		try {
			log.info("CheckFilesProcessedScheduler Job Started...");
			String targetUrl = env.getProperty("CheckFilesProcessedSchedulerJob.target.client");
			var response = jerseyClientHelper.doGETRequest(client, targetUrl);
			if (response.getStatus() == 200) {
				log.info("CheckFilesProcessedScheduler Job has been completed Successfully...");
			} else {
				log.error("CheckFilesProcessed Job Scheduler Bad Request due to response: {}", response.getStatusInfo().getReasonPhrase());
			}
			log.info("CheckFilesProcessedScheduler Job Ended...");
		} catch (Exception e) {
			log.error("CheckFilesProcessed Job Scheduler Exception: {}", Throwables.getStackTraceAsString(e));
		}
	}

	@Autowired
	@Qualifier("distributedClient")
	public void setClient(Client client) {
		this.client = client;
	}

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Autowired
	public void setJerseyClientHelper(JerseyClientHelper jerseyClientHelper) {
		this.jerseyClientHelper = jerseyClientHelper;
	}
}
