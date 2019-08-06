package com.example.demo.batch.job3;

import com.example.demo.batch.job.CoreJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 读取、处理、写入操作
 *
 * @author zhaozisheng
 */
@Slf4j
@Configuration
public class DemoSimpleJob3 extends CoreJobConfiguration {

    @Bean(name = "DemoSimpleJob3.simpleJob")
    public Job simpleJob() {
        return getJobBuilderFactory()
                .get("DemoSimpleJob3.simpleJob")
                .start(simpleStep())
                .next(simpleStep2())
                .next(simpleStep3())
                .build();
    }

    @Bean(name = "DemoSimpleJob3.simpleStep")
    Step simpleStep() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob3.simpleStep")
                .<String, String>chunk(2)
                .faultTolerant()
                .reader(reader())
                .processor(upperCaseProcessor())
                .writer(writer())
                .build();

    }

    @Bean(name = "DemoSimpleJob3.reader")
    public ItemReader<String> reader() {
        return new ListItemReader<>(Arrays.asList("first", "second", "error", "third", "error2", "four"));
    }

    @Bean(name = "DemoSimpleJob3.processor")
    public ItemProcessor<String, String> upperCaseProcessor() {
        return text -> {
            if (text == null) {
                return null;
            } else if (text.contains("error")) {
                log.info("跳过错误数据item: {}", text);

                return null;
            } else {
                return text.toUpperCase();
            }
        };
    }

    @Bean(name = "DemoSimpleJob3.writer")
    public ItemWriter<String> writer() {
        return items -> {
            for (String item : items) {
                log.info("写入数据到外部: " + item);
            }
        };
    }

    @Bean(name = "DemoSimpleJob3.simpleStep2")
    Step simpleStep2() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob3.simpleStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob3.simpleStep2 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean(name = "DemoSimpleJob3.simpleStep3")
    Step simpleStep3() {
        return getStepBuilderFactory()
                .get("DemoSimpleJob3.simpleStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info("DemoSimpleJob3.simpleStep3 >>>> ~~~~~~~~~~~~~~~~~~~~~ start ~~~~~~~~~~~~~~~~~~~~~");


                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
