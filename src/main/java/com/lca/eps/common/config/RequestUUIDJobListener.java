package com.lca.eps.common.config;

import org.apache.logging.log4j.ThreadContext;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RequestUUIDJobListener implements JobListener {

	private static final String EPS_REQUEST_UUID = "eps-requestUUID";

	@Override
	public String getName() {
		return "RequestUUIDJobListener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		ThreadContext.put(EPS_REQUEST_UUID, UUID.randomUUID().toString());
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		ThreadContext.remove(EPS_REQUEST_UUID);
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		ThreadContext.remove(EPS_REQUEST_UUID);
	}
}
