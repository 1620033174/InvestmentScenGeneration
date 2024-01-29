package org.spring.finance.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.hz.Res;
import org.spring.finance.entity.hz.StockDataTest;
import org.spring.finance.entity.po.*;
import org.spring.finance.entity.vo.AlgorithmVo;
import org.spring.finance.entity.vo.SceneVO;
import org.spring.finance.service.*;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scene11")
public class SceneController {
    @Autowired
    private SceneService sceneService;

    @Autowired
    private SceneAlgorithmService sceneAlgorithmService;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private AlgorithmFactor1Service algorithmFactor1Service;

    @Autowired
    private AlgorithmFactor2Service algorithmFactor2Service;

    @Autowired
    private FactorHZService factorHZService;

    @Autowired
    private StockDataTestService stockDataTestService;

    @Autowired
    private StockDataTestService stockDataTestServiceCount;

    @Autowired
    private StockDataTestService stockDataTestServiceLimit;

    @Autowired
    private ScenePictureService scenePictureService;

    @Autowired
    private PictureService PictureService;
    //获取场景表、场景-画像关联表
    @GetMapping("getByPage")
    public Result<Page<SceneVO>> getByPage(int page, int pageSize, String name){
        Page<Scene> pageInfo = new Page<>(page,pageSize);
        QueryWrapper<Scene> queryWrapper = new QueryWrapper<>();
        if (""!=name&&name!=null){
            queryWrapper.like("scene_name",name);
        }
        sceneService.page(pageInfo, queryWrapper);
        sceneService.list(queryWrapper);
        // 创建一个新的 Page<SceneVO> 对象用于存储转换后的数据
        Page<SceneVO> pageVO = new Page<>(page, pageSize);
        List<SceneVO> sceneVOList = new ArrayList<>();
        pageVO.setTotal(pageInfo.getTotal());
        // 遍历每个 Scene 对象，进行转换并添加到新的 Page<SceneVO> 中
        for (Scene scene : pageInfo.getRecords()) {
            SceneVO sceneVO = new SceneVO();
            sceneVO.setId(scene.getId());
            sceneVO.setSceneName(scene.getSceneName());
            sceneVO.setTheme(scene.getTheme());
            sceneVO.setDescription(scene.getDescription());
            // 解析 tags、stockList 字段，将逗号分隔的标签转换为 List<String>
            String[] tagsArray = scene.getTags().split(",");
//            String[] stocksArray = scene.getStockList().split(",");
            sceneVO.setTags(Arrays.asList(tagsArray));
            //sceneVO.setStockList(Arrays.asList(stocksArray));
            //获取算法列表
            QueryWrapper<SceneAlgorithm> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("scene_id",scene.getId());
            List<SceneAlgorithm> sceneAlgorithmList = sceneAlgorithmService.list(queryWrapper1);
            List<Algorithm> algorithmList = new ArrayList<>();
            for (SceneAlgorithm sceneAlgorithm : sceneAlgorithmList) {
                Algorithm algorithm = new Algorithm();
                algorithm.setId(String.valueOf(sceneAlgorithm.getAlgorithmId()));
                //根据获取的算法id获取算法信息
                QueryWrapper<Algorithm> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",sceneAlgorithm.getAlgorithmId());
                Algorithm algorithm1 = algorithmService.getOne(queryWrapper2);
                algorithm.setName(algorithm1.getName());
                //添加算法信息到算法列表
                algorithm.setAlgorithmDescription(algorithm1.getAlgorithmDescription());
                algorithm.setLogicDescription(algorithm1.getLogicDescription());
                algorithm.setType(algorithm1.getType());
                algorithm.setAlgorithmFilePath(algorithm1.getAlgorithmFilePath());
                algorithm.setStatus(algorithm1.getStatus());
                algorithm.setCreatedAt(algorithm1.getCreatedAt());
                algorithm.setAuthor(algorithm1.getAuthor());
                algorithmList.add(algorithm);
            }
            //获取画像列表
            QueryWrapper<ScenePicture> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("scene_id",scene.getId());
            List<ScenePicture> scenePictureList = scenePictureService.list(queryWrapper2);
            List<Picture> pictureList = new ArrayList<>();
            for(ScenePicture scenePicture : scenePictureList){
                Picture picture = new Picture();
                picture.setId(scenePicture.getPictureId());
                //根据pictureId获取picture信息
                QueryWrapper<Picture> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id",scenePicture.getPictureId());
                Picture picture1 = PictureService.getOne(queryWrapper3);
                picture.setName(picture1.getName());
                //添加picture信息到picture列表
                pictureList.add(picture);
            }
            sceneVO.setPictureList(pictureList);
            sceneVO.setAlgorithmList(algorithmList);
            sceneVOList.add(sceneVO);
        }
        pageVO.setRecords(sceneVOList);
        return Result.success(pageVO);
    }
    //通过场景id获取场景表、场景-算法关联表、场景-画像关联表
    @GetMapping("getById/{id}")
    public Result<SceneVO> getById(@PathVariable("id")Integer id){
        //根据场景id获取场景表
        Scene scene = sceneService.getById(id);
        SceneVO sceneVO = new SceneVO();
        sceneVO.setId(scene.getId());
        sceneVO.setSceneName(scene.getSceneName());
        sceneVO.setTheme(scene.getTheme());
        sceneVO.setDescription(scene.getDescription());
        // 解析 tags、stockList 字段，将逗号分隔的标签转换为 List<String>
        String[] tagsArray = scene.getTags().split(",");
        if(scene.getStockList()!=null) {
            String[] stocksArray = scene.getStockList().split(",");
            sceneVO.setStockList(Arrays.asList(stocksArray));
        }
        sceneVO.setTags(Arrays.asList(tagsArray));

        //获取算法列表
        QueryWrapper<SceneAlgorithm> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("scene_id",scene.getId());
        List<SceneAlgorithm> sceneAlgorithmList = sceneAlgorithmService.list(queryWrapper1);
        List<Algorithm> algorithmList = new ArrayList<>();
        for (SceneAlgorithm sceneAlgorithm : sceneAlgorithmList) {
            Algorithm algorithm = new Algorithm();
            algorithm.setId(String.valueOf(sceneAlgorithm.getAlgorithmId()));
            //根据获取的算法id获取算法信息
            QueryWrapper<Algorithm> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("id",sceneAlgorithm.getAlgorithmId());
            Algorithm algorithm1 = algorithmService.getOne(queryWrapper2);
            algorithm.setName(algorithm1.getName());
            //添加算法信息到算法列表
            algorithm.setAlgorithmDescription(algorithm1.getAlgorithmDescription());
            algorithm.setLogicDescription(algorithm1.getLogicDescription());
            algorithm.setType(algorithm1.getType());
            algorithm.setAlgorithmFilePath(algorithm1.getAlgorithmFilePath());
            algorithm.setStatus(algorithm1.getStatus());
            algorithm.setCreatedAt(algorithm1.getCreatedAt());
            algorithm.setAuthor(algorithm1.getAuthor());
            algorithmList.add(algorithm);
        }
        sceneVO.setAlgorithmList(algorithmList);
        //获取画像列表
        QueryWrapper<ScenePicture> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("scene_id",scene.getId());
        List<ScenePicture> scenePictureList = scenePictureService.list(queryWrapper2);
        List<Picture> pictureList = new ArrayList<>();
        for(ScenePicture scenePicture : scenePictureList){
            Picture picture = new Picture();
            picture.setId(scenePicture.getPictureId());
            //根据pictureId获取picture信息
            QueryWrapper<Picture> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("id",scenePicture.getPictureId());
            Picture picture1 = PictureService.getOne(queryWrapper3);
            picture.setName(picture1.getName());
            //添加picture信息到picture列表
            pictureList.add(picture);
        }
        sceneVO.setPictureList(pictureList);
        return Result.success(sceneVO);
    }


