package com.lca.eps.payment;

import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import com.lca.eps.payment.jobs.ACILkbxRvrslToUpdateVendorSchedulerJob;
import com.lca.eps.payment.jobs.CreateReconcilationCSVSchedulerJob;
import com.lca.eps.payment.jobs.DuplicatePaymentReportJob;
import com.lca.eps.payment.jobs.GenerateDailySpecimenEmailSchedulerJob;
import com.lca.eps.payment.jobs.GenerateLcbsPaymentFilesSchedulerJob;
import com.lca.eps.payment.jobs.GeneratePEPPaymentFileSchedulerJob;
import com.lca.eps.payment.jobs.ProcessConfirmationDataFileSchedulerJob;
import com.lca.eps.payment.jobs.ProcessLockboxReversalSchedulerJob;
import com.lca.eps.payment.jobs.ProcessReturnDataFileSchedulerJob;
import com.lca.eps.payment.jobs.ProcessVendorSettlementFileSchedulerJob;
import com.lca.eps.payment.jobs.ReconcileRRBB129SchedulerJob;
import com.lca.eps.payment.jobs.ReconcileRRTB317SchedulerJob;
import com.lca.eps.payment.jobs.ReconciliationProcessSchedulerJob;

@Configuration
public class ReportingJobsConfig {

	private final Environment environment;

