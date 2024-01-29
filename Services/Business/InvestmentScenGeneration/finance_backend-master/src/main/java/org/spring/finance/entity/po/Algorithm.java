package org.spring.finance.entity.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * algorithm
 */
@Data
public class Algorithm {
    /**
     * 算法id
     */
    @TableId(type = IdType.AUTO)
    private String id;
    /**
     * 算法名称
     */
    private String name;
    /**
     * 算法描述：对于条件选股由业务人员直接编写，对于综合选股由技术人员编写
     */
    private String algorithmDescription;
    /**
     * 1条件选股/2综合选股
     * 条件选股： 多因子，每个因子一个选股逻辑描述，在选股因子列表中可以看到对应字段
     * 综合选股： 多因子，但整个算法只有一个选股逻辑描述，写在👇logic_description中
     */
    private String type;
    /**
     * 选股逻辑描述,仅2综合选股有值
     */
    private String logicDescription;
    /**
     * 选股算法文件对应的路径，目前新建时产生的.py文件的路径即为算法id
     */
    private String algorithmFilePath;
    /**
     * 算法完成状态: "todo" | "done"
     */
    private String status;
    private String createdAt;
    private String author;
}
