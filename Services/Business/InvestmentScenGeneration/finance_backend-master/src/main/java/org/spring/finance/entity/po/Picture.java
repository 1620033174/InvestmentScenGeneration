package org.spring.finance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Picture {
    @TableId(type = IdType.AUTO)
    private int id;
    private String name;
}
