package org.spring.finance.entity.hz;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PictureList {
    private List<Double> variance_list = new ArrayList<>();
    private List<Double> standard_deviation_list = new ArrayList<>();
    private List<Double> mean_list = new ArrayList<>();
    private List<String> date_list = new ArrayList<>();
}
