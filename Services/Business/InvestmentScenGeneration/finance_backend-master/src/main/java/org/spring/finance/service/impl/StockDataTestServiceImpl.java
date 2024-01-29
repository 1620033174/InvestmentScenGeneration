package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.hz.StockDataTest;
import org.spring.finance.entity.po.Algorithm;
import org.spring.finance.mapper.AlgorithmMapper;
import org.spring.finance.mapper.StockDataTestMapper;
import org.spring.finance.service.AlgorithmService;
import org.spring.finance.service.StockDataTestService;
import org.springframework.stereotype.Service;

@Service
public class StockDataTestServiceImpl extends ServiceImpl<StockDataTestMapper, StockDataTest> implements StockDataTestService {
}