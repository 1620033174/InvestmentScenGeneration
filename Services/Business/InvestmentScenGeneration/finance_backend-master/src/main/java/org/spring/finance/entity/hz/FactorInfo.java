package org.spring.finance.entity.hz;

import lombok.Data;

import java.util.List;

@Data
public class FactorInfo {
    private String label;//因子类型
    private String value;//因子类型
    private List<FactorChildren> children;
}
