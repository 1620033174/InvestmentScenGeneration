package org.spring.finance.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.po.Stock;
import org.spring.finance.entity.vo.AlgorithmVo;
import org.spring.finance.service.StockService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stock")
public class StockControllerReal {

    @Autowired
    private FactorHZService factorHZService;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockService stockServiceCount;

    @Autowired
    private StockService stockServiceLimit;

    @PostMapping("getSimulatedStock")
    public Result<List<Map<String, String>>> getSimulatedStock(@RequestBody AlgorithmVo algorithmVO) throws Exception {
        //1、根据算法查询获得因子，根据因子获取
        int len = algorithmVO.getFactors().size();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();

        List<Stock> res = new ArrayList<>();
        Set<String> existingStockNames = new HashSet<>();
        for (AlgorithmVo.FactorHZ factor : list) {
            QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
            QueryWrapper<Stock> queryWrapper1 = new QueryWrapper<>();
            QueryWrapper<Stock> queryWrapper2 = new QueryWrapper<>();
            //先假定数据集为2020年
            queryWrapper.eq("date","2020/9/30");
            queryWrapper1.eq("date","2020/9/30");
            queryWrapper2.eq("date","2020/9/30");
            List<Stock> list1 = new ArrayList<>();
            String factorNameUs = factorHZService.getById(factor.getId()).getNameUs();
            String factorNameUs0 = factorNameUs + "+0";
            String logic = factor.getLogic();
            int choiceType = factor.getChoiceType();
            double value = Double.parseDouble(factor.getValue());
            int isTop = factor.getIsTop();
            //先选因子列
            queryWrapper.eq("factor_name",factorNameUs);
            queryWrapper1.eq("factor_name",factorNameUs);
            queryWrapper2.eq("factor_name",factorNameUs);

            //0：百分比
            if(choiceType==0){
                //从大到小
                if(isTop==1){
                    //获取限制的数量
                    long count = stockServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByDesc("factor_value+0");
                    queryWrapper2.last("limit 0,"+limit);
                    List<Stock> listLimit = stockServiceLimit.list(queryWrapper2);
                    Stock stockData = listLimit.get(listLimit.size() - 1);
                    //拼接where条件
                    queryWrapper.ge("factor_value+0",stockData.getFactorValue());

                }else{
                    //获取限制的数量
                    long count = stockServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);
                    queryWrapper2.orderByAsc("factor_value+0");
                    queryWrapper2.last("limit 0,"+limit);
                    List<Stock> listLimit = stockServiceLimit.list(queryWrapper2);
                    Stock stockData = listLimit.get(listLimit.size() - 1);
                    //拼接where条件
                    queryWrapper.le("factor_value+0",stockData.getFactorValue());
                }
            } else if (choiceType == 2) {
                //2：大于小于
                if(logic.equals("大于等于")){
                    queryWrapper.ge("factor_value+0",value);
                } else if (logic.equals("小于等于")) {
                    queryWrapper.le("factor_value+0",value);
                }else if(logic.equals("大于")) {
                    queryWrapper.gt("factor_value+0",value);
                }else if(logic.equals("小于")) {
                    queryWrapper.lt("factor_value+0",value);
                }else if(logic.equals("等于")) {
                    queryWrapper.eq("factor_value+0",value);
                }
            }else {
                //1：排名数字 分大到小和从小到大
                if(isTop==1) {
                    if(res.size()!=0){
                        //针对已有的数据排名取前十
                        queryWrapper.in("stock_name",res.stream()
                                .map(Stock::getStockName)
                                .collect(Collectors.toList()));
                        queryWrapper.orderByDesc("factor_value+0");
                        queryWrapper.last("limit 0," + (int)value*2);
                    }else{
                        queryWrapper.last("limit 0," + (int)value*2);
                    }
                    //queryWrapper.last("limit 0," + (int)value*2);
                }else{
                    queryWrapper.orderByAsc("factor_value+0");
                    //queryWrapper.last("limit 0," + (int)value*2);
                }
            }
            list1 = stockService.list(queryWrapper);
            if (res.isEmpty()) {
                res.addAll(list1);
                existingStockNames.addAll(list1.stream()
                        .map(Stock::getStockName)
                        .collect(Collectors.toSet())); // 使用HashSet来加速查找
            } else {
                //list1与res取交集,list1
                List<Stock> list1intersection = list1.stream()
                        .filter(stock -> existingStockNames.contains(stock.getStockName()))
                        .collect(Collectors.toList());
                //把res中与intersection的factor_name不同的元素删除
                existingStockNames.clear();
                existingStockNames.addAll(list1intersection.stream()
                        .map(Stock::getStockName)
                        .collect(Collectors.toSet()));
                //res与intersection取交集,res
                List<Stock> resIntersection = res.stream()
                        .filter(stock -> existingStockNames.contains(stock.getStockName()))
                        .collect(Collectors.toList());
                res.clear();
                res.addAll(list1intersection);
                res.addAll(resIntersection);
            }
        }

