package org.spring.finance.service.hz.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.hz.Result;
import org.spring.finance.mapper.hz.ResultMapper;
import org.spring.finance.service.hz.ResultService;
import org.springframework.stereotype.Service;

@Service
public class ResultServiceImpl extends ServiceImpl<ResultMapper, Result> implements ResultService {
}
