package org.spring.finance.service.hz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.mapper.hz.FactorHZMapper;
import org.spring.finance.service.hz.FactorHZService;
import org.springframework.stereotype.Service;

@Service
public class FactorHZServiceImpl extends ServiceImpl<FactorHZMapper, FactorHZ> implements FactorHZService {
}
