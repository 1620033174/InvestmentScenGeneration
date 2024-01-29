package org.spring.finance.mapper.hz;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.spring.finance.entity.hz.StockData;

import java.util.List;

@Mapper
public interface StockDataMapper extends BaseMapper<StockData> {

    @Select("<script>"
            +"select * from stock_data where code in("
            + "<foreach item='code' index='index' collection='codes'      open='(' separator=',' close=')'>"
            + "#{code}"
            + "</foreach>"
            +")"
            + "</script>")
    List<StockData> getUserList(List<String> codes);

}
