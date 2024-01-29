package org.spring.finance.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.spring.finance.entity.hz.*;
import org.spring.finance.entity.po.Stock;
import org.spring.finance.service.StockService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.service.hz.StockDataService;
import org.spring.finance.utils.factor.factorNameToUs;
import org.spring.finance.utils.result.CodeLists;
import org.spring.finance.utils.result.FactorToList;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.spring.finance.utils.result.CodeLists.*;
import static org.spring.finance.utils.result.Mutil.correlation;

//因子分析管理
@RestController
@RequestMapping("/hz/results")
//@CrossOrigin(origins = "http://localhost:9528")
@CrossOrigin(origins = "*")
public class FactorAnalysisController {
    @Autowired
    private StockDataService stockDataService;
    @Autowired
    private FactorHZService factorHZService;
    @Autowired
    private StockService stockService;

    //得到所有的因子中文名称
    @GetMapping("getAllNames")
    public Result<List<String>> getAllResults() {
        List<FactorHZ> resultList = new ArrayList<>();
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        resultList = factorHZService.list(queryWrapper);

        List<String> nametList = resultList.stream().map(FactorHZ::getName).collect(Collectors.toList());
//        System.out.println(nametList);
        return Result.success((nametList));
    }

    //得到选择因子中的 因子级联信息
    @GetMapping("getFactorsInfo")
    public Result<List<FactorInfo>> getFactorsInfo() {
        List<FactorHZ> resultList = new ArrayList<>();
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.select();
        resultList = factorHZService.list(queryWrapper);

        List<String> types = new ArrayList<String>();
        //最外层
        List<FactorInfo> factorInfos =  new ArrayList<FactorInfo>();

        //获取全部类型
        for (int i = 0; i < resultList.size(); i++) {
            List<FactorChildren> factorChildrenList = new ArrayList<FactorChildren>();
            FactorInfo factorInfo = new FactorInfo();
            FactorChildren factorChildren = new FactorChildren();
            factorChildren.setId(resultList.get(i).getId());
            factorChildren.setLabel(resultList.get(i).getName());
            factorChildren.setValue(resultList.get(i).getNameUs());
            factorChildren.setFormula(resultList.get(i).getFormula());
            factorChildren.setDescription(resultList.get(i).getDescription());
            factorChildren.setDefaultValue(resultList.get(i).getDefaultValue());
            factorChildren.setMaxValue(resultList.get(i).getMaxValue());
            factorChildren.setMinValue(resultList.get(i).getMinValue());
            factorChildren.setAccuracy(resultList.get(i).getAccuracy());
            //新的类型
            if(!types.contains(resultList.get(i).getType())){
                types.add(resultList.get(i).getType());
                //添加类型
                factorInfo.setLabel(resultList.get(i).getType());
                factorInfo.setValue(resultList.get(i).getType());
                factorChildrenList.add(factorChildren);
                //添加孩子
                factorInfo.setChildren(factorChildrenList);
                //factorChildrenList.clear();
                factorInfos.add(factorInfo);
            }else{
                for (int j = 0; j < factorInfos.size(); j++) {
                    //多添加一个孩子
                    if(factorInfos.get(j).getLabel().equals(resultList.get(i).getType())){
                        factorInfos.get(j).getChildren().add(factorChildren);
                    }
                }
            }
        }

        return Result.success((factorInfos));
    }

