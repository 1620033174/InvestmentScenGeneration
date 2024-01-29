package org.spring.finance.entity.hz;

import lombok.Data;

@Data
public class StockMaxOrMin {
    private String code;
    private String stock_name;
    private String value;
}
