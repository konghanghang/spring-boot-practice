package com.test.shallow;

import com.test.Attachment;

public class Client {
    public static void main(String[] args) {

        Attachment attachment = new Attachment(); //创建附件对象

        WeeklyLog weeklyLog = new WeeklyLog();
        weeklyLog.setContent("content-----");
        weeklyLog.setDate("data------");
        weeklyLog.setName("name-----");
        weeklyLog.setAttachment(attachment);

        WeeklyLog log_new = null;
        log_new = weeklyLog.clone();

        //比较周报
        System.out.println("周报是否相同？ " + (weeklyLog ==  log_new));

        //比较附件
        System.out.println("附件是否相同？ " +  (weeklyLog.getAttachment() == log_new.getAttachment()));

    }
}
