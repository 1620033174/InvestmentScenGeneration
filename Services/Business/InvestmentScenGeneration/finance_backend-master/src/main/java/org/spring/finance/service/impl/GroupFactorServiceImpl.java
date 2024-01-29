package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.GroupFactor;
import org.spring.finance.mapper.GroupFactorMapper;
import org.spring.finance.service.GroupFactorService;
import org.springframework.stereotype.Service;

@Service
public class GroupFactorServiceImpl extends ServiceImpl <GroupFactorMapper, GroupFactor> implements GroupFactorService {
}
