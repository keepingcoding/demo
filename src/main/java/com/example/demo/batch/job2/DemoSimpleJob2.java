package com.example.demo.batch.job2;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 顺序job
 *
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob2 extends CoreJobConfiguration {

    @Bean(name = "DemoSimpleJob2.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory().get("DemoSimpleJob2.simpleJob")
                .start(simpleStep())
                .next(simpleStep2())
                .build();
    }

    @Bean(name = "DemoSimpleJob2.simpleStep1")
    Step simpleStep() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob2.simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob2.simpleStep1 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    Thread.sleep(5000L);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob2.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob2.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob2.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    Thread.sleep(2000L);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
