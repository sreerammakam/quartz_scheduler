package com.lca.eps.payment;

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
import com.lca.eps.jobs.CheckFilesProcessedSchedulerJob;
import com.lca.eps.payment.jobs.AddSettlementResponseToBatchRefundsSchedulerJob;
import com.lca.eps.payment.jobs.BdylBatchProcessingJob;
import com.lca.eps.payment.jobs.CancelPymtsWithoutSignBeforeStlmtSchedulerJob;
import com.lca.eps.payment.jobs.ChaseNonEpsAutoRefundSchedulerJob;
import com.lca.eps.payment.jobs.CollectionFileGenerationJob;
import com.lca.eps.payment.jobs.ConsolidatedRemittancePayment;
import com.lca.eps.payment.jobs.ConsolidatedRemittancePaymentForPatientJob;
import com.lca.eps.payment.jobs.CreateAndUpdateSettlementBatchForACISchedulerJob;
import com.lca.eps.payment.jobs.CreateBatchAndUpdateSettlementSchedulerJob;
import com.lca.eps.payment.jobs.CreateClientCardSettlementBatchSchedulerJob;
import com.lca.eps.payment.jobs.GeneratDailyBasisReversalReportSchedulerJob;
import com.lca.eps.payment.jobs.GeneratDailyBasisSummaryReportSchedulerJob;
import com.lca.eps.payment.jobs.GenerateNachaFileSchedulerJob;
import com.lca.eps.payment.jobs.GenerateNachaPatientFileSchedulerJob;
import com.lca.eps.payment.jobs.GenerateSettlementBatchFileSchedulerJob;
import com.lca.eps.payment.jobs.PTPDailyReportJob;
import com.lca.eps.payment.jobs.PatientPaymentPlanNotificationsJob;
import com.lca.eps.payment.jobs.PatientScheduledPaymentNotificationsJob;
import com.lca.eps.payment.jobs.PaymentAdjustmentReportJob;
import com.lca.eps.payment.jobs.PaymentErrorReportSchedulerJob;
import com.lca.eps.payment.jobs.ProcessCreditCardCaptureSummaryJob;
import com.lca.eps.payment.jobs.ProcessLcbsChangesFileJob;
import com.lca.eps.payment.jobs.ProcessOCRFileSchedulerJob;
import com.lca.eps.payment.jobs.ProcessPatientPaymentPlanJob;
import com.lca.eps.payment.jobs.ProcessPaymentFileSchedulerJob;
import com.lca.eps.payment.jobs.ProcessSubscriptionsJob;
import com.lca.eps.payment.jobs.ProcessSubscriptionsRemitJob;
import com.lca.eps.payment.jobs.SendNotificationEmailSchedulerJob;
import com.lca.eps.payment.jobs.SettleAllSchedulerJob;
import com.lca.eps.payment.jobs.TokenizeSchedulerJob;
import com.lca.eps.payment.jobs.UpdateMangerDetailsJob;
import com.lca.eps.payment.jobs.UpdatePaymentPortalSchedulerJob;
import com.lca.eps.payment.jobs.VantivPaymentReconciliationAfterCancellationJob;
import com.lca.eps.payment.jobs.VantivPaymentReconciliationBeforeCancellationJob;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Configuration
public class BatchJobsConfig {

	private final PropertyValueDao propertyValueDao;
	private final Environment environment;

	private Map<String, String> propertyValueMap;

	@PostConstruct
	public void init() {
		propertyValueMap = propertyValueDao.findPropertyNameToPropertyValue();
	}

	@Autowired
	public BatchJobsConfig(PropertyValueDao propertyValueDao, Environment environment) {
		this.propertyValueDao = propertyValueDao;
		this.environment = environment;
	}

