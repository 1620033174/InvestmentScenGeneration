package org.spring.finance.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.hz.PictureList;
import org.spring.finance.entity.hz.QuantileList;
import org.spring.finance.entity.hz.StockMaxOrMin;
import org.spring.finance.entity.po.Stock;
import org.spring.finance.service.StockService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.CodeLists;
import org.spring.finance.utils.result.Result;
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
@RequestMapping("/hz/detail")
@CrossOrigin(origins = "*")
public class FactorDetailController {

    @Autowired
    private StockService stockService;

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

        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        //得到英文名称
        String name_us = getUsFactorName(factorname);
        String name_us_0 = name_us+"+0";

        queryWrapper.eq("factor_name",name_us);
        queryWrapper.eq("date","2020/9/30");
        queryWrapper.in("code",codelists);
        queryWrapper.orderByDesc("factor_value+0");
        queryWrapper.last("limit 20");
        List<Stock> maxList = stockService.list(queryWrapper);
        List<StockMaxOrMin> stockMaxOrMinList = new ArrayList<StockMaxOrMin>();

        for (int i = 0; i < maxList.size(); i++) {
            StockMaxOrMin stockMaxOrMin = new StockMaxOrMin();
            stockMaxOrMin.setCode(maxList.get(i).getCode());
            stockMaxOrMin.setStock_name(maxList.get(i).getStockName());
            stockMaxOrMin.setValue(maxList.get(i).getFactorValue());
            stockMaxOrMinList.add(stockMaxOrMin);
        }
        return Result.success((stockMaxOrMinList));
    }
    // 得到因子值最小的20只股票
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

        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        //得到英文名称
        String name_us = getUsFactorName(factorname);
        String name_us_0 = name_us+"+0";

        queryWrapper.eq("factor_name",name_us);
        queryWrapper.eq("date","2020/9/30");
        queryWrapper.in("code",codelists);
        queryWrapper.orderByAsc("factor_value+0");
        queryWrapper.last("limit 20");
        List<Stock> maxList = stockService.list(queryWrapper);
        List<StockMaxOrMin> stockMaxOrMinList = new ArrayList<StockMaxOrMin>();

        for (int i = 0; i < maxList.size(); i++) {
            StockMaxOrMin stockMaxOrMin = new StockMaxOrMin();
            stockMaxOrMin.setCode(maxList.get(i).getCode());
            stockMaxOrMin.setStock_name(maxList.get(i).getStockName());
            stockMaxOrMin.setValue(maxList.get(i).getFactorValue());
            stockMaxOrMinList.add(stockMaxOrMin);
        }
        return Result.success((stockMaxOrMinList));
    }
    //得到分位数图
    @GetMapping("getQuantilePic")
    public Result<QuantileList> getQuantilePic(@Param("factorname")String factorname,@Param("stockYear")  String stockYear, @Param("stockPool") String stockPool){
        List<Stock> resultList = new ArrayList<>();
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",stockYear+"/9/30");
        String name_us = getUsFactorName(factorname);
        queryWrapper.eq("factor_name",name_us);
        resultList = stockService.list(queryWrapper);

        //把resultList中的日期格式转换重存到resultList中
        for (Stock stock : resultList ) {
            String date = stock.getPubdate();
            String date_new = date.replace("/","-");
            stock.setPubdate(date_new);
        }

        //获得第一个数据的日期和最后一个数据的日期。
        String date_start = resultList.get(0).getPubdate();
        String date_end = resultList.get(resultList.size ()-1).getPubdate();
        //自定义结束日期
        date_end = stockYear+"-10-31";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date_start_lo = LocalDate.parse(date_start, fmt);
        LocalDate date_end_lo = LocalDate.parse(date_end, fmt);

        long length_date = date_end_lo.toEpochDay() - date_start_lo.toEpochDay();
        int step = 7;
        long day = length_date / step ;

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
            List<Stock> preStockData = resultList.stream().filter(
                    Stock -> LocalDate.parse(
                            Stock.getPubdate()).isBefore(finalDate_end_lo)).collect(Collectors.toList());
            List<Stock> equalStockData = resultList.stream().filter(
                    Stock -> LocalDate.parse(
                            Stock.getPubdate()).isEqual(finalDate_end_lo) ).collect(Collectors.toList());
            List<Stock> afterStockData = preStockData.stream().filter(
                    Stock -> LocalDate.parse(
                            Stock.getPubdate()).isAfter(finalDate_start_lo) ).collect(Collectors.toList());
            afterStockData.addAll(equalStockData);

            if(afterStockData.size()!=0) {
                List<String> factor_list = afterStockData.stream().map(Stock::getFactorValue).collect(Collectors.toList());
                factor_list.remove("");
                factor_list.removeIf(Objects::isNull);
                System.out.println(factor_list);
                List<Double> factor_list_double = factor_list.stream().map(Double::valueOf).collect(Collectors.toList());
                Collections.sort(factor_list_double);

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
        QuantileList quantileList = new QuantileList();
        quantileList.setMin_list(min_list);
        quantileList.setMax_list(max_list);
        quantileList.setTwo_list(two_list);
        quantileList.setThree_list(three_list);
        quantileList.setFour_list(four_list);
        quantileList.setDate_list(date_list);
        return Result.success(quantileList);
    }
    //得到方差、标准差、均值
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
        List<Stock> resultList = new ArrayList<>();
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
//        System.out.println(stockYear + "/9/30");
        queryWrapper.eq("date", stockYear + "/9/30");
        String name_us = getUsFactorName(factorname);
        queryWrapper.eq("factor_name",name_us);
        queryWrapper.in("code", codelists);
        resultList = stockService.list(queryWrapper);

        //把resultList中的日期格式转换重存到resultList中
        for (Stock stock : resultList ) {
            String date = stock.getPubdate();
            String date_new = date.replace("/","-");
            stock.setPubdate(date_new);
        }

        //获得第一个数据的日期和最后一个数据的日期。
        String date_start = resultList.get(0).getPubdate();
        String date_end = resultList.get(resultList.size ()-1).getPubdate();
        //自定义结束日期
        date_end = stockYear+"-10-31";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date_start_lo = LocalDate.parse(date_start, fmt);
        LocalDate date_end_lo = LocalDate.parse(date_end, fmt);

        long length_date = date_end_lo.toEpochDay() - date_start_lo.toEpochDay();
        int step = 5;
        long day = length_date / step ;
//        System.out.println(day);

        List<Double> variance_list = new ArrayList<>();
        List<Double> standard_deviation_list = new ArrayList<>();
        List<Double> mean_list = new ArrayList<>();
        List<String> date_list = new ArrayList<>();

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

            List<Stock> preStockData = resultList.stream().filter(
                    Stock -> LocalDate.parse(
                            Stock.getPubdate()).isBefore(finalDate_end_lo) ).collect(Collectors.toList());
            List<Stock> equalStockData = resultList.stream().filter(
                    Stock -> LocalDate.parse(
                            Stock.getPubdate()).isEqual(finalDate_end_lo) ).collect(Collectors.toList());
            List<Stock> afterStockData = preStockData.stream().filter(
                    Stock -> LocalDate.parse(
                            Stock.getPubdate()).isAfter(finalDate_start_lo) ).collect(Collectors.toList());
            afterStockData.addAll(equalStockData);

            //遍历获得均值
            if(afterStockData.size()!=0){
                List<String> factor_list = afterStockData.stream().map(Stock::getFactorValue).collect(Collectors.toList());
//                System.out.println(factor_list);
                factor_list.remove("");
                factor_list.removeIf(Objects::isNull);
                double variance = get_variance(factor_list);
                double standard_deviation = get_standard_deviation(factor_list);
                double mean = get_mean(factor_list);
                DecimalFormat df = new DecimalFormat("#.00");

//                System.out.println("variance："+variance);
//                System.out.println("standard_deviation："+standard_deviation);
//                System.out.println("mean："+mean);
//                System.out.println(factor_list);
//                System.out.println();
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
            date_start_lo = date_end_lo;
        }
//        System.out.println("方差:"+variance_list);
//        System.out.println("标准差:"+standard_deviation_list);
//        System.out.println("均值:"+mean_list);
//        System.out.println("日期:"+date_list);
//        System.out.println(date_start+date_end);
        PictureList pictureList = new PictureList();
        pictureList.setVariance_list(variance_list);
        pictureList.setStandard_deviation_list(standard_deviation_list);
        pictureList.setMean_list(mean_list);
        pictureList.setDate_list(date_list);
        return Result.success(pictureList);
    }

    public String getUsFactorName(String factorName){
        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorName);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();
        return name_us;
    }
}
