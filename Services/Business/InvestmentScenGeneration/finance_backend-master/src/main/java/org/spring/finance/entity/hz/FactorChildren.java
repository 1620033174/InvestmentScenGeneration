package org.spring.finance.entity.hz;

import lombok.Data;

@Data
public class FactorChildren {
    private int id;

    private String value;//因子英文名称

    private String label;//因子中文名称

    private String formula;

    private String description;

    private double defaultValue;

    private double maxValue;

    private double minValue;

    private double accuracy;
}
