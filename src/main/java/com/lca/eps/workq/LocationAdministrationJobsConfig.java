package com.lca.eps.workq;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.workq.jobs.EpsWorkqPendingLocationReportJob;
import com.lca.eps.workq.jobs.EpsWorkqUpdateAssociatedAssetIndicatorJob;

@Configuration
public class LocationAdministrationJobsConfig {

	private final Environment environment;

	@Autowired
	public LocationAdministrationJobsConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "sendPendingLocationJob")
	public JobDetailFactoryBean sendPendingLocationJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqPendingLocationReportJob.class);
		jobDetailFactory.setName("SendPendingLocationJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "sendPendingLocationJobTrigger")
	public CronTriggerFactoryBean sendPendingLocationJobTrigger(@Qualifier("sendPendingLocationJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("SendPendingLocationTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("SendPendingLocationJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "updateAssociatedAssetIndicatorJob")
	public JobDetailFactoryBean updateAssociatedAssetIndicatorJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqUpdateAssociatedAssetIndicatorJob.class);
		jobDetailFactory.setName("UpdateAssociatedAssetIndicatorJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "updateAssociatedAssetIndicatorJobTrigger")
	public CronTriggerFactoryBean updateAssociatedAssetIndicatorJobTrigger(@Qualifier("updateAssociatedAssetIndicatorJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("UpdateAssociatedAssetIndicatorTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("UpdateAssociatedAssetIndicatorJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
