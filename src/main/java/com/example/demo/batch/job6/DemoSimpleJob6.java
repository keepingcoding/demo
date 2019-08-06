package com.example.demo.batch.job6;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob6 extends CoreJobConfiguration {

    /**
     * 根据不同的决策执行不同的逻辑
     *
     * @return
     */
    @Bean(name = "DemoSimpleJob6.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory()
                .get("DemoSimpleJob6.simpleJob")
                .start(simpleStep1())
                .next(simpleDecider())
                .from(simpleDecider()).on("EVEN").to(simpleStep2())
                .from(simpleDecider()).on("ODD").to(simpleStep3())
                .from(simpleStep3()).on("*").to(simpleDecider())
                .end()
                .build();
    }

    @Bean(name = "DemoSimpleJob6.simpleStep1")
    Step simpleStep1() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob6.simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob6.simpleStep1 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob6.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob6.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob6.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob6.simpleStep3")
    Step simpleStep3() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob6.simpleStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob6.simpleStep3 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob6.simpleDecider")
    JobExecutionDecider simpleDecider() {
        return new JobExecutionDecider() {
            private int count = 0;

            @Override
            public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
                System.err.println(jobExecution);
                System.err.println(stepExecution);
                count++;
                if (count % 2 == 0) {
                    log.info("当前决策为双数 EVEN");
                    return new FlowExecutionStatus("EVEN");
                } else {
                    log.info("当前决策为单数 ODD");
                    return new FlowExecutionStatus("ODD");
                }
            }
        };
    }

}
