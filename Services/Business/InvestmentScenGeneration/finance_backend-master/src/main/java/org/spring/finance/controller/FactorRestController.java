package org.spring.finance.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.po.Factor;
import org.spring.finance.entity.po.GroupFactor;
import org.spring.finance.service.FactorService;
import org.spring.finance.service.GroupFactorService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/lwz/factors")
//@CrossOrigin(origins = "http://localhost:9528")
@CrossOrigin(origins = "*")
public class FactorRestController {
    @Autowired
    private FactorService factorService;
    @Autowired
    private GroupFactorService groupFactorService;

    @Autowired
    private FactorHZService factorHZService;

    @GetMapping("group")
    public Result<List<Factor>> getPreselectedFactorsByGroupId(@RequestParam String groupId) {
        List<Factor> preselectedFactorVoList = new ArrayList<>();
        QueryWrapper<GroupFactor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("group_id", groupId);
        List<GroupFactor> groupFactorList = groupFactorService.list(queryWrapper);
        for (GroupFactor item : groupFactorList
        ) {
            preselectedFactorVoList.add(factorService.getById(item.getFactorId()));
        }
        return Result.success((preselectedFactorVoList));
    }

    @GetMapping("getAllFactors")
    public Result<List<FactorHZ>> getAllFactors() {
        List<FactorHZ> factorHZList = new ArrayList<>();
        QueryWrapper<FactorHZ> queryWrapper = new QueryWrapper<>();

        queryWrapper.select();
        factorHZList = factorHZService.list(queryWrapper);
        return Result.success(factorHZList);
    }



}