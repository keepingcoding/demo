package com.example.demo.batch.job8;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob8 extends CoreJobConfiguration {

    @Bean(name = "DemoSimpleJob8.simpleJob")
    public Job simpleJob() throws Exception {
        return getJobBuilderFactory()
                .get("DemoSimpleJob8.simpleJob")
                .start(simpleStepForTrace("beforeStep1", "-------------- beforeStep1 --------------"))
                .next(childJobStep1())
                .next(simpleStepForTrace("afterStep1", "-------------- afterStep1 --------------"))
                .next(childJobStep2())
                .next(simpleStepForTrace("afterStep2", "-------------- afterStep2 --------------"))
                .build();

    }

    private Step simpleStepForTrace(String stepName, String logText) {
        return getStepBuilderFactory()
                .get(stepName)
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob8.simpleStepForTrace >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    log.info(logText);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob8.childJobStep1")
    @DependsOn("DemoSimpleJob1.simpleJob")
    Step childJobStep1() throws Exception {
        return jobStepBuilder("DemoSimpleJob8.childJobStep1", "DemoSimpleJob1.simpleJob").build();
    }

    @Bean(name = "DemoSimpleJob8.childJobStep2")
    @DependsOn("DemoSimpleJob2.simpleJob")
    Step childJobStep2() throws Exception {
        return jobStepBuilder("DemoSimpleJob8.childJobStep2", "DemoSimpleJob2.simpleJob").build();
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        super.afterJob(jobExecution);

        log.info("执行完成：BatchStatus = " + jobExecution.getStatus().name());
    }
}
