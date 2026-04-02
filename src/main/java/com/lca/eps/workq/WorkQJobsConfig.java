package com.lca.eps.workq;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.common.persistence.dao.PropertyValueDao;
import com.lca.eps.workq.jobs.EpsWorkqAmexBatchJob;
import com.lca.eps.workq.jobs.EpsWorkqCloseLocationJob;
import com.lca.eps.workq.jobs.EpsWorkqDtsBankActvEmailJob;
import com.lca.eps.workq.jobs.EpsWorkqDtsBankEmailJob;
import com.lca.eps.workq.jobs.EpsWorkqVantivFileReadJob;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class WorkQJobsConfig {

	private final Environment environment;
	private final PropertyValueDao propertyValueDao;

	private Map<String, String> propertyValueMap;

	@PostConstruct
	public void init() {
		propertyValueMap = propertyValueDao.findPropertyNameToPropertyValue();
	}


	@Autowired
	public WorkQJobsConfig(Environment environment,
	                       PropertyValueDao propertyValueDao) {
		this.environment = environment;
		this.propertyValueDao = propertyValueDao;
	}

	@Bean(name = "workqAmexBatchJob")
	public JobDetailFactoryBean workqAmexBatchJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqAmexBatchJob.class);
		jobDetailFactory.setName("EpsWorkqAmexBatchJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "workqAmexBatchJobTrigger")
	public CronTriggerFactoryBean workqAmexBatchJobTrigger(@Qualifier("workqAmexBatchJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		String cron = propertyValueMap.get("eps.workq.amex.cron");
		if (StringUtils.isBlank(cron)) {
			cron = environment.getRequiredProperty("eps.workq.amex.cron");
		}
		cronTriggerFactoryBean.setCronExpression(cron);
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("WorkqAmexBatchJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "workqCloseLocationJob")
	public JobDetailFactoryBean workqCloseLocationJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqCloseLocationJob.class);
		jobDetailFactory.setName("EpsWorkqCloseLocationJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "workqCloseLocationJobTrigger")
	public CronTriggerFactoryBean workqCloseLocationJobTrigger(@Qualifier("workqCloseLocationJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		String cron = propertyValueMap.get("eps.workq.closelocation.cron");
		if (StringUtils.isBlank(cron)) {
			cron = environment.getRequiredProperty("eps.workq.closelocation.cron");
		}
		cronTriggerFactoryBean.setCronExpression(cron);
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("WorkqCloseLocationJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "workqDtsBankActvEmailJob")
	public JobDetailFactoryBean workqDtsBankActvEmailJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqDtsBankActvEmailJob.class);
		jobDetailFactory.setName("EpsWorkqDtsBankActvEmailJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "workqDtsBankActvEmailJobTrigger")
	public CronTriggerFactoryBean workqDtsBankActvEmailJobTrigger(@Qualifier("workqDtsBankActvEmailJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		String cron = propertyValueMap.get("eps.workq.dtsbankactv.cron");
		if (StringUtils.isBlank(cron)) {
			cron = environment.getRequiredProperty("eps.workq.dtsbankactv.cron");
		}
		cronTriggerFactoryBean.setCronExpression(cron);
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("WorkqDtsBankActvEmailJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "workqDtsBankEmailJob")
	public JobDetailFactoryBean workqDtsBankEmailJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqDtsBankEmailJob.class);
		jobDetailFactory.setName("EpsWorkqDtsBankEmailJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "workqDtsBankEmailJobTrigger")
	public CronTriggerFactoryBean workqDtsBankEmailJobTrigger(@Qualifier("workqDtsBankEmailJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		String cron = propertyValueMap.get("eps.workq.dtsbank.cron");
		if (StringUtils.isBlank(cron)) {
			cron = environment.getRequiredProperty("eps.workq.dtsbank.cron");
		}
		cronTriggerFactoryBean.setCronExpression(cron);
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("WorkqDtsBankEmailJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "workqVantivFileReadJob")
	public JobDetailFactoryBean workqVantivFileReadJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(EpsWorkqVantivFileReadJob.class);
		jobDetailFactory.setName("EpsWorkqVantivFileReadJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-workq-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "workqVantivFileReadJobTrigger")
	public CronTriggerFactoryBean workqVantivFileReadJobTrigger(@Qualifier("workqVantivFileReadJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		String cron = propertyValueMap.get("eps.workq.vantiv.cron");
		if (StringUtils.isBlank(cron)) {
			cron = environment.getRequiredProperty("eps.workq.vantiv.cron");
		}
		cronTriggerFactoryBean.setCronExpression(cron);
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("WorkqVantivFileReadJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-workq-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
