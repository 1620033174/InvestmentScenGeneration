package org.spring.finance.entity.vo;

import lombok.Data;

@Data
public class BasicAlgorithmVo {
    /**
     * 算法id
     */
    private String id;
    /**
     * 算法名称
     */
    private String name;

    private String createdAt;
    private String author;

}
