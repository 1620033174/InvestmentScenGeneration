package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.Stock;
import org.spring.finance.mapper.StockMapper;
import org.spring.finance.service.StockService;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends ServiceImpl <StockMapper, Stock> implements StockService {
}
