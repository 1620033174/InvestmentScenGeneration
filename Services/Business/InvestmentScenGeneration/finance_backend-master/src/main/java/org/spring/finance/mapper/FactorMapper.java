package org.spring.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.spring.finance.entity.po.Factor;

//@Mapper // 标志为 Mybatis 的 Mapper
@Mapper
public interface FactorMapper extends BaseMapper<Factor> {
}
