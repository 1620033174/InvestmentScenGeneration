package org.spring.finance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SceneAlgorithm {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer SceneId;
    private Integer algorithmId;


}


