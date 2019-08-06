package com.example.demo.batch.job4;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob4 extends CoreJobConfiguration {

    @Bean(name = "DemoSimpleJob4.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory()
                .get("DemoSimpleJob4.simpleJob")
                .start(simpleStep())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(simpleStep2())
                .on(ExitStatus.COMPLETED.getExitCode())
                .to(simpleStep3())
                .end()
                .build();

    }

    @Bean(name = "DemoSimpleJob4.simpleStep1")
    Step simpleStep() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob4.simpleStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob4.simpleStep1 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();

    }

    @Bean(name = "DemoSimpleJob4.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob4.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob4.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");

                    log.info("准备睡眠 5 秒钟以模拟数据处理。");
                    Thread.sleep(5 * 1000);
                    log.info("睡眠了 5 秒钟。");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob4.simpleStep3")
    Step simpleStep3() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob4.simpleStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob4.simpleStep3 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
