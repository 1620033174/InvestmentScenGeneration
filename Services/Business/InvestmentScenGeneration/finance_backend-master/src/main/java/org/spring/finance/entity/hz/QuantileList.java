package org.spring.finance.entity.hz;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuantileList {
    private List<Double> min_list = new ArrayList<>();
    private List<Double> max_list = new ArrayList<>();
    private List<Double> two_list = new ArrayList<>();
    private List<Double> three_list = new ArrayList<>();
    private List<Double> four_list = new ArrayList<>();
    private List<String> date_list = new ArrayList<>();
}
