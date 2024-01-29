package org.spring.finance.entity.po;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class Scene {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String sceneName;

    private String theme;

    private String description;

    private String tags;

    private String stockList;

    private String date;
}
