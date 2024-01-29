package org.spring.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.po.Algorithm;
import org.spring.finance.entity.po.AlgorithmFactor1;
import org.spring.finance.entity.po.AlgorithmFactor2;
import org.spring.finance.entity.vo.AlgorithmVo;
import org.spring.finance.entity.vo.BasicAlgorithmVo;
import org.spring.finance.service.AlgorithmFactor1Service;
import org.spring.finance.service.AlgorithmFactor2Service;
import org.spring.finance.service.AlgorithmService;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lwz/algorithm")
//@CrossOrigin(origins = "http://localhost:9528")
@CrossOrigin(origins = "*")
public class AlgorithmRestController {
    @Autowired
    private AlgorithmService algorithmService_lwz;
    @Autowired
    private AlgorithmFactor1Service algorithmFactors1Service;

    @Autowired
    private AlgorithmFactor1Service algorithmFactors1ServiceDelete;

    @Autowired
    private AlgorithmFactor2Service algorithmFactor2Service;

    @Autowired
    private AlgorithmFactor2Service algorithmFactor2ServiceDelete;

    @Autowired
    private FactorHZService factorHZService;

    @GetMapping("list")
    public Result<List<BasicAlgorithmVo>> getAll(@Param("searchType") String searchType, @Param("searchContent") String searchContent
            , @Param("algorithmStatus") String algorithmStatus) {
        QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
        if (Objects.equals(searchType, "name")) {
            //根据name模糊搜索
            queryWrapper.like("name", searchContent);
        } else if (Objects.equals(searchType, "id")) {
            //根据id精确搜索
            queryWrapper.eq("id", searchContent);
        }
//       处理algorithmStatus字段
        if (!Objects.equals(algorithmStatus, "")) {
            queryWrapper.eq("status", algorithmStatus);
        }
        List<Algorithm> list = algorithmService_lwz.list(queryWrapper);
//        System.out.println("-----------------------");
//        System.out.println(list);
//        hutools简化代码
        return Result.success(BeanUtil.copyToList(list, BasicAlgorithmVo.class));
    }

    @GetMapping("get")
    public Result<Object> getById(@Param("id") Integer id) {
        Algorithm algorithm = algorithmService_lwz.getById(id); //*
        AlgorithmVo algorithmVo = new AlgorithmVo();
        if (Objects.equals(algorithm.getType(), "1")) {
            // 如果是1条件选股,则在factor1表中继续搜索
            QueryWrapper<AlgorithmFactor1> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("algorithm_id", algorithm.getId());
            List<AlgorithmFactor1> algorithmFactor1List = algorithmFactors1Service.list(queryWrapper);
            BeanUtil.copyProperties(algorithm, algorithmVo); // 复制相同属性
            // 逐个复制特有的属性
            algorithmVo.setFactors(
                    algorithmFactor1List.stream().map(
                            cur -> {
                                AlgorithmVo.FactorHZ factor = new AlgorithmVo.FactorHZ();

                                int id_int = Integer.parseInt(cur.getFactorId());
                                factor.setId(id_int);
                                factor.setName(factorHZService.getById(id_int).getName());
                                factor.setType(factorHZService.getById(id_int).getType());
                                factor.setFormula(factorHZService.getById(id_int).getFormula());
                                factor.setDescription(factorHZService.getById(id_int).getDescription());
                                factor.setNameUs(factorHZService.getById(id_int).getNameUs());
                                factor.setDefaultValue(factorHZService.getById(id_int).getDefaultValue());
                                factor.setMaxValue(factorHZService.getById(id_int).getMaxValue());
                                factor.setMinValue(factorHZService.getById(id_int).getMinValue());
                                factor.setAccuracy(factorHZService.getById(id_int).getAccuracy());

                                factor.setLogic(cur.getLogic());
                                factor.setValue(cur.getValue());
                                factor.setIsTop(cur.getIsTop());
                                factor.setChoiceType(cur.getChoiceType());
                                return factor;
                            }
                    ).collect(Collectors.toList())
            );
        } else {
            // 如果是2综合选股,则在factor2表中继续搜索
            QueryWrapper<AlgorithmFactor2> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("algorithm_id", algorithm.getId());
            List<AlgorithmFactor2> algorithmFactor2List = algorithmFactor2Service.list((queryWrapper));
            List<String> ids = algorithmFactor2List.stream().map(AlgorithmFactor2::getFactorId).collect(Collectors.toList());
            List<FactorHZ> factors = new ArrayList<>(factorHZService.listByIds(ids));
            BeanUtil.copyProperties(algorithm, algorithmVo);
            // 利用第三方工具库hutool简化代码
            algorithmVo.setFactors(BeanUtil.copyToList(factors, AlgorithmVo.FactorHZ.class));
        }
        return Result.success(algorithmVo);
    }

