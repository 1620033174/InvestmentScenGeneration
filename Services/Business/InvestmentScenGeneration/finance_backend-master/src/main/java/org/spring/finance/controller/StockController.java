package org.spring.finance.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.spring.finance.entity.hz.StockDataTest;
import org.spring.finance.entity.vo.AlgorithmVo;
import org.spring.finance.service.AlgorithmFactor1Service;
import org.spring.finance.service.AlgorithmService;
import org.spring.finance.service.StockDataTestService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/stock11")
public class StockController {
    @Autowired
    private AlgorithmService algorithmService_lwz;
    @Autowired
    private AlgorithmFactor1Service algorithmFactors1Service;

    @Autowired
    private FactorHZService factorHZService;


    @Autowired
    private StockDataTestService stockDataTestService;

    @Autowired
    private StockDataTestService stockDataTestServiceCount;


    @Autowired
    private StockDataTestService stockDataTestServiceLimit;

    @PostMapping("getSimulatedStock")
    public Result<List<StockDataTest>> getSimulatedStock(@RequestBody AlgorithmVo algorithmVO) throws Exception {
        //1、根据算法查询获得因子，根据因子获取
        int len = algorithmVO.getFactors().size();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        QueryWrapper<StockDataTest> queryWrapper = new QueryWrapper<>();
        QueryWrapper<StockDataTest> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<StockDataTest> queryWrapper2 = new QueryWrapper<>();
        //先假定数据集为2020年
        queryWrapper.eq("stadate","2020/9/30");
        queryWrapper1.eq("stadate","2020/9/30");
        String[] factorNameStrings = new String[len+2];
        factorNameStrings[0] = "id";
        factorNameStrings[1] = "code";
        int index = 2;
        for (AlgorithmVo.FactorHZ factor : list) {
            //得到因子名称
            String factorNameUs = factorHZService.getById(factor.getId()).getNameUs();
            String factorNameUs0 = factorNameUs + "+0";
            String logic = factor.getLogic();
            int choiceType = factor.getChoiceType();
            double value = Double.parseDouble(factor.getValue());
            int isTop = factor.getIsTop();
            System.out.println("喜喜");
            System.out.println(value);
            //先选因子列
            factorNameStrings[index++] = factorNameUs;
            //queryWrapper.select(factorNameUs);
            //0：百分比 ，需要提前查询其值
            if(choiceType==0){
                //从大到小
                if(isTop==1){
                    //获取限制的数量
                    long count = stockDataTestServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByDesc(factorNameUs0);
                    System.out.println(limit);
                    queryWrapper2.last("limit 0,"+limit);
                    List<StockDataTest> listLimit = stockDataTestServiceLimit.list(queryWrapper2);
                    StockDataTest stockData = listLimit.get(listLimit.size() - 1);
                    String factorname_get = getFactorname(factorNameUs);
                    //反射获取get方法得到对应因子值
                    Object ob = getGetMethod(stockData, factorname_get);
                    System.out.println(ob);
                    //拼接where条件
                    queryWrapper.ge(factorNameUs0,ob);

                }else{
                    //获取限制的数量
                    long count = stockDataTestServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByAsc(factorNameUs0);
                    queryWrapper2.last("limit 0,"+limit);
                    List<StockDataTest> listLimit = stockDataTestServiceLimit.list(queryWrapper2);
                    StockDataTest stockData = listLimit.get(listLimit.size() - 1);
                    String factorname_get = getFactorname(factorNameUs);
                    //反射获取get方法得到对应因子值
                    Object ob = getGetMethod(stockData, factorname_get);
                    System.out.println(ob);
                    //拼接where条件
                    queryWrapper.le(factorNameUs0,ob);

                }
            } else if (choiceType == 2) {
                //2：大于小于

                if(logic.equals("大于等于")){
                    queryWrapper.ge(factorNameUs0,value);
                } else if (logic.equals("小于等于")) {
                    queryWrapper.le(factorNameUs0,value);
                }else if(logic.equals("大于")) {
                    queryWrapper.gt(factorNameUs0,value);
                }else if(logic.equals("小于")) {
                    queryWrapper.lt(factorNameUs0,value);
                }else if(logic.equals("等于")) {
                    queryWrapper.eq(factorNameUs0,value);
                }
            }else {
                //1：排名数字 分大到小和从小到大
                if(isTop==1) {
                    queryWrapper.orderByDesc(factorNameUs0);
                    queryWrapper.last("limit 0," + (int)value*2);
                }else{
                    queryWrapper.orderByAsc(factorNameUs0);
                    queryWrapper.last("limit 0," + (int)value*2);
                }
            }
        }
        queryWrapper.select(factorNameStrings);
        List<StockDataTest> list1 = stockDataTestService.list(queryWrapper);
        return Result.success(list1);
    }

