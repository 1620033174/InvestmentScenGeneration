package org.spring.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.spring.finance.entity.po.Stock;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}
