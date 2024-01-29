package org.spring.finance.entity.hz;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("factor_hz")
public class FactorHZ {
    @TableId(type = IdType.AUTO)
    private int id;
    private String name;
    private String type;
    private String formula;
    private String description;
    private String nameUs;
    private double defaultValue;
    private double maxValue;
    private double minValue;
    private double accuracy;
}
