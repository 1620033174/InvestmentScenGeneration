package org.spring.finance.controller.hz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.spring.finance.entity.hz.*;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.service.hz.StockDataService;
import org.spring.finance.utils.result.CodeLists;
import org.spring.finance.utils.result.FactorToList;
import org.spring.finance.utils.result.Result;
import org.spring.finance.utils.result.StockData_Max_Min_GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.spring.finance.utils.result.CodeLists.*;

@RestController
@RequestMapping("/gg/detail")
@CrossOrigin(origins = "*")
public class HzFactorDetailController {

    @Autowired
    private StockDataService stockDataService;

    @Autowired
    private FactorHZService factorHZService;
    // 得到因子值最大的20只股票

    @GetMapping("getMax")
    public Result<List<StockMaxOrMin>> getMax(@Param("factorname")String factorname, @Param("stockPool") String stockPool){
        List<String> codelists= new ArrayList<>();
        if(stockPool.equals("沪深300")){
            codelists = CodeLists.get_CODES_HS300();
        } else if (stockPool.equals("中证100")) {
            codelists = CodeLists.get_CODES_ZZ100();
        }else if(stockPool.equals("中证500")){
            codelists = CodeLists.get_CODES_ZZ500();
        }

        QueryWrapper<StockData> queryWrapper = new QueryWrapper<>();

        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorname);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();
        String name_us_0 = name_us+"+0";

        queryWrapper.select("code", "stock_name", name_us);
        queryWrapper.in("code",codelists);

        queryWrapper.orderByDesc(name_us_0);
        //使用last方法拼接sql语句
        queryWrapper.last("limit 20");
        List<StockData> maxList = stockDataService.list(queryWrapper);
        List<StockMaxOrMin> stockMaxOrMinList = new ArrayList<StockMaxOrMin>();

        for (int i = 0; i < maxList.size(); i++) {
            StockMaxOrMin stockMaxOrMin = new StockMaxOrMin();
            stockMaxOrMin.setCode(maxList.get(i).getCode());
            stockMaxOrMin.setStock_name(maxList.get(i).getStockName());
            String value = StockData_Max_Min_GetMethod.get_Method_Value(name_us,i,maxList);
            stockMaxOrMin.setValue(value);
            stockMaxOrMinList.add(stockMaxOrMin);
        }
        return Result.success((stockMaxOrMinList));
    }



    @GetMapping("getMin")
    public Result<List<StockMaxOrMin>> getMin(@Param("factorname")String factorname, @Param("stockPool") String stockPool){
        List<String> codelists= new ArrayList<>();
        if(stockPool.equals("沪深300")){
            codelists = CodeLists.get_CODES_HS300();
        } else if (stockPool.equals("中证100")) {
            codelists = CodeLists.get_CODES_ZZ100();
        }else if(stockPool.equals("中证500")){
            codelists = CodeLists.get_CODES_ZZ500();
        }

        QueryWrapper<StockData> queryWrapper = new QueryWrapper<>();

        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorname);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();
        String name_us_0 = name_us+"+0";
        queryWrapper.select("code", "stock_name", name_us);
        queryWrapper.in("code",codelists);