    //回测用
    @PostMapping("getStockList")
    public Result<List<String>> getStockList(@RequestBody AlgorithmVo algorithmVO) throws Exception {
        //1、根据算法查询获得因子，根据因子获取
        int len = algorithmVO.getFactors().size();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        QueryWrapper<StockDataTest> queryWrapper = new QueryWrapper<>();
        QueryWrapper<StockDataTest> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<StockDataTest> queryWrapper2 = new QueryWrapper<>();
        //先假定数据集为2020年
        queryWrapper.eq("stadate","2020/9/30");
        queryWrapper1.eq("stadate","2020/9/30");
        String[] factorNameStrings = new String[len+2];
        factorNameStrings[0] = "id";
        factorNameStrings[1] = "code";
        int index = 2;
        for (AlgorithmVo.FactorHZ factor : list) {
            //得到因子名称
            String factorNameUs = factorHZService.getById(factor.getId()).getNameUs();
            String factorNameUs0 = factorNameUs + "+0";
            String logic = factor.getLogic();
            int choiceType = factor.getChoiceType();
            double value = Double.parseDouble(factor.getValue());
            int isTop = factor.getIsTop();
            System.out.println("喜喜");
            System.out.println(value);
            //先选因子列
            factorNameStrings[index++] = factorNameUs;
            //queryWrapper.select(factorNameUs);
            //0：百分比 ，需要提前查询其值
            if(choiceType==0){
                //从大到小
                if(isTop==1){
                    //获取限制的数量
                    long count = stockDataTestServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByDesc(factorNameUs0);
                    System.out.println(limit);
                    queryWrapper2.last("limit 0,"+limit);
                    List<StockDataTest> listLimit = stockDataTestServiceLimit.list(queryWrapper2);
                    StockDataTest stockData = listLimit.get(listLimit.size() - 1);
                    String factorname_get = getFactorname(factorNameUs);
                    //反射获取get方法得到对应因子值
                    Object ob = getGetMethod(stockData, factorname_get);
                    System.out.println(ob);
                    //拼接where条件
                    queryWrapper.ge(factorNameUs0,ob);

                }else{
                    //获取限制的数量
                    long count = stockDataTestServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByAsc(factorNameUs0);
                    queryWrapper2.last("limit 0,"+limit);
                    List<StockDataTest> listLimit = stockDataTestServiceLimit.list(queryWrapper2);
                    StockDataTest stockData = listLimit.get(listLimit.size() - 1);
                    String factorname_get = getFactorname(factorNameUs);
                    //反射获取get方法得到对应因子值
                    Object ob = getGetMethod(stockData, factorname_get);
                    System.out.println(ob);
                    //拼接where条件
                    queryWrapper.le(factorNameUs0,ob);

                }
            } else if (choiceType == 2) {
                //2：大于小于
                if(logic.equals("大于等于")){
                    queryWrapper.ge(factorNameUs0,value);
                } else if (logic.equals("小于等于")) {
                    queryWrapper.le(factorNameUs0,value);
                }else if(logic.equals("大于")) {
                    queryWrapper.gt(factorNameUs0,value);
                }else if(logic.equals("小于")) {
                    queryWrapper.lt(factorNameUs0,value);
                }else if(logic.equals("等于")) {
                    queryWrapper.eq(factorNameUs0,value);
                }
            }else {
                //1：数字 分大到小和从小到大
                if(isTop==1) {
                    queryWrapper.orderByDesc(factorNameUs0);
                    queryWrapper.last("limit 0," + (int)value);
                }else{
                    queryWrapper.orderByAsc(factorNameUs0);
                    queryWrapper.last("limit 0," + (int)value);
                }
            }
        }
        queryWrapper.select(factorNameStrings);
        List<StockDataTest> list1 = stockDataTestService.list(queryWrapper);
        List<String> stockList = new ArrayList<>();

        for (int i = 0; i < list1.size(); i++) {
            stockList.add(list1.get(i).getCode());
        }
        return Result.success(stockList);
    }

    private String getFactorname(String factorNameUs) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        char[] chars = factorNameUs.toCharArray();
        for (int i = 0; i <chars.length; i++) {
            if(chars[i]=='_'){

                chars[i+1] = Character.toUpperCase(chars[i+1]);
            }else{
                stringBuilder.append(chars[i]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 根据属性，获取get方法
     * @param ob 对象
     * @param name 属性名
     * @return
     * @throws Exception
     */
    public static Object getGetMethod(Object ob , String name)throws Exception {
        Method[] m = ob.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }
}
