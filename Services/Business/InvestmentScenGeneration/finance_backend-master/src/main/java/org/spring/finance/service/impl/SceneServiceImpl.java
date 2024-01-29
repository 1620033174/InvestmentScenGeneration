package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.Scene;
import org.spring.finance.mapper.SceneMapper;
import org.spring.finance.service.SceneService;
import org.springframework.stereotype.Service;

@Service
public class SceneServiceImpl extends ServiceImpl <SceneMapper, Scene> implements SceneService {
}
