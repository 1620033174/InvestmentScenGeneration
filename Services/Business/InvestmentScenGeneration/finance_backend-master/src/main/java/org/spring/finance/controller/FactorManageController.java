package org.spring.finance.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.po.Stock;
import org.spring.finance.service.StockService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/hz/factor")
public class FactorManageController {
    @Autowired
    private FactorHZService factorHZService;

    @Autowired
    private FactorHZService factorHZService1;

    @Autowired
    private StockService stockService;
    // 查看全部因子
    @GetMapping("getAll")
    public Result<List<FactorHZ>> getAllFactors(){
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper);
        return Result.success((factorHZList));
    }

    @GetMapping("getByPage")
    public Result<Page<FactorHZ>> getByPage(int page, int pageSize,String name,String type){
        Page<FactorHZ> pageInfo = new Page<>(page,pageSize);
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        if (""!=name&&name!=null){
            queryWrapper.like("name",name);
        }
        if (""!=type&&type!=null){
            queryWrapper.like("type",type);
        }
        factorHZService.page(pageInfo, queryWrapper);
        return Result.success(pageInfo);
    }

    @GetMapping("getByName")
    public Result<List<FactorHZ>> getByName(@Param("name")String name){
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        List<FactorHZ> factorHZList = factorHZService.list(queryWrapper);
        return Result.success((factorHZList));
    }

    // 删除因子和该因子对应的股票数据
    @DeleteMapping("deleteById/{id}")
    public Result<Void> deleteFactors(@PathVariable("id")Integer id){
//        System.out.println(id);
//        int id_int = Integer.parseInt(id);
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        factorHZService.remove(queryWrapper);
        //获取因子的name_us
        QueryWrapper<Stock> queryWrapper1 = new QueryWrapper<>();
        FactorHZ factorHZ = factorHZService1.getById(id);
        String name_us = factorHZ.getNameUs();
        queryWrapper1.eq("factor_name",name_us);
        stockService.remove(queryWrapper1);
        return Result.success();
    }

    //更新
    @PostMapping("update")
    public Result<String> update(@RequestBody FactorHZ factorHZ) {
        factorHZService.updateById(factorHZ);
        return Result.success("success");
    }

    //todo:增加因子， 需要增加列
    @PostMapping("add")
    public Result<String> add(@RequestBody FactorHZ factorHZ) {
        factorHZService.save(factorHZ);
        return Result.success("success");
    }

    //使用easyExcel导入
    @PostMapping("import")
    public Result importExcel(@RequestPart(value = "file") MultipartFile file){
        try {
            List<Stock> stockList = EasyExcel.read(file.getInputStream())
                    .head(Stock.class)
                    .sheet()
                    .doReadSync();
            //如果有name,则更新name
            stockService.saveBatch(stockList);
            return Result.success("success");
        } catch (IOException e) {
            return Result.failure("fail");
        }
    }
}
