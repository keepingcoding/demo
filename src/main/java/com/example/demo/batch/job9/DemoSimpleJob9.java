package com.example.demo.batch.job9;

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
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob9 extends CoreJobConfiguration {

    @Bean(name = "DemoSimpleJob9.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory()
                .get("DemoSimpleJob9.simpleJob")
                .start(simpleFlow())
                .next(splitFlow())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(simpleStep4())
                .end()
                .build();
    }

    @Bean(name = "DemoSimpleJob9.splitFlow")
    public Flow splitFlow() {
        return new FlowBuilder<SimpleFlow>("DemoSimpleJob9.splitFlow")
                .split(new SimpleAsyncTaskExecutor("DemoSimpleJob9.splitExecutor"))
                .add(simpleFlow2(), simpleFlow3(), simpleFlow4())
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleFlow")
    public Flow simpleFlow() {
        return new FlowBuilder<SimpleFlow>("DemoSimpleJob9.simpleFlow")
                .start(simpleStep1())
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleFlow2")
    public Flow simpleFlow2() {
        return new FlowBuilder<SimpleFlow>("DemoSimpleJob9.simpleFlow2")
                .start(simpleStep2())
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleFlow3")
    public Flow simpleFlow3() {
        return new FlowBuilder<SimpleFlow>("DemoSimpleJob9.simpleFlow3")
                .start(simpleStep3())
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleFlow4")
    public Flow simpleFlow4() {
        return new FlowBuilder<SimpleFlow>("DemoSimpleJob9.simpleFlow4")
                .start(
                        getStepBuilderFactory()
                        .get("DemoSimpleJob9.simpleFlow4.simpleStep")
                        .tasklet((contribution, chunkContext) -> {
                            log.info("DemoSimpleJob9.simpleFlow4.simpleStep >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                            Thread.sleep(2 * 1000);

                            log.info("DemoSimpleJob9.simpleFlow4.simpleStep >>>> ~~~~~~~~~~~~~~~~~~~~~ end ~~~~~~~~~~~~~~~~~~~~~");

                            return RepeatStatus.FINISHED;
                        })
                        .build()
                )
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleStep1")
    Step simpleStep1() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob9.simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob9.simpleStep1 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob9.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob9.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    Thread.sleep(5 * 1000);

                    log.info("DemoSimpleJob9.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ end ~~~~~~~~~~~~~~~~~~~~~");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleStep3")
    Step simpleStep3() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob9.simpleStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob9.simpleStep3 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    Thread.sleep(10 * 1000);

                    log.info("DemoSimpleJob9.simpleStep3 >>>> ~~~~~~~~~~~~~~~~~~~~~ end ~~~~~~~~~~~~~~~~~~~~~");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob9.simpleStep4")
    Step simpleStep4() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob9.simpleStep4")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob9.simpleStep4 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