//        List<Map<String, Object>> mapList = SqlRunner.db().selectList("select * from test_example limit 1,10");
        queryWrapper.orderByAsc(name_us_0);
        //使用last方法拼接sql语句
        queryWrapper.last("limit 20");
        List<StockData> minList = stockDataService.list(queryWrapper);


        List<StockMaxOrMin> stockMaxOrMinList = new ArrayList<StockMaxOrMin>();

        for (int i = 0; i < minList.size(); i++) {
            StockMaxOrMin stockMaxOrMin = new StockMaxOrMin();
            stockMaxOrMin.setCode(minList.get(i).getCode());
            stockMaxOrMin.setStock_name(minList.get(i).getStockName());
            String value = StockData_Max_Min_GetMethod.get_Method_Value(name_us,i,minList);
            stockMaxOrMin.setValue(value);
            stockMaxOrMinList.add(stockMaxOrMin);
        }
        return Result.success((stockMaxOrMinList));
    }
    //得到几分位数图
    @GetMapping("getQuantilePic")
    public Result<QuantileList> getQuantilePic(@Param("factorname")String factorname,@Param("stockYear")  String stockYear, @Param("stockPool") String stockPool){
        //数据不好看，取消中证限制。
//        List<String> codelists= new ArrayList<>();
//        if(stockPool.equals("沪深300")){
//            codelists = CodeLists.get_CODES_HS300();
//        } else if (stockPool.equals("中证100")) {
//            codelists = CodeLists.get_CODES_ZZ100();
//        }else if(stockPool.equals("中证500")){
//            codelists = CodeLists.get_CODES_ZZ500();
//        }

        List<StockData> resultList = new ArrayList<>();
        QueryWrapper<StockData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stadate",stockYear+"-09-30");
//        queryWrapper.in("code",codelists);
        resultList = stockDataService.list(queryWrapper);

        //获得第一个数据的日期和最后一个数据的日期。
        String date_start = resultList.get(0).getPubdate();
        String date_end = resultList.get(resultList.size ()-1).getPubdate();
        //自定义结束日期
        date_end = stockYear+"-10-31";
        System.out.println("第一个数据日期"+date_start);
        System.out.println("最后一个数据日期"+date_end);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date_start_lo = LocalDate.parse(date_start, fmt);
        LocalDate date_end_lo = LocalDate.parse(date_end, fmt);

        long length_date = date_end_lo.toEpochDay() - date_start_lo.toEpochDay();
        int step = 7;
        long day = length_date / step ;
        //System.out.println(day);

        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorname);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();

        List<Double> min_list = new ArrayList<>();
        List<Double> max_list = new ArrayList<>();
        List<Double> two_list = new ArrayList<>();
        List<Double> three_list = new ArrayList<>();
        List<Double> four_list = new ArrayList<>();
        List<String> date_list = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#.000");
        //遍历，以日期差，
        for (int i = 0; i < step; i++) {
            //按时间段划分
            date_end_lo = date_start_lo.plusDays(day);
            if(i==0){
                date_start_lo = date_start_lo.plusDays(-1);
            }
            date_list.add(date_start_lo.toString());
            System.out.println("开始日期"+date_start_lo);
            System.out.println("结束日期"+date_end_lo);
            LocalDate finalDate_end_lo = date_end_lo;
            LocalDate finalDate_start_lo = date_start_lo;

            List<StockData> preStockData = resultList.stream().filter(
                    StockData -> LocalDate.parse(
                            StockData.getPubdate()).isBefore(finalDate_end_lo) ).collect(Collectors.toList());
            List<StockData> equalStockData = resultList.stream().filter(
                    StockData -> LocalDate.parse(
                            StockData.getPubdate()).isEqual(finalDate_end_lo) ).collect(Collectors.toList());
//            System.out.println(preStockData);
            List<StockData> afterStockData = preStockData.stream().filter(
                    StockData -> LocalDate.parse(
                            StockData.getPubdate()).isAfter(finalDate_start_lo) ).collect(Collectors.toList());
            afterStockData.addAll(equalStockData);
            //System.out.println(afterStockData);

            if(afterStockData.size()!=0) {

                List<String> factor_list = FactorToList.factor_list(name_us,afterStockData);
                System.out.println("123");

                factor_list.remove("");
                factor_list.removeIf(Objects::isNull);
                System.out.println(factor_list);
                List<Double> factor_list_double = factor_list.stream().map(Double::valueOf).collect(Collectors.toList());
                //System.out.println("蛤");
                Collections.sort(factor_list_double);
                //System.out.println(factor_list_double);

                int size = factor_list_double.size();

                min_list.add(Double.valueOf(df.format(factor_list_double.get(0))));
                max_list.add(Double.valueOf(df.format(factor_list_double.get(size-1))));

                int two = size / 2;
                int three = size / 3;
                int four = size / 4;
                two_list.add(Double.valueOf(df.format(factor_list_double.get(two))));
                three_list.add(Double.valueOf(df.format(factor_list_double.get(three))));
                four_list.add(Double.valueOf(df.format(factor_list_double.get(four))));
            }
            date_start_lo = date_end_lo;
        }
