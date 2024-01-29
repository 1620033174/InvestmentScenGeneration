package org.spring.finance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
/*因子总表*/
public class Factor {
    @TableId(type = IdType.AUTO)
    private String id;
    @TableField(value = "factor_name")
    private String name;
}
