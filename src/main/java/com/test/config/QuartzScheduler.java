package com.test.config;

import com.test.job.TestJob;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Date;

/**
 * quartz
 * https://blog.csdn.net/upxiaofeng/article/details/79415108
 * https://blog.csdn.net/u012907049/article/details/73801122/
 * 解决quartz的job无法注入spring对象的问题：
 * https://blog.csdn.net/mengruobaobao/article/details/79106343
 */
@Configuration
public class QuartzScheduler {

    // 任务调度
    @Resource
    private Scheduler scheduler;


    /**
     * 开始执行所有任务
     *
     * @throws SchedulerException
     */
    public void startJob() throws SchedulerException {
        startTestJob();
        scheduler.start();
    }

    /**
     * 获取Job信息
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }

    private void startTestJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(TestJob.class)
                .withIdentity("job2", "group2")
                .build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0/30 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity("job2", "group2")
                .withSchedule(cronScheduleBuilder)
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

}
