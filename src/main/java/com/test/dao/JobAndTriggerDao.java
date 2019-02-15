package com.test.dao;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface JobAndTriggerDao {

    @Select({"SELECT " +
            "jd.job_name jobName,jd.job_group jobGroup,jd.job_class_name jobClassName,t.trigger_name triggerName, " +
            "t.trigger_group triggerGroup,ct.cron_expression cronExpression,ct.time_zone_id timeZoneId " +
            "FROM " +
            "qrtz_job_details jd " +
            "JOIN qrtz_triggers t " +
            "JOIN qrtz_cron_triggers ct ON jd.JOB_NAME = t.JOB_NAME " +
            "AND t.TRIGGER_NAME = ct.TRIGGER_NAME " +
            "AND t.TRIGGER_GROUP = ct.TRIGGER_GROUP"})
    List<JSONObject> getJobAndTriggerDetails();

}
