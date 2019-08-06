package com.example.demo.batch.job7;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
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
public class DemoSimpleJob7 extends CoreJobConfiguration {

    /**
     * 调用其他job
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "DemoSimpleJob7.simpleJob")
    public Job simpleJob() throws Exception {
        return getJobBuilderFactory()
                .get("DemoSimpleJob7.simpleJob")
                .start(simpleStep1())
                .next(childJobStep1())
                .next(simpleStep2())
                .build();
    }

    @Bean(name = "DemoSimpleJob7.simpleStep1")
    Step simpleStep1() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob7.simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob7.simpleStep1 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob7.childJobStep1")
    @DependsOn("DemoSimpleJob1.simpleJob")
    Step childJobStep1() throws Exception {
        return jobStepBuilder("DemoSimpleJob7.childJobStep1","DemoSimpleJob1.simpleJob").build();

    }

    @Bean(name = "DemoSimpleJob7.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob7.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob7.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