    //增加场景表、场景-算法关联表、场景-画像关联表
    @PostMapping("add")
    public Result<Void> add(@RequestBody SceneVO sceneVO) {
        System.out.println("add");
        Scene scene = new Scene();
        scene.setSceneName(sceneVO.getSceneName());
        scene.setTheme(sceneVO.getTheme());
        scene.setDescription(sceneVO.getDescription());
        String tags = "";
        for (String tag : sceneVO.getTags()) {
            tags = tags + tag + ",";
        }
        scene.setTags(tags);
        //处理stockList
        String stocks = "";
        for (String stock : sceneVO.getStockList()) {
            stocks = stocks + stock + ",";
        }
        scene.setStockList(stocks);
        sceneService.save(scene);
        //添加场景-算法关联表
        for (Algorithm algorithm : sceneVO.getAlgorithmList()) {
            SceneAlgorithm sceneAlgorithm = new SceneAlgorithm();
            sceneAlgorithm.setSceneId(scene.getId());
            sceneAlgorithm.setAlgorithmId(Integer.valueOf(algorithm.getId()));
            sceneAlgorithmService.save(sceneAlgorithm);
        }

        //添加场景-画像关联表
        for (Picture picture : sceneVO.getPictureList()) {
            ScenePicture scenePicture = new ScenePicture();
            scenePicture.setSceneId(scene.getId());
            scenePicture.setPictureId(picture.getId());
            scenePictureService.save(scenePicture);
        }
        return Result.success();
    }

