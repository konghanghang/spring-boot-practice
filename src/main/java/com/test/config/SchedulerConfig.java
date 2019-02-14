package com.test.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import java.io.IOException;

@Configuration
public class SchedulerConfig {

    @Resource
    private CustomJobFactory customJobFactory;

    /**
     * 配置SchedulerFactoryBean
     *  -- DataSource dataSource 数据源 --基于ram不需要此参数
     * @return
     * @throws IOException
     */
    @Bean
    public SchedulerFactoryBean schedulerFactory() throws IOException {
        //Spring提供SchedulerFactoryBean为Scheduler提供配置信息,并被Spring容器管理其生命周期
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        // factory.setOverwriteExistingJobs(true);
        // 延时启动(秒)
        factory.setStartupDelay(20);
        // 设置quartz的配置文件
        // factory.setQuartzProperties(quartzProperties());
        // 设置自定义Job Factory，用于Spring管理Job bean
        factory.setJobFactory(customJobFactory);
        return factory;
    }

    /**
     * 初始注入scheduler
     * @return
     * @throws SchedulerException
     */
    @Bean
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactory) throws SchedulerException {
        // SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        return schedulerFactory.getScheduler();
    }

}
