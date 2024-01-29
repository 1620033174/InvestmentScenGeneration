package org.spring.finance.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class AlgorithmVo {
    /**
     * 算法id:新建算法时没有这一项
     */
    private String id;
    /**
     * 算法名称
     */
    private String name;
    /**
     * 仅综合选股
     */
    private List<FactorHZ> factors;
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
     * 选股算法文件对应的路径
     */
    private String algorithmFilePath;
    private String status;
    private String createdAt;
    private String author;

    @Data
    public static class FactorHZ {
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
        //逻辑描述
        private String logic;
        private String value;
        private int isTop;
        private int choiceType;
    }
}