    //根据场景id删除场景表、场景-算法关联表、场景-画像关联表
    @DeleteMapping("deleteById/{id}")
    public Result<Void> deleteScene(@PathVariable("id")Integer id){
        //删除场景表
        QueryWrapper<Scene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        sceneService.remove(queryWrapper);
        //删除场景-算法关联表
        QueryWrapper<SceneAlgorithm> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("scene_id",id);
        sceneAlgorithmService.remove(queryWrapper1);
        //删除场景-画像关联表
        QueryWrapper<ScenePicture> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("scene_id",id);
        scenePictureService.remove(queryWrapper2);
        return Result.success();
    }

    //根据id更新场景表，删除场景-算法关联表，添加新的关联表
    @PostMapping("updateById")
    public Result<Void> updateById(@RequestBody SceneVO sceneVO){
        //更新场景表
        Scene scene = new Scene();
        scene.setId(sceneVO.getId());
        scene.setSceneName(sceneVO.getSceneName());
        scene.setTheme(sceneVO.getTheme());
        scene.setDescription(sceneVO.getDescription());
        String tags = "";
        for (String tag : sceneVO.getTags()) {
            tags = tags + tag + ",";
        }
        scene.setTags(tags);
        String stocks = "";
        for (String stock : sceneVO.getStockList()) {
            stocks = stocks + stock + ",";
        }
        scene.setStockList(stocks);
        sceneService.updateById(scene);
        //删除场景-算法关联表并重新导入场景-算法关联
        QueryWrapper<SceneAlgorithm> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("scene_id",sceneVO.getId());
        sceneAlgorithmService.remove(queryWrapper);
        for (Algorithm algorithm : sceneVO.getAlgorithmList()) {
            SceneAlgorithm sceneAlgorithm = new SceneAlgorithm();
            sceneAlgorithm.setSceneId(sceneVO.getId());
            sceneAlgorithm.setAlgorithmId(Integer.valueOf(algorithm.getId()));
            sceneAlgorithmService.save(sceneAlgorithm);
        }
        //删除场景-画像关联表并重新导入场景-画像关联表
        QueryWrapper<ScenePicture> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("scene_id",sceneVO.getId());
        scenePictureService.remove(queryWrapper1);
        for (Picture picture : sceneVO.getPictureList()) {
            ScenePicture scenePicture = new ScenePicture();
            scenePicture.setSceneId(sceneVO.getId());
            scenePicture.setPictureId(picture.getId());
            scenePictureService.save(scenePicture);
        }
        return Result.success();
    }

