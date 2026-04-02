package com.lca.eps.invoice.jobs;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;

import java.util.List;
import java.util.Set;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ChangeStateSchedulerJob extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private final Scheduler scheduler;

	@Autowired
	public ChangeStateSchedulerJob(Scheduler scheduler) {
		this.scheduler = scheduler;
	}


	@Override
	public void executeInternal(JobExecutionContext jeContext) throws JobExecutionException {
		String currentClassSimpleName = this.getClass().getSimpleName();
		logger.info("{} started...", currentClassSimpleName);
		Set<JobKey> jobKeys;
		try {
			for (String group : scheduler.getJobGroupNames()) {
				jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
				logger.info("Job keys={}", jobKeys.size());
				for (JobKey jobKey : jobKeys) {
					if (!jobKey.getName().contains("ChangeState")) {
						List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
						Trigger trigger = triggers.getFirst();
						Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
						if (Trigger.TriggerState.BLOCKED.equals(triggerState)) {
							logger.info("Trying to unblock trigger State...");

							String cronExpression = ((CronTrigger) trigger).getCronExpression();
							CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
							cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
							if (scheduler.checkExists(trigger.getKey())) {
								logger.info("job exists...");
								scheduler.interrupt(jobKey);
							}
						} else {
							logger.info("trigger State={}", triggerState);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("{} raised an exception: {}", currentClassSimpleName, Throwables.getStackTraceAsString(e));
		}
	}
}
