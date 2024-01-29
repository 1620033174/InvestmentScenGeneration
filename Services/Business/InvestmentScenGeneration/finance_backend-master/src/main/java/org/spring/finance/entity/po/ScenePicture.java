package org.spring.finance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ScenePicture {
    @TableId(type = IdType.AUTO)
    private int id;
    private Integer sceneId;
    private Integer pictureId;
}