    @PostMapping("save")
    public Result<String> save(@RequestBody AlgorithmVo algorithmVO) {
        Algorithm algorithm = new Algorithm();
        BeanUtil.copyProperties(algorithmVO, algorithm);
        algorithm.setCreatedAt(DateTime.now().toString()); // 插入创建时的时间
        algorithm.setAuthor("高仁");
        if (Objects.equals(algorithmVO.getType(), "1")) {
            algorithm.setStatus("done"); // 条件选股由业务人员直接配置好
        } else {
            algorithm.setStatus("todo"); // 综合选股还需要由技术人员进行后续开发
        }
        algorithmService_lwz.save(algorithm);
        // 更新AlgorithmFilePath字段
        algorithm.setAlgorithmFilePath(algorithm.getId());
        algorithmService_lwz.updateById(algorithm);
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        String logicAll = "";
        if (Objects.equals(algorithmVO.getType(), "1")) {

            String logic = "";
            for (AlgorithmVo.FactorHZ factor : list) {
                AlgorithmFactor1 savedFactor = new AlgorithmFactor1();
                savedFactor.setAlgorithmId(algorithm.getId());
                savedFactor.setFactorId(String.valueOf(factor.getId()));
                //新增更新逻辑和值
                savedFactor.setLogic(factor.getLogic());
                savedFactor.setValue(factor.getValue());
                savedFactor.setIsTop(factor.getIsTop());
                savedFactor.setChoiceType(factor.getChoiceType());

                //根据因子id查询factorHZ表得到因子中文名称
                String factorNameCN = factorHZService.getById(factor.getId()).getName();
                logic = getLogicDes(factorNameCN,factor.getChoiceType(),factor.getIsTop(),factor.getLogic(),factor.getValue());

                if(logicAll.equals("")){
                    logicAll = logic;
                }else{
                    logicAll = logicAll + "，"+ logic;
                }
                algorithmFactors1Service.save(savedFactor);
            }
            algorithm.setLogicDescription(logicAll);
            algorithmService_lwz.updateById(algorithm);
        } else {
            //todo add 综合选股的选股逻辑 到代码注释中
            for (AlgorithmVo.FactorHZ factor : list) {
                AlgorithmFactor2 savedFactor = new AlgorithmFactor2();
                savedFactor.setAlgorithmId(algorithm.getId());
                savedFactor.setFactorId(String.valueOf(factor.getId()));
                algorithmFactor2Service.save((savedFactor));
            }
        }
        return Result.success(algorithm.getId());
    }
    //todo update 综合选股 的logic描述
    @PostMapping("update")
    public Result<String> update(@RequestBody AlgorithmVo algorithmVO) {
//        目前仅2综合选股需要支持update接口, 且仅能修改算法描述字段
        Algorithm algorithm = new Algorithm();
        BeanUtil.copyProperties(algorithmVO, algorithm);
        System.out.println(algorithmVO);
        System.out.println(algorithm);
        //算法表更新
        algorithmService_lwz.updateById(algorithm);
        //算法因子关系表更新。
        String type = algorithmVO.getType();
        String algorithmId = algorithmVO.getId();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        String logicAll = "";
        if(type.equals("1")){
            //条件选股
            //先删除算法因子关系表中得记录
            QueryWrapper<AlgorithmFactor1> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("algorithm_id",algorithmId);
            algorithmFactors1ServiceDelete.remove(queryWrapper);
            String logic = "";
            for (AlgorithmVo.FactorHZ factor : list) {
                AlgorithmFactor1 savedFactor = new AlgorithmFactor1();
                savedFactor.setAlgorithmId(algorithm.getId());
                savedFactor.setFactorId(String.valueOf(factor.getId()));
                savedFactor.setLogic(factor.getLogic());
                savedFactor.setValue(factor.getValue());
                savedFactor.setIsTop(factor.getIsTop());
                savedFactor.setChoiceType(factor.getChoiceType());
                //根据因子id查询factorHZ表得到因子中文名称
                String factorNameCN = factorHZService.getById(factor.getId()).getName();
                logic = getLogicDes(factorNameCN,factor.getChoiceType(),factor.getIsTop(),factor.getLogic(),factor.getValue());

                if(logicAll.equals("")){
                    logicAll = logic;
                }else{
                    logicAll = logicAll + "，"+ logic;
                }
                algorithmFactors1Service.save(savedFactor);
            }
            algorithm.setLogicDescription(logicAll);
            algorithmService_lwz.updateById(algorithm);
        }else{
            QueryWrapper<AlgorithmFactor2> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("algorithm_id",algorithmId);
            algorithmFactor2ServiceDelete.remove(queryWrapper);
            for (AlgorithmVo.FactorHZ factor : list) {
                AlgorithmFactor2 savedFactor = new AlgorithmFactor2();
                savedFactor.setAlgorithmId(algorithm.getId());
                savedFactor.setFactorId(String.valueOf(factor.getId()));
                algorithmFactor2Service.save(savedFactor);
            }
        }
        return Result.success("success");
    }

