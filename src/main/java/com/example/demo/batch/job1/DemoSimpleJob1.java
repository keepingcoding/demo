package com.example.demo.batch.job1;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 单个job，以及listener
 *
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob1 extends CoreJobConfiguration {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);

        log.info("this is before DemoSimpleJob1 job listener ~~~");
    }

    @Bean(name = "DemoSimpleJob1.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory()
                .get("DemoSimpleJob1.simpleJob")
                .listener(this)
                //.incrementer(new RunIdIncrementer())
                .start(simpleStep())
                .build();

        //注册job
        //ReferenceJobFactory referenceJobFactory = new ReferenceJobFactory(job);
        //jobRegistry.register(referenceJobFactory);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution);

        log.info("this is before DemoSimpleJob1.simpleStep step listener ~~~");
    }

    @Bean(name = "DemoSimpleJob1.simpleStep")
    Step simpleStep() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob1.simpleStep")
                .listener(this)
                //.allowStartIfComplete(true)
                .tasklet((stepContribution, chunkContext) -> {
                    log.info("DemoSimpleJob1.simpleStep >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    Thread.sleep(3000L);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
