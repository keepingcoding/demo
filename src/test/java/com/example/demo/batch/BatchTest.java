package com.example.demo.batch;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author zhaozisheng
 * @version 1.0
 * @date 2019-05-16 17:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BatchTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRegistry jobRegistry;

    /**
     * Batch测试
     *
     * @throws Exception
     */
    @Test
    public void contextLoads3() throws Exception {
        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        JobRegistry jobRegistry = applicationContext.getBean(JobRegistry.class);

        //JobOperator jobOperator = applicationContext.getBean(JobOperator.class);
        //Long start = jobOperator.start("DemoSimpleJob1.simpleJob", "time=" + System.currentTimeMillis());
        //System.err.println(start);

        Job job = jobRegistry.getJob("DemoSimpleJob9.simpleJob");
        JobParameters jobParameters = new JobParametersBuilder().addDate("time", new Date()).toJobParameters();

        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        System.err.println(jobExecution);
    }
}
