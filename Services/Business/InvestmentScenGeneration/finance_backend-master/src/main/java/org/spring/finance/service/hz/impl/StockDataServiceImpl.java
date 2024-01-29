package org.spring.finance.service.hz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.hz.Result;
import org.spring.finance.entity.hz.StockData;
import org.spring.finance.mapper.hz.ResultMapper;
import org.spring.finance.mapper.hz.StockDataMapper;
import org.spring.finance.service.hz.ResultService;
import org.spring.finance.service.hz.StockDataService;
import org.springframework.stereotype.Service;

@Service
public class StockDataServiceImpl extends ServiceImpl<StockDataMapper, StockData> implements StockDataService {
}