    //得到因子分析表
    @GetMapping("getFactorsData")
    public Result<List<FactorAnalysis>> getStockData(@Param("stockPool") String stockPool, @Param("stockYear")  String stockYear,
                                                @Param("Theme") String Theme, @Param("factorsname") String[] factorsname) throws NoSuchMethodException, UnsupportedEncodingException {
        //得到股票池、周期、主题
        System.out.println(stockPool);
        System.out.println(stockYear);
        System.out.println(Theme);
        String s = URLDecoder.decode(Arrays.toString(factorsname), "UTF-8" );
        s = s.replace("[", "");
        s = s.replace("]", "");
        System.out.println(s);
        List<String> factorsname_list=Arrays.asList(s.split(","));
        for (int i = 0; i <factorsname_list.size() ; i++) {
            System.out.println(factorsname_list.get(i));
        }
        //得到主题因子
        String factor_theme = get_factor_theme(Theme);
        List<String> codelists= new ArrayList<>();
        if(stockPool.equals("沪深300")){
            codelists = CodeLists.get_CODES_HS300();
        } else if (stockPool.equals("中证100")) {
            codelists = CodeLists.get_CODES_ZZ100();
        }else if(stockPool.equals("中证500")){
            codelists = CodeLists.get_CODES_ZZ500();
        }

        List<Stock> resultList = new ArrayList<>();
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",stockYear+"/9/30");
        queryWrapper.in("code",codelists);
        resultList = stockService.list(queryWrapper);

        //返回给前端的形式
        List<FactorAnalysis> resList = new ArrayList<FactorAnalysis>();
        //遍历选定因子
        for (String factorname:factorsname_list){

            String name_us = getUsFactorName(factorname);
            //得到选择的因子list
            List<String> factor_selected = new ArrayList<>();
            List<String> theme_list = new ArrayList<>();
            for(Stock stock:resultList){
                //股息率需要特殊处理
                if(stock.getFactorName().equals(name_us)){
                    if(stock.getFactorName().equals("dividend_yield")){
                        factor_selected.add(stock.getFactorValue().replace("%", ""));
                    }else{
                        factor_selected.add(stock.getFactorValue());
                    }
                }
                if (stock.getFactorName().equals(factor_theme)){
                    if(stock.getFactorName().equals("dividend_yield")){
                        theme_list.add(stock.getFactorValue().replace("%", ""));
                    }else{
                        theme_list.add(stock.getFactorValue());
                    }
                }
            }
            factor_selected.remove("");
            factor_selected.removeAll(Collections.singleton(null));

            theme_list.remove("");
            theme_list.removeAll(Collections.singleton(null));
            //缺失因子
            int nullnumber_factor = resultList.size() - factor_selected.size();
            int nullnumber_theme = resultList.size() - theme_list.size();
            //缺失百分比
            DecimalFormat df =new DecimalFormat("#0.00%");
            String missing = df.format((float) nullnumber_factor / (float) resultList.size());
            //方差
            double variance = get_variance(factor_selected);
            //标准差
            double standard_deviation = get_standard_deviation(factor_selected);
            //与主题相关性
            double correlation = get_correlation(factor_selected,theme_list,nullnumber_factor,nullnumber_theme);
            FactorAnalysis factorAnalysis =new FactorAnalysis();
            factorAnalysis.setFactorname(factorname);
            factorAnalysis.setMissing(missing);
            factorAnalysis.setVariance(variance);
            factorAnalysis.setStandard_deviation(standard_deviation);
            factorAnalysis.setCorrelation(correlation);
            resList.add(factorAnalysis);
        }
        return Result.success((resList));
    }

