package org.spring.finance.entity.hz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
public class Result {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer factorId;
    private Integer setId;
    private String name;
    private double missing;
    private double variance;
    private double frequency;
    private double correlation;
}