	@Autowired
	public ReportingJobsConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean(name = "aciLkbxRvrslToUpdateVendorJobDetail")
	public JobDetailFactoryBean aciLkbxRvrslToUpdateVendorSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ACILkbxRvrslToUpdateVendorSchedulerJob.class);
		jobDetailFactory.setName("aciLkbxRvrslToUpdateVendorSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "aciLkbxRvrslToUpdateVendorSchedulerJobTrigger")
	public CronTriggerFactoryBean aciLkbxRvrslToUpdateVendorSchedulerJobTrigger(@Qualifier("aciLkbxRvrslToUpdateVendorJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ACILkbxRvrslUpdate.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("aciLkbxRvrslToUpdateVendorSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "createReconcilationCSVFileJobDetail")
	public JobDetailFactoryBean createReconcilationCSVFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CreateReconcilationCSVSchedulerJob.class);
		jobDetailFactory.setName("CreateReconcilationCSVFileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "createReconcilationCSVFileJobTrigger")
	public CronTriggerFactoryBean createReconcilationCSVFileJobTrigger(@Qualifier("createReconcilationCSVFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("CreateReconcilationCSVFileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("CreateReconcilationCSVFileJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "duplicatePaymentReportJobDetail")
	public JobDetailFactoryBean duplicatePaymentReportJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(DuplicatePaymentReportJob.class);
		jobDetailFactory.setName("duplicatePaymentReportJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "duplicatePaymentReportJobTrigger")
	public CronTriggerFactoryBean duplicatePaymentReportJobTrigger(@Qualifier("duplicatePaymentReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("duplicatePaymentReportJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("DuplicatePaymentReportJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateDailySpecimenEmailReportJobDetail")
	public JobDetailFactoryBean generateDailySpecimenEmailReportJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateDailySpecimenEmailSchedulerJob.class);
		jobDetailFactory.setName("GenerateDailySpecimenEmailSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateDailySpecimenEmailReportTrigger")
	public CronTriggerFactoryBean generateDailySpecimenEmailReportTrigger(@Qualifier("generateDailySpecimenEmailReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generateDailySpecimenEmailReportJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateDailySpecimenEmailReportTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateLcbsPaymentFilesJobDetail")
	public JobDetailFactoryBean generateLcbsPaymentFilesJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateLcbsPaymentFilesSchedulerJob.class);
		jobDetailFactory.setName("GenerateLcbsPaymentFilesJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateLcbsPaymentFilesTrigger")
	public CronTriggerFactoryBean generateLcbsPaymentFilesTrigger(@Qualifier("generateLcbsPaymentFilesJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("GenerateLcbsPaymentFilesTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("GenerateLcbsPaymentFilesTrigger");
		cronTriggerFactoryBean.setGroup("eps-payments-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateLcbsPaymentFilesSatJobDetail")
	public JobDetailFactoryBean generateLcbsPaymentFilesSatJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateLcbsPaymentFilesSchedulerJob.class);
		jobDetailFactory.setName("GenerateLcbsPaymentFilesSatJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateLcbsPaymentFilesSatTrigger")
	public CronTriggerFactoryBean generateLcbsPaymentFilesSatTrigger(@Qualifier("generateLcbsPaymentFilesSatJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("GenerateLcbsPaymentFilesSatTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("GenerateLcbsPaymentFilesSatTrigger");
		cronTriggerFactoryBean.setGroup("eps-payments-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generatePEPPaymentFileJobDetail")
	public JobDetailFactoryBean generatePEPPaymentFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GeneratePEPPaymentFileSchedulerJob.class);
		jobDetailFactory.setName("generatePEPPaymentFileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generatePEPPaymentFileTrigger")
	public CronTriggerFactoryBean generatePEPPaymentFileTrigger(@Qualifier("generatePEPPaymentFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("GeneratePEPPaymentFileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generatePEPPaymentFileTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generatePEPPaymentFileSchedulerWeekEndJobDetail")
	public JobDetailFactoryBean generatePEPPaymentFileSaturdayJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GeneratePEPPaymentFileSchedulerJob.class);
		jobDetailFactory.setName("generatePEPPaymentFileSchedulerWeekEndJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generatePEPPaymentFileSaturdayTrigger")
	public CronTriggerFactoryBean generatePEPPaymentFileSaturdayTrigger(@Qualifier("generatePEPPaymentFileSchedulerWeekEndJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("GeneratePEPPaymentFileSatTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generatePEPPaymentFileSaturdayTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processConfirmationDataFileJobDetail")
	public JobDetailFactoryBean processConfirmationDataFileSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessConfirmationDataFileSchedulerJob.class);
		jobDetailFactory.setName("processConfirmationDataFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processConfirmationDataFileSchedulerJobTrigger")
	public CronTriggerFactoryBean processConfirmationDataFileSchedulerJobTrigger(@Qualifier("processConfirmationDataFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processConfirmationDataFileSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processConfirmationDataFileSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processLockboxReversalJobDetail")
	public JobDetailFactoryBean processLockboxReversalSchedularJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessLockboxReversalSchedulerJob.class);
		jobDetailFactory.setName("processLockboxReversalSchedularJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processLockboxReversalSchedularJobTrigger")
	public CronTriggerFactoryBean processLockboxReversalSchedularJobTrigger(@Qualifier("processLockboxReversalJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processLockboxReversalSchedularJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processLockboxReversalSchedularJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processLockboxReversalSchedularWeekEndJobDetail")
	public JobDetailFactoryBean processLockboxReversalSchedularWeekEndJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessLockboxReversalSchedulerJob.class);
		jobDetailFactory.setName("processLockboxReversalSchedularWeekEndJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processLockboxReversalSchedularJobWeekEndTrigger")
	public CronTriggerFactoryBean processLockboxReversalSchedularJobWeekEndTrigger(@Qualifier("processLockboxReversalSchedularWeekEndJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processLockboxReversalSchedularJobTrigger.cron.expression.weekend"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processLockboxReversalSchedularJobWeekEndTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processReturnDataFileJobDetail")
	public JobDetailFactoryBean processReturnDataFileSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessReturnDataFileSchedulerJob.class);
		jobDetailFactory.setName("processReturnDataFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processReturnDataFileSchedulerJobTrigger")
	public CronTriggerFactoryBean processReturnDataFileSchedulerJobTrigger(@Qualifier("processReturnDataFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processReturnDataFileSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processReturnDataFileSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processVendorSettlementFileJobDetail")
	public JobDetailFactoryBean processVendorSettlementFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessVendorSettlementFileSchedulerJob.class);
		jobDetailFactory.setName("ProcessVendorSettlementFileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processVendorSettlementFileJobTrigger")
	public CronTriggerFactoryBean processVendorSettlementFileJobTrigger(@Qualifier("processVendorSettlementFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processVendorSettlementFileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessVendorSettlementFileJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "reconcileRRBB129FileJobDetail")
	public JobDetailFactoryBean reconcileRRBB129FileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ReconcileRRBB129SchedulerJob.class);
		jobDetailFactory.setName("ReconcileRRBB129FileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "reconcileRRBB129FileJobTrigger")
	public CronTriggerFactoryBean reconcileRRBB129FileJobTrigger(@Qualifier("reconcileRRBB129FileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("reconcileRRBB129FileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ReconcileRRBB129FileJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "reconcileRRTB317FileJobDetail")
	public JobDetailFactoryBean reconcileRRTB317FileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ReconcileRRTB317SchedulerJob.class);
		jobDetailFactory.setName("ReconcileRRTB317FileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "reconcileRRTB317FileJobTrigger")
	public CronTriggerFactoryBean reconcileRRTB317FileJobTrigger(@Qualifier("reconcileRRTB317FileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ReconcileRRTB317FileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ReconcileRRTB317FileJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "ReconciliationProcessJobDetail")
	public JobDetailFactoryBean reconciliationProcessSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ReconciliationProcessSchedulerJob.class);
		jobDetailFactory.setName("ProcessReconciliationJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processReconciliationJobTrigger")
	public CronTriggerFactoryBean processReconciliationJobTrigger(@Qualifier("ReconciliationProcessJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processPaymentReconciliationJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processReconciliationJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