//        System.out.println(two_list);
//        System.out.println(three_list);
//        System.out.println(four_list);
//        System.out.println(min_list);
//        System.out.println(max_list);
//        System.out.println(date_list);
        QuantileList quantileList = new QuantileList();
        quantileList.setMin_list(min_list);
        quantileList.setMax_list(max_list);
        quantileList.setTwo_list(two_list);
        quantileList.setThree_list(three_list);
        quantileList.setFour_list(four_list);
        quantileList.setDate_list(date_list);
        return Result.success(quantileList);
    }

    //先看看日期，日期时间段分为五段
    //1、按时间区间划分，先划分五段，分别计算这五段的方差，
    //第一个数据的日期，
    //2、时间点取每一段的第一个时间的

    //使用list嵌套：最后要返回的数据：三个[200, 120, 190, 244, 190, 270, 340]+一个日期的['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    @GetMapping("getVariancePic")
    public Result<PictureList> getVariancePic(@Param("stockPool") String stockPool, @Param("stockYear")  String stockYear,
                                          @Param("Theme") String Theme, @Param("factorname") String factorname) {
        //得到股票池、周期、主题
        String factor_theme = get_factor_theme(Theme);
        List<String> codelists = new ArrayList<>();
        if (stockPool.equals("沪深300")) {
            codelists = CodeLists.get_CODES_HS300();
        } else if (stockPool.equals("中证100")) {
            codelists = CodeLists.get_CODES_ZZ100();
        } else if (stockPool.equals("中证500")) {
            codelists = CodeLists.get_CODES_ZZ500();
        }
        List<StockData> resultList = new ArrayList<>();
        QueryWrapper<StockData> queryWrapper = new QueryWrapper<>();
        System.out.println(stockYear + "-9-30");
        queryWrapper.eq("stadate", stockYear + "-09-30");
        queryWrapper.in("code", codelists);
        resultList = stockDataService.list(queryWrapper);
        //System.out.println(resultList);

        //获得第一个数据的日期和最后一个数据的日期。
        String date_start = resultList.get(0).getPubdate();
        String date_end = resultList.get(resultList.size ()-1).getPubdate();
        //自定义结束日期
        date_end = stockYear+"-10-31";
        System.out.println("第一个数据日期"+date_start);
        System.out.println("最后一个数据日期"+date_end);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date_start_lo = LocalDate.parse(date_start, fmt);
        LocalDate date_end_lo = LocalDate.parse(date_end, fmt);

        long length_date = date_end_lo.toEpochDay() - date_start_lo.toEpochDay();
        int step = 5;
        long day = length_date / step ;
        System.out.println(day);

        List<Double> variance_list = new ArrayList<>();
        List<Double> standard_deviation_list = new ArrayList<>();
        List<Double> mean_list = new ArrayList<>();
        List<String> date_list = new ArrayList<>();

        //得到英文名称
        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorname);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();

        //遍历，以日期差，
        for (int i = 0; i < step; i++) {
            //按时间段划分
            date_end_lo = date_start_lo.plusDays(day);
            if(i==0){
                date_start_lo = date_start_lo.plusDays(-1);
            }
            date_list.add(date_start_lo.toString());
            System.out.println("开始日期"+date_start_lo);
            System.out.println("结束日期"+date_end_lo);
            LocalDate finalDate_end_lo = date_end_lo;
            LocalDate finalDate_start_lo = date_start_lo;

            List<StockData> preStockData = resultList.stream().filter(
                    StockData -> LocalDate.parse(
                            StockData.getPubdate()).isBefore(finalDate_end_lo) ).collect(Collectors.toList());
            List<StockData> equalStockData = resultList.stream().filter(
                    StockData -> LocalDate.parse(
                            StockData.getPubdate()).isEqual(finalDate_end_lo) ).collect(Collectors.toList());
//            System.out.println(preStockData);
            List<StockData> afterStockData = preStockData.stream().filter(
                    StockData -> LocalDate.parse(
                            StockData.getPubdate()).isAfter(finalDate_start_lo) ).collect(Collectors.toList());
            afterStockData.addAll(equalStockData);
            System.out.println(afterStockData);
            System.out.println("你是");

//            System.out.println(afterStockData);

            //遍历获得均值

            if(afterStockData.size()!=0){
                List<String> factor_list = FactorToList.factor_list(name_us,afterStockData);
                System.out.println(factor_list);
                factor_list.remove("");
                factor_list.removeIf(Objects::isNull);
                double variance = get_variance(factor_list);
                double standard_deviation = get_standard_deviation(factor_list);
                double mean = get_mean(factor_list);
                DecimalFormat df = new DecimalFormat("#.00");

                //double v = Double.parseDouble(df.format(variance));

                System.out.println("variance："+variance);
                System.out.println("standard_deviation："+standard_deviation);
                System.out.println("mean："+mean);
                System.out.println(factor_list);
                System.out.println();
                if(Double.isNaN(variance)){
                    variance_list.add(0.0);
                }else{
                    variance_list.add(Double.valueOf(df.format(variance)));
                }
                if(Double.isNaN(standard_deviation)){
                    standard_deviation_list.add(0.0);
                }else{
                    standard_deviation_list.add(Double.valueOf(df.format(standard_deviation)));
                }
                if(Double.isNaN(mean)){
                    mean_list.add(0.0);
                }else{
                    mean_list.add(Double.valueOf(df.format(mean)));
                }
            }else{
                variance_list.add(0.0);
                standard_deviation_list.add(0.0);
                mean_list.add(0.0);
            }
//            if(resultList.)
            //afterStockData;

            date_start_lo = date_end_lo;
        }
        System.out.println("方差:"+variance_list);
        System.out.println("标准差:"+standard_deviation_list);
        System.out.println("均值:"+mean_list);
        System.out.println("日期:"+date_list);
        System.out.println(date_start+date_end);
//        List<List<>>
        PictureList pictureList = new PictureList();
        pictureList.setVariance_list(variance_list);
        pictureList.setStandard_deviation_list(standard_deviation_list);
        pictureList.setMean_list(mean_list);
        pictureList.setDate_list(date_list);
        return Result.success(pictureList);
    }

}
