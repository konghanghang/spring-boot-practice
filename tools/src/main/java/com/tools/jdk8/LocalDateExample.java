package com.tools.jdk8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * localDateTime测试类
 * java8 api: https://blog.csdn.net/chenxun_2010/article/details/72539981
 * @author yslao@outlook.com
 */
public class LocalDateExample {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取localDateTime
     */
    public void getLocalDate(){
        // 1
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        LocalDateTime.of(localDate, localTime);

        // 2
        LocalDateTime.now();
        // 3
        LocalDateTime.of(2019, 9, 29, 12, 10);
    }

    /**
     * 通过时间戳获取localDateTime对象
     */
    public void getLocalDateFromStamp(){
        long now = System.currentTimeMillis();

        // 1
        LocalDateTime localDateTime = Instant.ofEpochMilli(now)
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(localDateTime.format(formatter));

        // 2
        Instant instant = Instant.ofEpochMilli(now);
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime.ofInstant(instant, zoneId);

        // 3
        new Date(now).toInstant();
    }

    /**
     * 通过时间戳获取localDateTime
     */
    public void getStampFromLocalDate(){
        LocalDateTime localDateTime = LocalDateTime.now();

        // 1
        localDateTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // 2
        localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 用于计算两个时间（秒，纳秒）间隔。
     */
    public void duration(){
        // 2019-07-31 18:03
        LocalDateTime localDateTime = LocalDateTime.now();
        long milli1 = localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        LocalDateTime begin = localDateTime.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        long milli2 = begin.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        // 64982191
        System.out.println(milli1 - milli2);
        Duration duration = Duration.between(begin, localDateTime);
        // 64982
        System.out.println(duration.getSeconds());
        // 不知道计算的是什么 191000000
        System.out.println(duration.getNano());

        // 0
        System.out.println(duration.toDays());
        // 18
        System.out.println(duration.toHours());
        // 64982191000000
        System.out.println(duration.toNanos());

        long between = ChronoUnit.SECONDS.between(begin, localDateTime);
        System.out.println(between);
    }

    /**
     * 用于计算两个日期（年月日）间隔。
     */
    public void period(){
        LocalDate initialDate = LocalDate.parse("2019-08-01");
        LocalDate finalDate = initialDate.plus(Period.ofDays(5));
        System.out.println(finalDate);
        int five = Period.between(finalDate, initialDate).getDays();
        System.out.println(five);

        long between = ChronoUnit.DAYS.between(finalDate , initialDate);
        System.out.println(between);
    }

    /**
     * 比较两个【年月日】日期是否相等
     */
    public void compareDateTime(){
        LocalDate localDate = LocalDate.of(2019, 7, 30);
        LocalDate now = LocalDate.now();
        System.out.println(localDate.equals(now));
    }

    /**
     * 比较两个【月日】日期是否相等
     */
    public void compareMonthDay(){
        MonthDay monthDay = MonthDay.of(7, 30);
        MonthDay nowMonthDay = MonthDay.from(LocalDate.now());
        System.out.println(monthDay.equals(nowMonthDay));
    }

    /**
     * 日期加减
     */
    public void datePlusOrMinus(){
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        LocalDate plus = localDate.plus(1, ChronoUnit.WEEKS);
        System.out.println(plus);
        LocalDate minus = localDate.minus(1, ChronoUnit.YEARS);
        System.out.println(minus);
        LocalDate minusDays = localDate.minusDays(2);
        System.out.println(minusDays);
    }

    /**
     * 判断一个日期是否在另一个日期 前 或者 后
     */
    public void dateAfterOrBefore(){
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime minusDays = localDateTime.minusDays(1);
        // false
        System.out.println(localDateTime.isBefore(minusDays));
        // true
        System.out.println(localDateTime.isAfter(minusDays));
    }

    /**
     * 年月类api
     */
    public void yearMonthTest(){
        YearMonth yearMonth = YearMonth.now();
        // 31
        System.out.println(yearMonth.lengthOfMonth());
        // 365
        System.out.println(yearMonth.lengthOfYear());
    }

    /**
     * 判断是否是闰年
     */
    public void leapYear(){
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.isLeapYear());
    }

    /**
     * 判断是否是生日
     */
    public void birthdayCheck() {
        LocalDate date1 = LocalDate.now();

        LocalDate date2 = LocalDate.of(2018,2,6);
        MonthDay birthday = MonthDay.of(date2.getMonth(),date2.getDayOfMonth());
        MonthDay currentMonthDay = MonthDay.from(date1);

        if(currentMonthDay.equals(birthday)){
            System.out.println("是你的生日");
        }else{
            System.out.println("你的生日还没有到");
        }
    }

}