        List<Map<String, String>> maps = mergeStockData(res);
        return Result.success(maps);

    }
    public List<Map<String, String>> mergeStockData1(List<Stock> res){
        Map<String, Map<String, String>> mergedData = new HashMap<>();
        // 遍历原始数据列表
        for (Stock stock : res) {
            String stockName = stock.getStockName();
            //根据因子英文名称从factorHZ获取中文名称
            QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name_us",stock.getFactorName());
            FactorHZ factorHZList = factorHZService.list(queryWrapper).get(0);
            String factorName = factorHZList.getName();
            // 如果mergedData中已经有这个stock_name的数据，就获取对应的Map
            // 否则，创建一个新的Map
            Map<String, String> stockData = mergedData.getOrDefault(stockName, new HashMap<>());

            // 将原始数据中的字段和值添加到stockData中
            stockData.put("股票代码", stock.getCode());
            stockData.put("股票名称", stock.getStockName());
            stockData.put("发布日期", stock.getPubdate());
            stockData.put("开始日期", stock.getDate());
            stockData.put(factorName, stock.getFactorValue());

            // 更新或添加合并后的数据到mergedData中
            mergedData.put(stockName, stockData);
        }
        List<Map<String, String>> mergedList = new ArrayList<>(mergedData.values());
        return mergedList;
    }

    public List<Map<String, String>> mergeStockData(List<Stock> res){
        Map<String, Map<String, String>> mergedData = new HashMap<>();
        // 遍历原始数据列表
        for (Stock stock : res) {
            String stockName = stock.getStockName();

            // 如果mergedData中已经有这个stock_name的数据，就获取对应的Map
            // 否则，创建一个新的Map
            Map<String, String> stockData = mergedData.getOrDefault(stockName, new HashMap<>());

            // 将原始数据中的字段和值添加到stockData中
            stockData.put("code", stock.getCode());
            stockData.put("stock_name", stock.getStockName());
            stockData.put("pubdate", stock.getPubdate());
            stockData.put("date", stock.getDate());
            stockData.put(stock.getFactorName(), stock.getFactorValue());

            // 更新或添加合并后的数据到mergedData中
            mergedData.put(stockName, stockData);
        }
        List<Map<String, String>> mergedList = new ArrayList<>(mergedData.values());
        return mergedList;
    }
    //回测用
    @GetMapping("getNameByDataSource")
    public String getNameByDataSource(@RequestParam String factorNameUS) throws Exception {
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name_us",factorNameUS);
        FactorHZ factorHZList = factorHZService.list(queryWrapper).get(0);
        String factorNameEN = factorHZList.getName();
        return factorNameEN;
    }
    //得到条件选股股票代码
    @PostMapping("getStockList")
    public Result<List<String>> getStockList(@RequestBody AlgorithmVo algorithmVO) throws Exception {
        //1、根据算法查询获得因子，根据因子获取
        Result<List<Map<String, String>>> simulatedStock = getSimulatedStock(algorithmVO);
        //取出所有的code，保存为List<String>并返回
        List<Map<String, String>> data = simulatedStock.getData();
        List<String> stockList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            stockList.add(data.get(i).get("code"));
        }
        return Result.success(stockList);
    }
    //得到综合选股股票代码
    @GetMapping("getInitStockList")
    public Result<List<Map<String, String>>> getInitStockList(@RequestParam String date,@RequestParam String factorsName) throws Exception {

//        factorsName = factorsName.replace("[", "");
//        factorsName = factorsName.replace("]", "");
//        factorsName = factorsName.replace("\"", "");
//        System.out.println(factorsName);
        List<String> factorsname_list=Arrays.asList(factorsName.split(","));
        date = date + "/9/30";
        //根据日期和因子列
        QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date",date)
                    .in("factor_name",factorsname_list);
        List<Stock> list = stockService.list(queryWrapper);
        List<Map<String, String>> maps = mergeStockData(list);
        return Result.success(maps);
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
