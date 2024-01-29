package org.spring.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.spring.finance.entity.po.GroupFactor;

@Mapper
public interface GroupFactorMapper extends  BaseMapper<GroupFactor>{
}

//@Mapper // 标志为 Mybatis 的 Mapper

