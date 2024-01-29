package org.spring.finance.utils.factor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.service.hz.FactorHZService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class factorNameToUs {
    @Autowired
    private FactorHZService factorHZService1;
    //根据因子名字获取因子英文名字
    public String getUsFactorName(String factorName){
        QueryWrapper<FactorHZ> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name",factorName);
        List<FactorHZ> factorHZList = factorHZService1.list(queryWrapper1);
        String name_us = factorHZList.get(0).getNameUs();
        return name_us;
    }
}
