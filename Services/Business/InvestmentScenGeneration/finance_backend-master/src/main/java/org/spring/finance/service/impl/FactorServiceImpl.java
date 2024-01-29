package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.Factor;
import org.spring.finance.mapper.FactorMapper;
import org.spring.finance.service.FactorService;
import org.springframework.stereotype.Service;

@Service
public class FactorServiceImpl extends ServiceImpl <FactorMapper, Factor> implements FactorService {
}
