package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.Picture;
import org.spring.finance.mapper.PictureMapper;
import org.spring.finance.service.PictureService;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl extends ServiceImpl <PictureMapper, Picture> implements PictureService {
}