    //得到因子相关性表
    @GetMapping("getCorrelationFactors")
    //得到二维矩阵因子间相关性
    public Result<List<List<Map<String, Double>>>> getCorrelationFactors(@Param("stockPool") String stockPool, @Param("stockYear")  String stockYear, @Param("factorsname") String[] factorsname) throws UnsupportedEncodingException {
        System.out.println(factorsname);
        System.out.println("123");
        String s = URLDecoder.decode(Arrays.toString(factorsname), "UTF-8");
        s = s.replace("[", "");
        s = s.replace("]", "");
//        System.out.println(s);
        List<String> factorsname_list=Arrays.asList(s.split(","));
        double[][] correlation_factors = new double[factorsname_list.size()][factorsname_list.size()];

        //获得中证或沪深代表的股票代码
        List<String> codelists= new ArrayList<>();
        if(stockPool.equals("沪深300")){
            codelists = CodeLists.get_CODES_HS300();
        } else if (stockPool.equals("中证100")) {
            codelists = CodeLists.get_CODES_ZZ100();
        }else if(stockPool.equals("中证500")){
            codelists = CodeLists.get_CODES_ZZ500();
        }
        //以年份和股票列表做筛选
        List<Stock> resultList = new ArrayList<>();
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",stockYear+"/9/30");
        queryWrapper.in("code",codelists);
        resultList = stockService.list(queryWrapper);

        List<List<Map<String,Double>>> res = new ArrayList<List<Map<String,Double>>>();

        for (int i = 0; i < factorsname_list.size(); i++) {
            //定义中间组件
            List<Map<String,Double>> res_mid = new ArrayList<>();
            for (int j = 0; j < factorsname_list.size(); j++) {
                //获取i对应的英文名
                String name_us1 = getUsFactorName(factorsname_list.get(i));
                //获取j对应的英文名
                String name_us2 = getUsFactorName(factorsname_list.get(j));

                Map<String,Double> stringDoubleMap = new HashMap<>();

                if(i==j){
                    stringDoubleMap.put(name_us2, 1.0);
                    //correlation_factors[i][j] =1;
                }else{
                    stringDoubleMap.put(name_us2, get_correlations_factor_factor(name_us1,name_us2,resultList));
                    //correlation_factors[i][j] = get_correlations_factor_factor(name_us1,name_us2,resultList);
                }
                res_mid.add(stringDoubleMap);
            }
            res.add(res_mid);
        }
//        System.out.println(res);
        return Result.success(res);
    }

    private double get_correlations_factor_factor(String nameUs1, String nameUs2, List<Stock> resultList) {
        //得到i和j的因子list

        List<String> factor_list1 = new ArrayList<>();
        List<String> factor_list2 = new ArrayList<>();
        for (Stock stock : resultList) {
            //股息率需要特殊处理
            if (stock.getFactorName().equals(nameUs1)) {
                if (stock.getFactorName().equals("dividend_yield")) {
                    factor_list1.add(stock.getFactorValue().replace("%", ""));
                } else {
                    factor_list1.add(stock.getFactorValue());
                }
            }
            if (stock.getFactorName().equals(nameUs2)) {
                if (stock.getFactorName().equals("dividend_yield")) {
                    factor_list2.add(stock.getFactorValue().replace("%", ""));
                } else {
                    factor_list2.add(stock.getFactorValue());
                }
            }
        }
        factor_list1.remove("");
        factor_list1.removeAll(Collections.singleton(null));

        factor_list2.remove("");
        factor_list2.removeAll(Collections.singleton(null));

        //缺失因子
        int nullnumber_factor1 = resultList.size() - factor_list1.size();
        int nullnumber_factor2 = resultList.size() - factor_list2.size();

        return get_correlation(factor_list1,factor_list2,nullnumber_factor1,nullnumber_factor2);
    }

    //得到主题相关性
    private double get_correlation(List<String> factorList, List<String> themeList,int nullnumber_factor,int nullnumber_theme) {
        //因子列都变成double类型
        List<Double> factor_list_double = factorList.stream().map(Double::valueOf).collect(Collectors.toList());
        List<Double> theme_list_double = themeList.stream().map(Double::valueOf).collect(Collectors.toList());

        if(nullnumber_factor>0||nullnumber_theme>0) {
            double avg_factor = factor_list_double.stream().collect(Collectors.averagingDouble(x -> x));
            double avg_theme = theme_list_double.stream().collect(Collectors.averagingDouble(x -> x));
            //根据缺失值补充均值进行处理
            for (int i = 0; i < nullnumber_factor; i++) {
                factor_list_double.add(avg_factor);
            }

            for (int i = 0; i < nullnumber_theme; i++) {
                theme_list_double.add(avg_theme);
            }
        }

        double[] arr1 = factor_list_double.stream().mapToDouble(i->i).toArray();
        double[] arr2 = theme_list_double.stream().mapToDouble(i->i).toArray();
        return correlation(arr1,arr2);
    }

    public String getUsFactorName(String factorName){
        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorName);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();
        return name_us;
    }
}