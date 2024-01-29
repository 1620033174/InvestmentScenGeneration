package org.spring.finance.controller;

import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class test {

    public void test1(){

    }
    public static void main(String[] args) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = "2023-05-10";
        LocalDate oldDate = LocalDate.parse(dateStr);

        LocalDate today = LocalDate.now();
        System.out.println("Today：" + today);
//        LocalDate oldDate = LocalDate.of(year, month, dayOfMonth);
        System.out.println("OldDate：" + oldDate);

        Period p = Period.between(oldDate, today);
        long l = today.toEpochDay() - oldDate.toEpochDay();
        System.out.println(l);

        System.out.println(today.plusDays(8));

        System.out.printf("目标日期距离今天的时间差：%d 年 %d 个月 %d 天\n", p.getYears(), p.getMonths(), p.getDays());
        //创建集合
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
        //使用stream流进行分组
        Map<Boolean, List<Integer>> preAvg = list.stream().collect(Collectors.groupingBy(x -> x < 2 || x > 4));
//        Map<Boolean, List<Integer>> preAvg = list.stream().collect(Collectors.);
        List<Integer> tr = preAvg.get(true);
        List<Integer> fa = preAvg.get(false);
        System.out.println("true" + tr);
        System.out.println("false" + fa);
        //使用filter过滤
        List<Integer> collect = list.stream().filter(x -> x < 2 || x > 4)
            .collect(Collectors.toList());
        //输出满足过滤条件的集合数据
        System.out.println("true" + collect);
    }
}

