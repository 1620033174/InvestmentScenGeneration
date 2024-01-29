package org.spring.finance.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data

public class AlgorithmFactor2 {
    @TableId(type = IdType.AUTO)
    private String id;
    private String factorId;
    private String algorithmId;
}


