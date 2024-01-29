package org.spring.finance.entity.vo;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.spring.finance.entity.po.Algorithm;
import org.spring.finance.entity.po.Picture;

import java.util.List;
@Data
public class SceneVO {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String sceneName;

    private String theme;

    private String description;

    private String date;

    private List<String> tags;

    private List<String> stockList;

    private List<Algorithm> algorithmList;

    private List<Picture> pictureList;

}
