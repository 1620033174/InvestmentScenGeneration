package org.spring.finance.entity.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Stock {
    //雪花算法
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @ExcelProperty("股票代码")
    private String code;
    @ExcelProperty("股票名称")
    private String stockName;
    @ExcelProperty("发布日期")
    private String pubdate;
    @ExcelProperty("开始日期")
    private String date;
    @ExcelProperty("因子名称")
    private String factorName;
    @ExcelProperty("因子值")
    private String factorValue;
}
