package com.lca.eps.payment;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.payment.jobs.ProcessTerminalIdsReportJob;

@Configuration
public class AssetJobsConfig {

	private final Environment environment;

	@Autowired
	public AssetJobsConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "processTerminalIdsReportJobDetail")
	public JobDetailFactoryBean processTerminalIdsReportJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessTerminalIdsReportJob.class);
		jobDetailFactory.setName("ProcessTerminalIdsReportJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processTerminalIdsReportJobTrigger")
	public CronTriggerFactoryBean processTerminalIdsReportJobTrigger(@Qualifier("processTerminalIdsReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processTerminalIdsReportJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processTerminalIdsReportJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
