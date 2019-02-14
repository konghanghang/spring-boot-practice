package com.test.job;

import com.test.serivce.ITestService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;

public class TestJob implements Job {

    @Resource
    ITestService testService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        testService.time();
    }
}
