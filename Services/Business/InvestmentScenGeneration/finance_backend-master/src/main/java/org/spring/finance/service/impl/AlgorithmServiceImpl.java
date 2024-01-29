package org.spring.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spring.finance.entity.po.Algorithm;
import org.spring.finance.mapper.AlgorithmMapper;
import org.spring.finance.service.AlgorithmService;
import org.springframework.stereotype.Service;

@Service
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements AlgorithmService {
}
//public class AlgorithmServiceImpl implements AlgorithmService {
//    @Autowired
//    public AlgorithmDao algorithmDao;
//
//    public Algorithm[] searchAlgorithms(String searchType, String searchContent) {
//        return algorithmDao.searchAlgorithms(searchType,searchContent);
//    }
//
//    @Override
//    public Algorithm getAlgorithmById(String id) {
//        return algorithmDao.getAlgorithmById(id);
//    }
//
//    @Override
//    public Algorithm createNewAlgorithm(Algorithm algorithm) {
//        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date(System.currentTimeMillis());
//        System.out.println(formatter.format(date));
////    INSERT INTO algorithm(name,description,`createdAt`,`type`) VALUES('121212','111','2023-04-27 17:35:08',1);
//        return algorithmDao.createNewAlgorithm(algorithm.getName(),algorithm.getAlgorithmDescription(),algorithm.getType(),"2023-04-27 17:35:08");
//    }
//}