	@Bean(name = "addSettlementResponseToBatchRefundsJobDetail")
	public JobDetailFactoryBean addSettlementResponseToBatchRefundsJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(AddSettlementResponseToBatchRefundsSchedulerJob.class);
		jobDetailFactory.setName("AddSettlementResponseToBatchRefundsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "addSettlementResponseToBatchRefundsTrigger")
	public CronTriggerFactoryBean addSettlementResponseToBatchRefundsTrigger(@Qualifier("addSettlementResponseToBatchRefundsJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("AddSettlementResponseToBatchRefundsTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("AddSettlementResponseToBatchRefundsTrigger");
		cronTriggerFactoryBean.setGroup("eps-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "bdylBatchProcessingJobDetail")
	public JobDetailFactoryBean bdylBatchProcessingJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(BdylBatchProcessingJob.class);
		jobDetailFactory.setName("bdylBatchProcessingJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "bdylBatchProcessingJobTrigger")
	public CronTriggerFactoryBean bdylBatchProcessingJobTrigger(@Qualifier("bdylBatchProcessingJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("bdylBatchProcessingJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("BdylBatchProcessingJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "cancelPymtsWithoutSignBeforeStlmtJobDetail")
	public JobDetailFactoryBean cancelPymtsWithoutSignBeforeSettlementJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CancelPymtsWithoutSignBeforeStlmtSchedulerJob.class);
		jobDetailFactory.setName("CancelPymtsWithoutSignBeforeStlmtSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "cancelPymtsWithoutSignBeforeStlmtSchedulerJobTrigger")
	public CronTriggerFactoryBean cancelPymtsWithoutSignBeforeSettlementJobTrigger(@Qualifier("cancelPymtsWithoutSignBeforeStlmtJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("cancelPymtsWithoutSignBeforeStlmtSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("CancelPymtsWithoutSignBeforeStlmtSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "collectionFileGenerationJobDetail")
	public JobDetailFactoryBean collectionFileGenerationJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CollectionFileGenerationJob.class);
		jobDetailFactory.setName("CollectionFileGenerationJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "collectionFileGenerationJobTrigger")
	public CronTriggerFactoryBean collectionFileGenerationJobTrigger(@Qualifier("collectionFileGenerationJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("collectionFileGenerationJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("collectionFileGenerationJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "consolidatedRemittancePaymentJobDetail")
	public JobDetailFactoryBean consolidatedRemittancePaymentJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ConsolidatedRemittancePayment.class);
		jobDetailFactory.setName("consolidatedRemittancePaymentJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "consolidatedRemittancePaymentJobTrigger")
	public CronTriggerFactoryBean consolidatedRemittancePaymentJobTrigger(@Qualifier("consolidatedRemittancePaymentJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("consolidatedRemittancePaymentJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("consolidatedRemittancePaymentJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "consolidatedRemittancePaymentForPatientJobDetail")
	public JobDetailFactoryBean consolidatedRemittancePaymentForPatientJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ConsolidatedRemittancePaymentForPatientJob.class);
		jobDetailFactory.setName("ConsolidatedRemittancePaymentForPatientJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "consolidatedRemittancePaymentForPatientJobTrigger")
	public CronTriggerFactoryBean consolidatedRemittancePaymentForPatientJobTrigger(@Qualifier("consolidatedRemittancePaymentForPatientJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("consolidatedRemittancePaymentForPatient.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("consolidatedRemittancePaymentForPatientJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "createAndUpdateSettlementBatchForACIJobDetail")
	public JobDetailFactoryBean createAndUpdateSettlementBatchForACIJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CreateAndUpdateSettlementBatchForACISchedulerJob.class);
		jobDetailFactory.setName("CreateAndUpdateSettlementBatchForACIJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "createAndUpdateSettlementBatchForACIJobTrigger")
	public CronTriggerFactoryBean createAndUpdateSettlementBatchForACIJobTrigger(@Qualifier("createAndUpdateSettlementBatchForACIJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("createandupdatesettlementbatchforacijob.cron.expresion"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("CreateAndUpdateSettlementBatchForACIJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "CreateBatchAndUpdateSettlementJobDetail")
	public JobDetailFactoryBean createBatchAndUpdateSettlementJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CreateBatchAndUpdateSettlementSchedulerJob.class);
		jobDetailFactory.setName("CreateBatchAndUpdateSettlementSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "CreateBatchAndUpdateSettlementSchedulerJobTrigger")
	public CronTriggerFactoryBean createBatchAndUpdateSettlementSchedulerJobTrigger(@Qualifier("CreateBatchAndUpdateSettlementJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("CreateBatchAndUpdateSettlementSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("CreateBatchAndUpdateSettlementSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "createClientCardSettlementBatchJobDetail")
	public JobDetailFactoryBean createClientCardSettlementBatchSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CreateClientCardSettlementBatchSchedulerJob.class);
		jobDetailFactory.setName("createClientCardSettlementBatchSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-reports-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "createClientCardSettlementBatchSchedulerJobTrigger")
	public CronTriggerFactoryBean createClientCardSettlementBatchSchedulerJobTrigger(@Qualifier("createClientCardSettlementBatchJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("createClientCardSettlementBatchSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("createClientCardSettlementBatchSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-reports-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateDailyBasisReversalReportJobDetail")
	public JobDetailFactoryBean generateDailyBasisReversalReportSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GeneratDailyBasisReversalReportSchedulerJob.class);
		jobDetailFactory.setName("generateDailyBasisReversalReportSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-reports-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateDailyBasisReversalReportJobSchedulerJobTrigger")
	public CronTriggerFactoryBean generateDailyBasisReversalReportJobSchedulerJobTrigger(@Qualifier("generateDailyBasisReversalReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generateDailyBasisReversalReportJobSchedulerJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateDailyBasisReversalReportJobSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-reports-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateDailyBasisSummaryReportJobDetail")
	public JobDetailFactoryBean generateDailyBasisSummaryReportSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GeneratDailyBasisSummaryReportSchedulerJob.class);
		jobDetailFactory.setName("generateDailyBasisSummaryReportSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-reports-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateDailyBasisSummaryReportJobSchedulerTrigger")
	public CronTriggerFactoryBean generateDailyBasisSummaryReportJobSchedulerJobTrigger(@Qualifier("generateDailyBasisSummaryReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generateDailyBasisSummaryReportJobSchedulerJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateDailyBasisSummaryReportJobSchedulerTrigger");
		cronTriggerFactoryBean.setGroup("eps-reports-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateNachaFileJobDetail")
	public JobDetailFactoryBean generateNachaFileSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateNachaFileSchedulerJob.class);
		jobDetailFactory.setName("generateNachaFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateNachaFileSchedulerJobTrigger")
	public CronTriggerFactoryBean generateNachaFileSchedulerJobTrigger(@Qualifier("generateNachaFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generateNachaFileSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateNachaFileSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateNachaPatientFiledJobDetail")
	public JobDetailFactoryBean generateNachaPatientFileSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateNachaPatientFileSchedulerJob.class);
		jobDetailFactory.setName("generateNachaPatientFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateNachaPatientFileSchedulerJobTrigger")
	public CronTriggerFactoryBean generateNachaPatientFileSchedulerJobTrigger(@Qualifier("generateNachaPatientFiledJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generatePatientNachaFileSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateNachaPatientFileSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "generateSettlementBatchFileJobDetail")
	public JobDetailFactoryBean genrateSettlementBatchFileSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(GenerateSettlementBatchFileSchedulerJob.class);
		jobDetailFactory.setName("generateSettlementBatchFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "generateSettlementBatchFileSchedulerJobTrigger")
	public CronTriggerFactoryBean generateSettlementBatchFileSchedulerJobTrigger(@Qualifier("generateSettlementBatchFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("generateSettlementBatchFileSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("generateSettlementBatchFileSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "patientPaymentPlanNotificationsJobDetail")
	public JobDetailFactoryBean patientPaymentPlanNotificationsJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PatientPaymentPlanNotificationsJob.class);
		jobDetailFactory.setName("PatientPaymentPlanNotificationsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "patientPaymentPlanNotificationsJobTrigger")
	public CronTriggerFactoryBean patientPaymentPlanNotificationsJobTrigger(@Qualifier("patientPaymentPlanNotificationsJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("patientPaymentPlanNotificationsJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("patientPaymentPlanNotificationsJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "patientScheduledPaymentNotificationsJobDetail")
	public JobDetailFactoryBean patientScheduledPaymentNotificationsJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PatientScheduledPaymentNotificationsJob.class);
		jobDetailFactory.setName("PatientScheduledPaymentNotificationsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "patientScheduledPaymentNotificationsJobTrigger")
	public CronTriggerFactoryBean patientScheduledPaymentNotificationsJobTrigger(@Qualifier("patientScheduledPaymentNotificationsJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("patientScheduledPaymentNotificationsJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("patientScheduledPaymentNotificationsJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "paymentAdjustmentReportJobDetail")
	public JobDetailFactoryBean paymentAdjustmentReportJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PaymentAdjustmentReportJob.class);
		jobDetailFactory.setName("PaymentAdjustmentReportJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "paymentAdjustmentReportJobTrigger")
	public CronTriggerFactoryBean paymentAdjustmentReportJobTrigger(@Qualifier("paymentAdjustmentReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("paymentAdjustmentReportJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("PaymentAdjustmentReportJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "paymentErrorReportJobDetail")
	public JobDetailFactoryBean paymentErrorReportSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PaymentErrorReportSchedulerJob.class);
		jobDetailFactory.setName("paymentErrorReportSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "paymentErrorReportSchedulerJobTrigger")
	public CronTriggerFactoryBean paymentErrorReportSchedulerJobTrigger(@Qualifier("paymentErrorReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("paymentErrorReportSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("paymentErrorReportSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processCreditCardCaptureSummaryJobDetail")
	public JobDetailFactoryBean processCreditCardCaptureSummaryJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessCreditCardCaptureSummaryJob.class);
		jobDetailFactory.setName("processCreditCardCaptureSummaryJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-reports-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processCreditCardCaptureSummaryJobTrigger")
	public CronTriggerFactoryBean processCreditCardCaptureSummaryJobTrigger(@Qualifier("processCreditCardCaptureSummaryJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processCreditCardCaptureSummaryJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processCreditCardCaptureSummaryJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-reports-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processLcbsChangesFileJobDetail")
	public JobDetailFactoryBean processLcbsChangesFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessLcbsChangesFileJob.class);
		jobDetailFactory.setName("ProcessLcbsChangesFileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processLcbsChangesFileJobTrigger")
	public CronTriggerFactoryBean processLcbsChangesFileJobTrigger(@Qualifier("processLcbsChangesFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processLcbsChangesFileJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processLcbsChangesFileJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processOCRFileJobDetail")
	public JobDetailFactoryBean processOCRFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessOCRFileSchedulerJob.class);
		jobDetailFactory.setName("ProcessOCRFileSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processOCRFileJobTrigger")
	public CronTriggerFactoryBean processOCRFileJobTrigger(@Qualifier("processOCRFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processOCRFileJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessOCRFileJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processPatientPaymentPlanJobDetail")
	public JobDetailFactoryBean processPatientPaymentPlanJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessPatientPaymentPlanJob.class);
		jobDetailFactory.setName("ProcessPatientPaymentPlanJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processPatientPaymentPlanJobTrigger")
	public CronTriggerFactoryBean processPatientPaymentPlanJobTrigger(@Qualifier("processPatientPaymentPlanJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processPatientPaymentPlanJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processPatientPaymentPlanJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processPaymentFileJobDetail")
	public JobDetailFactoryBean processPaymentFileJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessPaymentFileSchedulerJob.class);
		jobDetailFactory.setName("ProcessPaymentFileJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processPaymentFileTrigger")
	public CronTriggerFactoryBean processPaymentFileTrigger(@Qualifier("processPaymentFileJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ProcessPaymentFileTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessPaymentFileTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processPaymentFileSatJobDetail")
	public JobDetailFactoryBean processPaymentFileSatJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessPaymentFileSchedulerJob.class);
		jobDetailFactory.setName("ProcessPaymentFileSatJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processPaymentFileSatTrigger")
	public CronTriggerFactoryBean processPaymentFileSatTrigger(@Qualifier("processPaymentFileSatJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("ProcessPaymentFileSatTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ProcessPaymentFileSatTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "ptpDailyReportJobDetail")
	public JobDetailFactoryBean ptpDailyReportJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(PTPDailyReportJob.class);
		jobDetailFactory.setName("PTPDailyReportJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "ptpDailyReportJobTrigger")
	public CronTriggerFactoryBean ptpDailyReportJobTrigger(@Qualifier("ptpDailyReportJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		String cron = propertyValueMap.get("ptpDailyReportJob.cron.expression");
		if (StringUtils.isBlank(cron)) {
			cron = environment.getRequiredProperty("ptpDailyReportJob.cron.expression");
		}
		cronTriggerFactoryBean.setCronExpression(cron);
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ptpDailyReportJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "sendNotificationEmailJobDetail")
	public JobDetailFactoryBean sendNotificationEmailSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(SendNotificationEmailSchedulerJob.class);
		jobDetailFactory.setName("sendNotificationEmailSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "sendNotificationEmailSchedulerJobTrigger")
	public CronTriggerFactoryBean sendNotificationEmailSchedulerJobTrigger(@Qualifier("sendNotificationEmailJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("sendNotificationEmailSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("sendNotificationEmailSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "settleAllJobDetail")
	public JobDetailFactoryBean settleAllJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(SettleAllSchedulerJob.class);
		jobDetailFactory.setName("SettleAllJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "settleAllTrigger")
	public CronTriggerFactoryBean settleAllTrigger(@Qualifier("settleAllJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("SettleAllTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("SettleAllTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "tokenizeJobDetail")
	public JobDetailFactoryBean tokenizeJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(TokenizeSchedulerJob.class);
		jobDetailFactory.setName("TokenizeSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "tokenizeSchedulerJobTrigger")
	public CronTriggerFactoryBean tokenizeJobTrigger(@Qualifier("tokenizeJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("TokenizeJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("TokenizeJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "updateMangerDetailsJobDetail")
	public JobDetailFactoryBean updateMangerDetailsJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(UpdateMangerDetailsJob.class);
		jobDetailFactory.setName("UpdateMangerDetailsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "updateMangerDetailsJobTrigger")
	public CronTriggerFactoryBean updateMangerDetailsJobTrigger(@Qualifier("updateMangerDetailsJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("UpdateMangerDetailsJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("updateMangerDetailsJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "updatePaymentPortalJobDetail")
	public JobDetailFactoryBean updatePaymentPortalSchedulerJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(UpdatePaymentPortalSchedulerJob.class);
		jobDetailFactory.setName("updatePaymentPortalSchedulerJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "updatePaymentPortalSchedulerJobTrigger")
	public CronTriggerFactoryBean updatePaymentPortalSchedulerJobTrigger(@Qualifier("updatePaymentPortalJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("updatePaymentPortalSchedulerJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("updatePaymentPortalSchedulerJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "vantivPaymentReconciliationAfterCancellationJobDetail")
	public JobDetailFactoryBean vantivPaymentReconciliationAfterCancellationJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(VantivPaymentReconciliationAfterCancellationJob.class);
		jobDetailFactory.setName("vantivPaymentReconciliationAfterCancellationJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "vantivPaymentReconciliationAfterCancellationJobTrigger")
	public CronTriggerFactoryBean vantivPaymentReconciliationAfterCancellationJobTrigger(@Qualifier("vantivPaymentReconciliationAfterCancellationJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("vantivPaymentReconciliationAfterCancellationJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("vantivPaymentReconciliationAfterCancellationJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "vantivPaymentReconciliationBeforeCancellationJobDetail")
	public JobDetailFactoryBean vantivPaymentReconciliationBeforeCancellationJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(VantivPaymentReconciliationBeforeCancellationJob.class);
		jobDetailFactory.setName("VantivPaymentReconciliationBeforeCancellationJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "vantivPaymentReconciliationBeforeCancellationJobTrigger")
	public CronTriggerFactoryBean vantivPaymentReconciliationBeforeCancellationJobTrigger(@Qualifier("vantivPaymentReconciliationBeforeCancellationJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("vantivPaymentReconciliationBeforeCancellationJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("VantivPaymentReconciliationBeforeCancellationJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "checkFilesProcessedJob")
	public JobDetailFactoryBean checkFilesProcessedJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(CheckFilesProcessedSchedulerJob.class);
		jobDetailFactory.setName("CheckFilesProcessedJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "checkFilesProcessedTrigger")
	public CronTriggerFactoryBean checkFilesProcessedTrigger(@Qualifier("checkFilesProcessedJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("CheckFilesProcessedTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("CheckFilesProcessedTrigger");
		cronTriggerFactoryBean.setGroup("eps-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processSubscriptionJob")
	public JobDetailFactoryBean processSubscriptionJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessSubscriptionsJob.class);
		jobDetailFactory.setName("ProcessSubscriptionsJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processSubscriptionJobTrigger")
	public CronTriggerFactoryBean processSubscriptionJobTrigger(@Qualifier("processSubscriptionJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processSubscriptionsJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processSubscriptionJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "processSubscriptionRemitJob")
	public JobDetailFactoryBean processSubscriptionRemitJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ProcessSubscriptionsRemitJob.class);
		jobDetailFactory.setName("ProcessSubscriptionsRemitJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "processSubscriptionRemitJobTrigger")
	public CronTriggerFactoryBean processSubscriptionRemitJobTrigger(@Qualifier("processSubscriptionRemitJob") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("processSubscriptionsRemitJobTrigger.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("processSubscriptionRemitJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}

	@Bean(name = "chaseNonEpsAutoRefundJobDetail")
	public JobDetailFactoryBean chaseNonEpsAutoRefundJob() {
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(ChaseNonEpsAutoRefundSchedulerJob.class);
		jobDetailFactory.setName("chaseNonEpsAutoRefundJob");
		jobDetailFactory.setDurability(true);
		jobDetailFactory.setGroup("eps-payment-job-group");
		return jobDetailFactory;
	}

	@Bean(name = "chaseNonEpsAutoRefundJobTrigger")
	public CronTriggerFactoryBean chaseNonEpsAutoRefundJobTrigger(@Qualifier("chaseNonEpsAutoRefundJobDetail") JobDetail jobDetail) {
		CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
		cronTriggerFactoryBean.setJobDetail(jobDetail);
		cronTriggerFactoryBean.setCronExpression(environment.getRequiredProperty("chaseNonEpsAutoRefundJob.cron.expression"));
		cronTriggerFactoryBean.setStartDelay(5000);
		cronTriggerFactoryBean.setName("ChaseNonEpsAutoRefundJobTrigger");
		cronTriggerFactoryBean.setGroup("eps-payment-trigger-group");
		cronTriggerFactoryBean.setMisfireInstruction(org.quartz.CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
		return cronTriggerFactoryBean;
	}
}
