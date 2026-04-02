package com.lca.eps.payment;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.payment.jobs.DuplicatePaymentAutoVoidJob;
import com.lca.eps.payment.jobs.GenerateDuplicatePaymentReportJob;
import com.lca.eps.payment.jobs.PatientScheduledPaymentsProcessingJob;
import com.lca.eps.payment.jobs.PepcFailedPaymentsRetryJob;
import com.lca.eps.payment.jobs.ProcessPaymetricBatchRefundsJob;
import com.lca.eps.payment.jobs.ProcessPepECheckNSFReportSchedulerJob;
import com.lca.eps.payment.jobs.ProcessPepPaymentFileSchedulerJob;
import com.lca.eps.payment.jobs.RemittanceScheduledPayment;

@Configuration
public class PaymentJobsConfig {

	private final Environment environment;

	@Autowired
	public PaymentJobsConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "duplicatePaymentAutoVoidJobDetail")
	public JobDetailFactoryBean duplicatePaymentAutoVoidJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(DuplicatePaymentAutoVoidJob.class);
		jobDetailFactory.setName("duplicatePaymentAutoVoidJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "duplicatePaymentAutoVoidJobTrigger")
	public CronTriggerFactoryBean duplicatePaymentAutoVoidJobTrigger(@Qualifier("duplicatePaymentAutoVoidJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("duplicatePaymentAutoVoidJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("DuplicatePaymentAutoVoidJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateDuplicatePaymentReportJobDetail")
	public JobDetailFactoryBean generateDuplicatePaymentReportJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateDuplicatePaymentReportJob.class);
		jobDetailFactory.setName("GenerateDuplicatePaymentReportJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateDuplicatePaymentReportJobTrigger")
	public CronTriggerFactoryBean generateDuplicatePaymentReportJobTrigger(@Qualifier("generateDuplicatePaymentReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generateDuplicatePaymentReportJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateDuplicatePaymentReportJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "patientScheduledPaymentsProcessingJobDetail")
	public JobDetailFactoryBean patientScheduledPaymentsProcessingJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PatientScheduledPaymentsProcessingJob.class);
		jobDetailFactory.setName("PatientScheduledPaymentsProcessingJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "patientScheduledPaymentsProcessingJobTrigger")
	public CronTriggerFactoryBean patientScheduledPaymentsProcessingJobTrigger(@Qualifier("patientScheduledPaymentsProcessingJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("patientScheduledPaymentsProcessingJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("patientScheduledPaymentsProcessingJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "pepcFailedPaymentsRetryJobDetail")
	public JobDetailFactoryBean pepcFailedPaymentsRetryJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PepcFailedPaymentsRetryJob.class);
		jobDetailFactory.setName("pepcFailedPaymentsRetryJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "pepcFailedPaymentsRetryJobTrigger")
	public CronTriggerFactoryBean pepcFailedPaymentsRetryJobTrigger(@Qualifier("pepcFailedPaymentsRetryJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("pepcFailedPaymentsRetryJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("PepcFailedPaymentsRetryJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processPaymetricBatchRefundsJobDetail")
	public JobDetailFactoryBean processPaymetricBatchRefundsJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessPaymetricBatchRefundsJob.class);
		jobDetailFactory.setName("ProcessPaymetricBatchRefundsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processPaymetricBatchRefundsTrigger")
	public CronTriggerFactoryBean processPaymetricBatchRefundsTrigger(@Qualifier("processPaymetricBatchRefundsJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ProcessPaymetricBatchRefundsTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessPaymetricBatchRefundsTrigger");
		cronTriggerFactoryBean.setGroup("eps-payments-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processPepECheckNSFReportJobDetail")
	public JobDetailFactoryBean processPepECheckNSFReportSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessPepECheckNSFReportSchedulerJob.class);
		jobDetailFactory.setName("processPepECheckNSFReportSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-reports-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processPepECheckNSFReportSchedulerJobTrigger")
	public CronTriggerFactoryBean processPepECheckNSFReportSchedulerJobTrigger(@Qualifier("processPepECheckNSFReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processPepECheckNSFReportJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processPepECheckNSFReportSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-reports-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processPepPaymentFileJobDetail")
	public JobDetailFactoryBean processPepPaymentFileSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessPepPaymentFileSchedulerJob.class);
		jobDetailFactory.setName("processPepPaymentFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-reports-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processPepPaymentFileSchedulerJobTrigger")
	public CronTriggerFactoryBean processPepPaymentFileSchedulerJobTrigger(@Qualifier("processPepPaymentFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processPepPaymentFileSchedulerJob.cron.expressionM-F"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processPepPaymentFileSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-reports-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "remittanceScheduledPaymentJobDetail")
	public JobDetailFactoryBean remittanceScheduledPaymentJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(RemittanceScheduledPayment.class);
		jobDetailFactory.setName("remittanceScheduledPaymentJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "remittanceScheduledPaymentJobTrigger")
	public CronTriggerFactoryBean remittanceScheduledPaymentJobTrigger(@Qualifier("remittanceScheduledPaymentJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("remittanceScheduledPaymentJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("remittanceScheduledPaymentJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
