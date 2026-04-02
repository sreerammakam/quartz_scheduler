package com.lca.eps.invoice;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.invoice.jobs.CloseClientInvoiceSchedulerJob;
import com.lca.eps.invoice.jobs.CloseInvoicesSchedulerJob;
import com.lca.eps.invoice.jobs.ProcessClientMAMFileSchedulerJob;
import com.lca.eps.invoice.jobs.ProcessInvoiceInboundJournalSchedulerJob;
import com.lca.eps.invoice.jobs.ProcessJournalToInvoiceSchedulerJob;
import com.lca.eps.invoice.jobs.ProcessMAMFileSchedulerJob;

@Configuration
public class InvoiceJobsConfig {

	private final Environment environment;

	@Autowired
	public InvoiceJobsConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "closeClientInvoiceJobDetail")
	public JobDetailFactoryBean closeClientInvoiceSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CloseClientInvoiceSchedulerJob.class);
		jobDetailFactory.setName("closeClientInvoiceSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-invoice-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "closeClientInvoiceSchedulerJobTrigger")
	public CronTriggerFactoryBean closeClientInvoiceSchedulerJobTrigger(@Qualifier("closeClientInvoiceJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("closeClientInvoiceSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("closeClientInvoiceSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-invoice-job-group");
		cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "closeInvoicesJobDetail")
	public JobDetailFactoryBean closeInvoicesJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CloseInvoicesSchedulerJob.class);
		jobDetailFactory.setName("CloseInvoicesJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-invoice-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "closeInvoicesTrigger")
	public CronTriggerFactoryBean closeInvoicesTrigger(@Qualifier("closeInvoicesJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("CloseInvoicesTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("CloseInvoicesTrigger");
		cronTriggerFactoryBean.setGroup("eps-invoice-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processClientMamJobDetail")
	public JobDetailFactoryBean processClientMamSchedularJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessClientMAMFileSchedulerJob.class);
		jobDetailFactory.setName("processClientMamSchedularJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-invoice-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processClientMamSchedularJobTrigger")
	public CronTriggerFactoryBean processClientMamSchedularJobTrigger(@Qualifier("processClientMamJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processClientMamSchedularJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processClientMamSchedularJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-invoice-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processInvoiceInboundJournalJobDetail")
	public JobDetailFactoryBean processInvoiceInboundJournalJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessInvoiceInboundJournalSchedulerJob.class);
		jobDetailFactory.setName("ProcessInvoiceInboundJournalJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-invoice-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processInvoiceInboundJournalTrigger")
	public CronTriggerFactoryBean processInvoiceInboundJournalTrigger(@Qualifier("processInvoiceInboundJournalJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ProcessInvoiceInboundJournalTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessInvoiceInboundJournalTrigger");
		cronTriggerFactoryBean.setGroup("eps-invoice-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processInvoiceJournalForClientBillsJobDetail")
	public JobDetailFactoryBean processJournalToInvoiceSchedularJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessJournalToInvoiceSchedulerJob.class);
		jobDetailFactory.setName("processInvoiceJournalForClientBillsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-invoice-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processJournalToInvoiceSchedularJobTrigger")
	public CronTriggerFactoryBean processJournalToInvoiceSchedularJobTrigger(@Qualifier("processInvoiceJournalForClientBillsJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processJournalToInvoiceSchedularJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processJournalToInvoiceSchedularJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-invoice-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processMAMFileJobDetail")
	public JobDetailFactoryBean processMAMFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessMAMFileSchedulerJob.class);
		jobDetailFactory.setName("ProcessMAMFileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-invoice-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processMAMFileTrigger")
	public CronTriggerFactoryBean processMAMFileTrigger(@Qualifier("processMAMFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ProcessMAMFileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessMAMFileTrigger");
		cronTriggerFactoryBean.setGroup("eps-invoice-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