    //获取全部算法
    @GetMapping("getAlgorithmByPage")
    public Result<Page<Algorithm>> getAlgorithmByPage(int page, int pageSize, String name,String type) {
        Page<Algorithm> pageInfo = new Page<>(page,pageSize);
        QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
        if (""!=name&&name!=null){
            queryWrapper.like("name",name);
        }
        if(type.equals("1")){
            queryWrapper.eq("type",Integer.parseInt(type));
            algorithmService_lwz.page(pageInfo, queryWrapper);
        } else if (type.equals("2")) {
            queryWrapper.eq("type",Integer.parseInt(type));
            algorithmService_lwz.page(pageInfo, queryWrapper);
        }else{
            algorithmService_lwz.page(pageInfo, queryWrapper);
        }

        return Result.success(pageInfo);
    }
    //获取全部算法
    @GetMapping("all")
    public Result<List<Algorithm>> getAllAlgorithm(@RequestParam String name) {
        QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
        if(name!=null){
            queryWrapper.like("name",name);
        }
        List<Algorithm> list = algorithmService_lwz.list(queryWrapper);
        return Result.success(list);
    }

    //根据id删除算法
    @DeleteMapping("deleteAlgorithmById/{id}")
    public Result<Void> deleteAlgorithmById(@PathVariable("id")Integer id){
//        System.out.println(id);
//        int id_int = Integer.parseInt(id);
        QueryWrapper<Algorithm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        String type = algorithmService_lwz.getById(id).getType();
        algorithmService_lwz.remove(queryWrapper);
//        System.out.println(type);
        QueryWrapper<AlgorithmFactor1> queryWrapperAlgorithmFactor1 = new QueryWrapper<>();
        QueryWrapper<AlgorithmFactor2> queryWrapperAlgorithmFactor2 = new QueryWrapper<>();

        if (Objects.equals(type, "1")) {
            queryWrapperAlgorithmFactor1.eq("algorithm_id",id);
            algorithmFactors1Service.remove(queryWrapperAlgorithmFactor1);
        }else{
            queryWrapperAlgorithmFactor2.eq("algorithm_id",id);
            algorithmFactor2Service.remove(queryWrapperAlgorithmFactor2);
        }

        return Result.success();
    }


    //根据选股算法条件拼接逻辑描述
    private String getLogicDes(String factorNameCN, int choiceType, int isTop, String logicChoiceType, String value) {
        String logic = "";
        if (choiceType == 0) {
            //0百分比
            if (isTop == 1) {
                //前百分之多少
                logic = factorNameCN + "前" + value + "%";
            } else {
                //后百分之多少
                logic = factorNameCN + "后" + value + "%";
            }
        } else if (choiceType == 1) {
            //1数字
            if (isTop == 1) {
                //前多少
                logic = factorNameCN + "前" + value;
            } else {
                //后多少
                logic = factorNameCN + "后" + value;
            }
        } else {
            //2大于小于
            if (logicChoiceType.equals("大于")) {
                logic = factorNameCN + "大于" + value;
            } else if (logicChoiceType.equals("大于等于")) {
                logic = factorNameCN + "大于等于" + value;
            } else if (logicChoiceType.equals("小于")) {
                logic = factorNameCN + "小于" + value;
            } else if (logicChoiceType.equals("小于等于")) {
                logic = factorNameCN + "小于等于" + value;
            } else {
                logic = factorNameCN + "等于" + value;
            }
        }
        return logic;
    }
}
