package org.spring.finance.entity.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
/**
 * hz给的数据库, 预选因子表
 */
@TableName("factor_group")
public class GroupFactor {
    @TableId(type = IdType.AUTO)
    private String id;
    private  String groupId;
    private String factorId;
}
