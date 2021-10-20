package com.design.created.prototype;

public class Client {
    public static void main(String[] args) {
        WeeklyLog weeklyLog = new WeeklyLog();
        weeklyLog.setContent("content-----");
        weeklyLog.setDate("data------");
        weeklyLog.setName("name-----");

        WeeklyLog log_new = null;
        log_new = weeklyLog.clone();
        System.out.println(log_new.getContent());
        System.out.println(log_new.getName());
        System.out.println(log_new.getName());

        System.out.println(weeklyLog == log_new);
        System.out.println(weeklyLog.getName() == log_new.getName());
    }
}
