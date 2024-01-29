package org.spring.finance.entity.hz;

import lombok.Data;

@Data
public class FactorAnalysis {
    private String factorname;
    private String missing;
    private double variance;
    private double standard_deviation;
    private double correlation;
}
