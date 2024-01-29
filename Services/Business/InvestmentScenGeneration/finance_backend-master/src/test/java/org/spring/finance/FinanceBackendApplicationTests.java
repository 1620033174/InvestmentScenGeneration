package org.spring.finance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DecimalFormat;

@SpringBootTest
class FinanceBackendApplicationTests {

    @Test
    void contextLoads() {
        double f = 111231.5585;
        DecimalFormat df = new DecimalFormat("#.00");
        System.out.println(df.format(f));
    }

}
