package com.example.demo.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author zhaozisheng
 * @version 1.0
 * @date 2019-05-15 17:01
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class CoreJobConfiguration implements JobExecutionListener, StepExecutionListener, ChunkListener {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobLocator jobLocator;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    protected JobBuilderFactory getJobBuilderFactory() {
        return this.jobBuilderFactory;
    }

    protected StepBuilderFactory getStepBuilderFactory() {
        return this.stepBuilderFactory;
    }

    protected JobStepBuilder jobStepBuilder(String stepName, String jobName) throws Exception {
        return new JobStepBuilder(new StepBuilder(stepName))
                .job(this.jobLocator.getJob(jobName))
                .launcher(this.jobLauncher)
                .repository(jobRepository())
                .transactionManager(this.platformTransactionManager);
    }

    protected JobStepBuilder jobStepBuilder(String stepName, Job job) throws Exception {
        return new JobStepBuilder(new StepBuilder(stepName))
                .job(job)
                .launcher(this.jobLauncher)
                .repository(jobRepository())
                .transactionManager(this.platformTransactionManager);
    }

    /**
     * 作业仓库,把job运行过程中产生的数据持久化到数据库
     * DataSource 数据源,多个数据源的时候指定按名称注入
     */
    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(this.dataSource);
        jobRepositoryFactoryBean.setTransactionManager(this.platformTransactionManager);
        jobRepositoryFactoryBean.setDatabaseType(DatabaseType.MYSQL.name());
        return jobRepositoryFactoryBean.getObject();
    }

    /**
     * 这是一个bean后处理器，可以在创建时注册所有作业
     *
     * @param jobRegistry
     * @return
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    /**
     * 以下为监听器
     *
     * @param jobExecution
     */
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("CoreJobConfiguration.beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("CoreJobConfiguration.afterJob");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("CoreJobConfiguration.beforeStep");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("CoreJobConfiguration.afterStep");

        return stepExecution.getExitStatus();
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        log.info("CoreJobConfiguration.beforeChunk");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("CoreJobConfiguration.afterChunk");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.info("CoreJobConfiguration.afterChunkError");
    }
}
