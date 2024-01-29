package org.spring.finance.controller.hz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gg/factor")
public class HzFactorManageController {
    @Autowired
    private FactorHZService factorHZService;

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

    // 删除因子
    @DeleteMapping("deleteById/{id}")
    public Result<Void> deleteFactors(@PathVariable("id")Integer id){
        System.out.println(id);
//        int id_int = Integer.parseInt(id);
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        factorHZService.remove(queryWrapper);
        return Result.success();
    }

    //更新
    @PostMapping("update")
    public Result<String> update(@RequestBody FactorHZ factorHZ) {
        factorHZService.updateById(factorHZ);
        return Result.success("success");
    }

    //增加
    @PostMapping("add")
    public Result<String> add(@RequestBody FactorHZ factorHZ) {
        factorHZService.save(factorHZ);
        return Result.success("success");
    }
}
