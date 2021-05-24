package com.test.deep;

import java.io.IOException;

public class Client {
    public static void main(String[] args) {

        Attachment attachment = new Attachment(); //创建附件对象

        WeeklyLog weeklyLog = new WeeklyLog();
        weeklyLog.setContent("content-----");
        weeklyLog.setDate("data------");
        weeklyLog.setName("name-----");
        weeklyLog.setAttachment(attachment);

        WeeklyLog log_new = null;
        try {
            log_new = weeklyLog.deepClone();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("克隆失败！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("克隆失败！");
        }

        //比较周报
        System.out.println("周报是否相同？ " + (weeklyLog ==  log_new));

        //比较附件
        System.out.println("附件是否相同？ " +  (weeklyLog.getAttachment() == log_new.getAttachment()));

    }
}
