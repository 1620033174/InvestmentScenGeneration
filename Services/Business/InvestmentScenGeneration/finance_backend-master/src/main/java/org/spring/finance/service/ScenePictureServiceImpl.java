package org.spring.finance.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.ScenePicture;
import org.spring.finance.mapper.ScenePictureMapper;
import org.springframework.stereotype.Service;

@Service
public class ScenePictureServiceImpl extends ServiceImpl <ScenePictureMapper, ScenePicture> implements ScenePictureService {
}
