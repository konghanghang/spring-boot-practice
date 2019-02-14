package com.test.config;

import org.quartz.SchedulerException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Resource;

@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private QuartzScheduler quartzScheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            quartzScheduler.startJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
