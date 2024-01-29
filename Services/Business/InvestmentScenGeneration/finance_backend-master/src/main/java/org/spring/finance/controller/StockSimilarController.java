package org.spring.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.hz.StockDataTest;
import org.spring.finance.entity.po.*;
import org.spring.finance.entity.vo.AlgorithmVo;
import org.spring.finance.entity.vo.SceneVO;
import org.spring.finance.service.*;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scene")
public class StockSimilarController {
    @Autowired
    private SceneService sceneService;

    //根据股票代码获取相似个股，仍是2020年数据，有一个新增的相似度字段
    @GetMapping("getSceneSimilarStocks")
    public Result<List<StockDataTest>> getSceneSimilarStocks(@RequestParam Integer id){
        //根据场景id获得场景信息中的stockList
        Scene scene = sceneService.getById(id);
        String stockList = scene.getStockList();
        //解析stockList存成list
        List<String> stockCodeList = Arrays.asList(stockList.split(","));
        //根据stockCodeList查询相似个股
        List<StockDataTest> stockDataTestList = new ArrayList<>();
        return Result.success(stockDataTestList);
    }

    @GetMapping("getHistoryMostSimilarStock")
    public Result<List<StockDataTest>> getHistoryMostSimilarStock(@RequestParam Integer id){
        //根据场景id获得场景信息中的stockList
        Scene scene = sceneService.getById(id);
        String stockList = scene.getStockList();
        //解析stockList存成list
        List<String> stockCodeList = Arrays.asList(stockList.split(","));
        //根据stockCodeList查询相似个股
        List<StockDataTest> stockDataTestList = new ArrayList<>();
        return Result.success(stockDataTestList);
    }



}
