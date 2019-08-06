package com.example.demo.batch.job5;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob5 extends CoreJobConfiguration {

    @Bean(name = "DemoSimpleJob5.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory()
                .get("DemoSimpleJob5.simpleJob")
                .start(simpleFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(simpleStep2())
                .end()
                .build();
    }

    @Bean(name = "DemoSimpleJob5.simpleFlow")
    public Flow simpleFlow() {
        return new FlowBuilder<SimpleFlow>("DemoSimpleJob5.simpleFlow")
                .start(simpleStep1())
                .next(simpleStep3())
                .build();
    }

    @Bean(name = "DemoSimpleJob5.simpleStep1")
    Step simpleStep1() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob5.simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob5.simpleStep1 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob5.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob5.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob5.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob5.simpleStep3")
    Step simpleStep3() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob5.simpleStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob5.simpleStep3 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