    //接收参数为id，根据算法id返回算法股票数据
    @GetMapping("getStockDataById")
    public Result<JSONObject> getStockDataById(@RequestParam Integer id) {
        List<AlgorithmVo> algorithmVoList = new ArrayList<>();
        if (id == null) {
            return Result.success(new JSONObject());
        }
        //获取算法Volist
        // 根据id获取算法
        Algorithm algorithm = algorithmService.getById(id);
        AlgorithmVo algorithmVo = new AlgorithmVo();
        if (Objects.equals(algorithm.getType(), "1")) {
            // 如果是1条件选股,则在factor1表中继续搜索
            QueryWrapper<AlgorithmFactor1> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("algorithm_id", algorithm.getId());
            List<AlgorithmFactor1> algorithmFactor1List = algorithmFactor1Service.list(queryWrapper);
            // 复制相同属性
            BeanUtil.copyProperties(algorithm, algorithmVo);
            // 设置因子列表
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
            // todo 综合选股的选股部分逻辑未写
            QueryWrapper<AlgorithmFactor2> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("algorithm_id", algorithm.getId());
            List<AlgorithmFactor2> algorithmFactor2List = algorithmFactor2Service.list((queryWrapper));
//                List<String> ids = algorithmFactor2List.stream().map(AlgorithmFactor2::getFactorId).collect(Collectors.toList());
//                List<FactorHZ> factors = new ArrayList<>(factorHZService.listByIds(ids));
//                BeanUtil.copyProperties(algorithm, algorithmVo);
//                // 利用第三方工具库hutool简化代码
//                algorithmVo.setFactors(BeanUtil.copyToList(factors, AlgorithmVo.FactorHZ.class));
        }
        algorithmVoList.add(algorithmVo);
        //查询股票数据返回json格式数据.
        //假设有两个算法，算法1选出股票代码为000001，000002，算法2选出股票代码为000003，000004
        //则返回的json格式数据为data:{colunms:[{prop:name,label:"股票名称"},{prop:code,label:"股票代码"},{prop:attribute,label:"对应的属性名"}],rows:[{name:"平安银行",code:"000001"},{name:"万科A",code:"000002"},{name:"中国平安",code:"000003"},{name:"中国人寿",code:"000004"}]}
//前端根据返回的json数据进行表格渲染
        JSONObject res;
        try {
            List<StockDataTest> stockDataTestList = getStockEntity(algorithmVo);
            if(stockDataTestList==null){
                return Result.success(new JSONObject());
            }

            String[] factorName_ = getFactorName_(algorithmVo);
            String[] factorNameMax = getFactorNameMax(factorName_);
            res = generateJson(stockDataTestList, factorName_, factorNameMax);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Result.success(res);
    }
    private static JSONObject createColumn(String prop, String label) {
        JSONObject column = new JSONObject();
        column.put("prop", prop);
        column.put("label", label);
        return column;
    }
    //构造目标json格式
    public JSONObject generateJson(List<StockDataTest> stockDataTestList, String[] factorname_,String[] factornameMax) {
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONArray columns = new JSONArray();
        //factorname是[a_b这种]
        // 遍历factorname，根据factor的id获取中文名称
        for (String factor : factorname_) {
            String label = "";
            String prop = "";
            if(factor.equals("id")){
               continue;
            }
            if(factor.equals("code")){
                label = "股票代码";
                prop = "code";
            }else{
                // 通过factor的英文名称从factorHZ表中获取中文名称
                label = getFactorNameByfactorNameUS(factor);
                prop = factor; // 使用factor作为prop
            }

            JSONObject column = new JSONObject();
            column.put("prop", prop);
            column.put("label", label);
            columns.add(column);
        }
        JSONArray rows = new JSONArray();
        for (StockDataTest stockData : stockDataTestList) {
            JSONObject row = new JSONObject();
            int i = 1;
            for (String factor : factornameMax) {

                if(factor.equals("id")){
                    continue;
                }
                String prop = factorname_[i++];
                //这里的factorname需要是属性值，也就是aB这种
                String value = getPropertyValue(stockData, factor); // 获取StockDataTest对象中对应属性的值
                row.put(prop, value);
            }
            rows.add(row);
        }

        data.put("columns", columns);
        data.put("rows", rows);
        result.put("data", data);
        return result;
    }
    // 由英文名称得到中文名称
    private String getFactorNameByfactorNameUS(String factorNameUS) {
        //实现根据英文名称查询中文名称
        String factorNameCN = factorHZService.getOne(new QueryWrapper<FactorHZ>().eq("name_us", factorNameUS)).getName();
        return factorNameCN; // 示例中简单返回一个固定值
    }

    // 获取StockDataTest对象中对应属性的值
    private String getPropertyValue(StockDataTest stockData, String propertyName) {
        //根据stockData对象和属性名，利用反射获取属性值
        Object propertyValue = getPropertyValueByReflect(stockData, propertyName);
        return propertyValue.toString(); // 示例中简单返回空字符串
    }

    // 使用反射获取属性值
    public static Object getPropertyValueByReflect(Object object, String propertyName) {
        try {
            // 获取对象的Class对象
            Class<?> clazz = object.getClass();

            // 使用反射获取属性对象
            Field field = clazz.getDeclaredField(propertyName);

            // 设置属性对象可访问（如果是私有属性）
            field.setAccessible(true);

            // 获取属性的值
            Object value = field.get(object);

            return value;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    //得到因子名的英文名数组，格式为a_b
    public String[] getFactorName_(AlgorithmVo algorithmVO) {
        //1、根据算法查询获得因子，根据因子获取
        int len = algorithmVO.getFactors().size();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        String[] factorNameStrings = new String[len+2];
        factorNameStrings[0] = "id";
        factorNameStrings[1] = "code";
        int index = 2;
        for (AlgorithmVo.FactorHZ factor : list) {
            //得到因子名称
            String factorNameUs = factorHZService.getById(factor.getId()).getNameUs();
            factorNameStrings[index++] = factorNameUs;
        }
        return factorNameStrings;
    }
    //得到因子名的英文名数组，格式为驼峰格式
    public String[] getFactorNameMax(String[] factorNameStrings) {
        String[] getFactorNameMax = new String[factorNameStrings.length];
        for(int i = 0; i <factorNameStrings.length ; i++) {
            getFactorNameMax[i] = getFactorname(factorNameStrings[i]);
        }
        return getFactorNameMax;
    }
    public List<StockDataTest> getStockEntity(AlgorithmVo algorithmVO) throws Exception {
        if(algorithmVO==null){
            return null;
        }
        //1、根据算法查询获得因子，根据因子获取
        int len = algorithmVO.getFactors().size();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        QueryWrapper<StockDataTest> queryWrapper = new QueryWrapper<>();
        QueryWrapper<StockDataTest> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<StockDataTest> queryWrapper2 = new QueryWrapper<>();
        //先假定数据集为2020年
        queryWrapper.eq("stadate","2020/9/30");
        queryWrapper1.eq("stadate","2020/9/30");
        String[] factorNameStrings = new String[len+2];
        factorNameStrings[0] = "id";
        factorNameStrings[1] = "code";
        int index = 2;
        //表示是否存在数字级别的限制。
        int label = 0;
        for (AlgorithmVo.FactorHZ factor : list) {
            //得到因子名称
            String factorNameUs = factorHZService.getById(factor.getId()).getNameUs();
            String factorNameUs0 = factorNameUs + "+0";
            String logic = factor.getLogic();
            int choiceType = factor.getChoiceType();
            double value = Double.parseDouble(factor.getValue());
            int isTop = factor.getIsTop();
            System.out.println("喜喜");
            System.out.println(value);
            //先选因子列
            factorNameStrings[index++] = factorNameUs;
            //queryWrapper.select(factorNameUs);
            //0：百分比 ，需要提前查询其值
            if(choiceType==0){
                //从大到小
                if(isTop==1){
                    //获取限制的数量
                    long count = stockDataTestServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByDesc(factorNameUs0);
                    System.out.println(limit);
                    queryWrapper2.last("limit 0,"+limit);
                    List<StockDataTest> listLimit = stockDataTestServiceLimit.list(queryWrapper2);
                    StockDataTest stockData = listLimit.get(listLimit.size() - 1);
                    String factorname_get = getFactorname(factorNameUs);
                    //反射获取get方法得到对应因子值
                    Object ob = getGetMethod(stockData, factorname_get);
                    System.out.println(ob);
                    //拼接where条件
                    queryWrapper.ge(factorNameUs0,ob);

                }else{
                    //获取限制的数量
                    long count = stockDataTestServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByAsc(factorNameUs0);
                    queryWrapper2.last("limit 0,"+limit);
                    List<StockDataTest> listLimit = stockDataTestServiceLimit.list(queryWrapper2);
                    StockDataTest stockData = listLimit.get(listLimit.size() - 1);
                    String factorname_get = getFactorname(factorNameUs);
                    //反射获取get方法得到对应因子值
                    Object ob = getGetMethod(stockData, factorname_get);
                    System.out.println(ob);
                    //拼接where条件
                    queryWrapper.le(factorNameUs0,ob);
                }
            } else if (choiceType == 2) {
                //2：大于小于
                if(logic.equals("大于等于")){
                    queryWrapper.ge(factorNameUs0,value);
                } else if (logic.equals("小于等于")) {
                    queryWrapper.le(factorNameUs0,value);
                }else if(logic.equals("大于")) {
                    queryWrapper.gt(factorNameUs0,value);
                }else if(logic.equals("小于")) {
                    queryWrapper.lt(factorNameUs0,value);
                }else if(logic.equals("等于")) {
                    queryWrapper.eq(factorNameUs0,value);
                }
            }else {
                //1：数字 分大到小和从小到大
                label = 1;
                if(isTop==1) {
                    queryWrapper.orderByDesc(factorNameUs0);
                    queryWrapper.last("limit 0," + (int)value);
                }else{
                    queryWrapper.orderByAsc(factorNameUs0);
                    queryWrapper.last("limit 0," + (int)value);
                }
            }
        }
        if(label == 0){
            queryWrapper.last("limit 0," + 10);
        }
        queryWrapper.select(factorNameStrings);
        List<StockDataTest> list1 = stockDataTestService.list(queryWrapper);

        return list1;
    }
    //将因子名由格式_转为驼峰格式
    private String getFactorname(String factorNameUs) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("");
        char[] chars = factorNameUs.toCharArray();
        for (int i = 0; i <chars.length; i++) {
            if(chars[i]=='_'){

                chars[i+1] = Character.toUpperCase(chars[i+1]);
            }else{
                stringBuilder.append(chars[i]);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 根据属性，获取get方法
     * @param ob 对象
     * @param name 属性名
     * @return
     * @throws Exception
     */
    public static Object getGetMethod(Object ob , String name)throws Exception {
        Method[] m = ob.getClass().getMethods();
        for (int i = 0; i < m.length; i++) {
            if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
                return m[i].invoke(ob);
            }
        }
        return null;
    }
}
