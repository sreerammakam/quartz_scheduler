package com.lca.eps.common.config;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.invoice.jobs.ChangeStateSchedulerJob;
import com.lca.eps.jobs.ProcessExceptionHandlingSchedulerJob;

@Configuration
public class CommonJobsConfig {

	private final Environment environment;

	@Autowired
	public CommonJobsConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "changeStateJobDetail")
	public JobDetailFactoryBean changeStateJobDetail() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ChangeStateSchedulerJob.class);
		jobDetailFactory.setName("ChangeStateJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "changeStateJobTrigger")
	public CronTriggerFactoryBean changeStateJobTrigger(@Qualifier("changeStateJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("change-state.job.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ChangeStateTrigger");
		cronTriggerFactoryBean.setGroup("eps-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processExceptionHandlingJobDetail")
	public JobDetailFactoryBean processExceptionHandlingJobDetail() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessExceptionHandlingSchedulerJob.class);
		jobDetailFactory.setName("ProcessExceptionHandlingJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processExceptionHandlingJobTrigger")
	public CronTriggerFactoryBean processExceptionHandlingJobTrigger(@Qualifier("processExceptionHandlingJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("process-exception-handling.job.trigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessExceptionHandlingTrigger");
		cronTriggerFactoryBean.setGroup("eps-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
