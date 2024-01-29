package org.spring.finance.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.spring.finance.entity.hz.FactorHZ;
import org.spring.finance.entity.hz.StockDataTest;
import org.spring.finance.entity.po.*;
import org.spring.finance.entity.vo.AlgorithmVo;
import org.spring.finance.entity.vo.SceneVO;
import org.spring.finance.service.*;
import org.spring.finance.service.hz.FactorHZService;
import org.spring.finance.utils.HttpClientUtil;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scene")
public class SceneControllerReal {
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
    private StockService stockService;

    @Autowired
    private StockService stockServiceCount;

    @Autowired
    private StockService stockServiceLimit;

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
//        System.out.println("add");
        Scene scene = new Scene();
        scene.setSceneName(sceneVO.getSceneName());
        scene.setTheme(sceneVO.getTheme());
        scene.setDate(sceneVO.getDate());
        scene.setDescription(sceneVO.getDescription());
        String tags = "";
        for (String tag : sceneVO.getTags()) {
            tags = tags + tag + ",";
        }
        scene.setTags(tags);
        //处理stockList
        String stocks = "";
        List<String> stock_list = new ArrayList<>();
        for (String stock : sceneVO.getStockList()) {
            stock_list.add(stock);
            //stocks = stocks + stock + ",";
        }
        if(stock_list.size()>10){
            stock_list = createRandoms(stock_list,10);
        }
        for (int i = 0; i < stock_list.size(); i++) {
            if(i==stock_list.size()-1){
                stocks = stocks + stock_list.get(i);
            }else{
                stocks = stocks + stock_list.get(i) + ",";
            }
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

    /**
     * 从集合中随机取出N个不重复的元素
     * @param list 需要被取出数据的集合
     * @param n 取出的元素数量
     * @return
     */
    private List<String> createRandoms(List<String> list, int n) {
        Map<Integer,String> map = new HashMap();
        List<String> news = new ArrayList();
        if (list.size() <= n) {
            return list;
        } else {
            while (map.size() < n) {
                int random = (int)(Math.random() * list.size());
                if (!map.containsKey(random)) {
                    map.put(random, "");
                    news.add(list.get(random));
                }
            }
            return news;
        }
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

    //接收参数为算法id，根据算法id返回算法股票数据
    @GetMapping("getStockDataById")
    public Result<JSONObject> getStockDataById(@RequestParam Integer id,@RequestParam String date) {
        List<AlgorithmVo> algorithmVoList = new ArrayList<>();
//        if (id == null) {
//            return Result.success(new JSONObject());
//        }
        //获取算法Volist
        // 根据id获取算法
        Algorithm algorithm = algorithmService.getById(id);
        AlgorithmVo algorithmVo = new AlgorithmVo();
        int type = 0;
        if (Objects.equals(algorithm.getType(), "1")) {
            // 如果是1条件选股,则在factor1表中继续搜索
            type = 1;
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
            String SERVER_URL = "http://localhost:5000";
            Result<Object> objectResult = new Result<>();
            String url = SERVER_URL + "//lwz/algorithm/file/runZonghe";
                //组装参数
                HashMap<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                //发送请求
                String s = HttpClientUtil.doGet(url, map);
//                System.out.println(s);
                JSONObject jsonObject = JSONObject.parseObject(s);
                JSONObject res1 = new JSONObject();
                res1.put("data", jsonObject);
                return Result.success(res1);
        }
        JSONObject res = new JSONObject();
        JSONArray columns = new JSONArray();
        JSONArray rows = new JSONArray();
        List<Map<String, String>> stockEntity = null;
        JSONObject res1;
        try {
            stockEntity = getStockEntity(algorithmVo,date);
            for (String key : stockEntity.get(0).keySet()) {
                JSONObject column = new JSONObject();
                    column.put("prop", key);
                String value = getFactorNameByfactorNameUS(key);
                column.put("label", value);
                columns.add(column);
            }
            for (Map<String, String> maps : stockEntity) {
                JSONObject row = new JSONObject();
                for (Map.Entry<String, String> entry : maps.entrySet()) {
//                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    row.put(entry.getKey(), entry.getValue());
                }
                rows.add(row);
            }
            res.put("columns", sortByMe(columns));
            res.put("rows", rows);
            res1 = new JSONObject();
            res1.put("data", res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(res1);
    }
    @GetMapping("showStock")
    public Result<JSONObject> showStock(@RequestParam Integer id){
        Scene scene = sceneService.getById(id);
        String date = scene.getDate();
        date = date + "/9/30";
        String stockcode = scene.getStockList();
        List<String> stockcode_list = Arrays.asList(stockcode.split(","));
        List<Stock> stocks = new ArrayList<>();
        for (String code:stockcode_list) {
            QueryWrapper<Stock> queryWrapper5 = new QueryWrapper<>();
            queryWrapper5.eq("code",code);
            queryWrapper5.eq("date",date);
            List<Stock> stocks1 = stockService.list(queryWrapper5);
            stocks.addAll(stocks1);
        }
        List<String> factornameUs = new ArrayList<>();
        factornameUs.add("eps");
        factornameUs.add("capital_reserve_fund_per_share");
        factornameUs.add("retained_profit_per_share");
        List<Map<String, String>> maps = mergeStockData_specila_factor(stocks,factornameUs);

        JSONObject res = new JSONObject();
        JSONArray columns = new JSONArray();
        JSONArray rows = new JSONArray();
        JSONObject res1;
        try {
            for (String key : maps.get(0).keySet()) {
                JSONObject column = new JSONObject();
                column.put("prop", key);
                String value = getFactorNameByfactorNameUS(key);
                column.put("label", value);
                columns.add(column);
            }
            for (Map<String, String> map : maps) {
                JSONObject row = new JSONObject();
                for (Map.Entry<String, String> entry : map.entrySet()) {
//                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    row.put(entry.getKey(), entry.getValue());
                }
                rows.add(row);
            }
            res.put("columns", sortByMe(columns));
            res.put("rows", rows);
            res1 = new JSONObject();
            res1.put("data", res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(res1);
    }
    @GetMapping("showStockTest")
    public Result<JSONObject> showStockTest(@RequestParam Integer id){
        Scene scene = sceneService.getById(id);
//        SceneVO sceneVO = new SceneVO();

        //获取算法列表
        QueryWrapper<SceneAlgorithm> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("scene_id",id);
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
        List<String> factor_name_list = new ArrayList<>();
        //获取所有的因子
        for (int i = 0; i < algorithmList.size(); i++) {
            if(algorithmList.get(i).getType()=="1"){
                QueryWrapper<AlgorithmFactor1> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("algorithm_id",algorithmList.get(i).getId());
                List<AlgorithmFactor1> algorithmFactor1List = algorithmFactor1Service.list(queryWrapper3);
                for (AlgorithmFactor1 algorithmfactor1:algorithmFactor1List) {
                    factor_name_list.add(algorithmfactor1.getFactorId());
                }
            }else{
                QueryWrapper<AlgorithmFactor2> queryWrapper4 = new QueryWrapper<>();
                queryWrapper4.eq("algorithm_id",algorithmList.get(i).getId());
                List<AlgorithmFactor2> algorithmFactor2List = algorithmFactor2Service.list(queryWrapper4);
                for (AlgorithmFactor2 algorithmfactor2:algorithmFactor2List) {
                    factor_name_list.add(algorithmfactor2.getFactorId());
                }
            }
        }
        //因子去重
        List<String> listNew = new ArrayList<String>(new TreeSet<String>(factor_name_list));
        List<String> factornameUs = new ArrayList<>();
        for (String factorId:listNew) {
            QueryWrapper<FactorHZ> queryWrapper6 = new QueryWrapper<>();
            queryWrapper6.eq("id",factorId);
            FactorHZ factorHZ = factorHZService.getOne(queryWrapper6);
            factornameUs.add(factorHZ.getNameUs());
        }
        
        //获取所有股票
        String date = scene.getDate();
        date = date + "/9/30";
        String stockcode = scene.getStockList();
        if(stockcode==""){
            //return Result.success(maps);
        }
        List<String> stockcode_list = Arrays.asList(stockcode.split(","));
        List<Stock> stocks = new ArrayList<>();
        for (String code:stockcode_list) {
            QueryWrapper<Stock> queryWrapper5 = new QueryWrapper<>();
            queryWrapper5.eq("code",code);
            queryWrapper5.eq("date",date);
            List<Stock> stocks1 = stockService.list(queryWrapper5);
            stocks.addAll(stocks1);
        }
        factornameUs.add("eps");
        factornameUs.add("capital_reserve_fund_per_share");
        factornameUs.add("retained_profit_per_share");
        factornameUs = new ArrayList<String>(new TreeSet<String>(factornameUs));
        List<Map<String, String>> maps = mergeStockData_specila_factor(stocks,factornameUs);

        JSONObject res = new JSONObject();
        JSONArray columns = new JSONArray();
        JSONArray rows = new JSONArray();
        JSONObject res1;
        try {

            for (String key : maps.get(0).keySet()) {
                JSONObject column = new JSONObject();
                column.put("prop", key);
                String value = getFactorNameByfactorNameUS(key);
                column.put("label", value);
                columns.add(column);
            }
            for (Map<String, String> map : maps) {
                JSONObject row = new JSONObject();
                for (Map.Entry<String, String> entry : map.entrySet()) {
//                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    row.put(entry.getKey(), entry.getValue());
                }
                rows.add(row);
            }
            res.put("columns", sortByMe(columns));
            res.put("rows", rows);
            res1 = new JSONObject();
            res1.put("data", res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success(res1);
    }

    private JSONArray sortByMe(JSONArray columns) {
        List<String> desired_columns1 = new ArrayList<>();
        desired_columns1.add("股票代码");
        desired_columns1.add("股票名称");
        desired_columns1.add("公告日期");
        desired_columns1.add("日期");
        String[] desired_columns = {"股票代码","股票名称","公告日期","日期"};
        JSONArray newcolums = new JSONArray();
        for (int i = 0; i < desired_columns.length; i++) {
            for(int j = 0; j < columns.size(); j++){
                JSONObject jsonObject = columns.getJSONObject(j);
//                System.out.println(jsonObject);
                String co = jsonObject.getString("label");
                if(co.equals(desired_columns[i])){
                    newcolums.add(jsonObject);
                }
            }
        }
        for(int i = 0; i < columns.size(); i++){
            JSONObject jsonObject = columns.getJSONObject(i);
//            System.out.println(jsonObject);
            String co = jsonObject.getString("label");
            if(!desired_columns1.contains(co)){
                newcolums.add(jsonObject);
            }
        }
        return newcolums;
    }
    // 由英文名称得到中文名称
    private String getFactorNameByfactorNameUS(String factorNameUS) {
        //实现根据英文名称查询中文名称
        if(factorNameUS.equals("pubdate")) return "公告日期";
        if(factorNameUS.equals("date")) return "日期";
        if(factorNameUS.equals("code")) return "股票代码";
        if(factorNameUS.equals("stock_name")) return "股票名称";
        String factorNameCN = factorHZService.getOne(new QueryWrapper<FactorHZ>().eq("name_us", factorNameUS)).getName();
        return factorNameCN; // 示例中简单返回一个固定值
    }

    public List<Map<String, String>> getStockEntity(AlgorithmVo algorithmVO,String date) throws Exception {
        int len = algorithmVO.getFactors().size();
        List<AlgorithmVo.FactorHZ> list = algorithmVO.getFactors();
        List<Stock> res = new ArrayList<>();
        Set<String> existingStockNames = new HashSet<>();
        if(Objects.equals(date, "2020")){
            date = "2020/9/30";
        } else if (Objects.equals(date, "2021")) {
            date = "2021/9/30";
        }else{
            date = "2022/9/30";
        }
        for (AlgorithmVo.FactorHZ factor : list) {
            QueryWrapper<Stock> queryWrapper = new QueryWrapper<>();
            QueryWrapper<Stock> queryWrapper1 = new QueryWrapper<>();
            QueryWrapper<Stock> queryWrapper2 = new QueryWrapper<>();
            //先假定数据集为2020年
            queryWrapper.eq("date",date);
            queryWrapper1.eq("date",date);
            queryWrapper2.eq("date",date);
            List<Stock> list1 = new ArrayList<>();
            String factorNameUs = factorHZService.getById(factor.getId()).getNameUs();
            String factorNameUs0 = factorNameUs + "+0";
            String logic = factor.getLogic();
            int choiceType = factor.getChoiceType();
            double value = Double.parseDouble(factor.getValue());
            int isTop = factor.getIsTop();
            //先选因子列
            queryWrapper.eq("factor_name",factorNameUs);
            queryWrapper1.eq("factor_name",factorNameUs);
            queryWrapper2.eq("factor_name",factorNameUs);

            //0：百分比
            if(choiceType==0){
                //从大到小
                if(isTop==1){
                    //获取限制的数量
                    long count = stockServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);

                    queryWrapper2.orderByDesc("factor_value+0");
                    queryWrapper2.last("limit 0,"+limit);
                    List<Stock> listLimit = stockServiceLimit.list(queryWrapper2);
                    Stock stockData = listLimit.get(listLimit.size() - 1);
                    //拼接where条件
                    queryWrapper.ge("factor_value+0",stockData.getFactorValue());

                }else{
                    //获取限制的数量
                    long count = stockServiceCount.count(queryWrapper1);
                    Double number_limit =count * (value /100);
                    String limit = String.format("%.0f",number_limit);
                    queryWrapper2.orderByAsc("factor_value+0");
                    queryWrapper2.last("limit 0,"+limit);
                    List<Stock> listLimit = stockServiceLimit.list(queryWrapper2);
                    Stock stockData = listLimit.get(listLimit.size() - 1);
                    //拼接where条件
                    queryWrapper.le("factor_value+0",stockData.getFactorValue());
                }
            } else if (choiceType == 2) {
                //2：大于小于
                if(logic.equals("大于等于")){
                    queryWrapper.ge("factor_value+0",value);
                } else if (logic.equals("小于等于")) {
                    queryWrapper.le("factor_value+0",value);
                }else if(logic.equals("大于")) {
                    queryWrapper.gt("factor_value+0",value);
                }else if(logic.equals("小于")) {
                    queryWrapper.lt("factor_value+0",value);
                }else if(logic.equals("等于")) {
                    queryWrapper.eq("factor_value+0",value);
                }
            }else {
                //1：排名数字 分大到小和从小到大
                if(isTop==1) {
                    if(res.size()!=0){
                        //针对已有的数据排名取前十
                        queryWrapper.in("stock_name",res.stream()
                                .map(Stock::getStockName)
                                .collect(Collectors.toList()));
                        queryWrapper.orderByDesc("factor_value+0");
                        queryWrapper.last("limit 0," + (int)value*2);
                    }else{
                        queryWrapper.last("limit 0," + (int)value*2);
                    }
                    //queryWrapper.last("limit 0," + (int)value*2);
                }else{
                    queryWrapper.orderByAsc("factor_value+0");
                    //queryWrapper.last("limit 0," + (int)value*2);
                }
            }

            list1 = stockService.list(queryWrapper);
            if (res.isEmpty()) {
                res.addAll(list1);
                existingStockNames.addAll(list1.stream()
                        .map(Stock::getStockName)
                        .collect(Collectors.toSet())); // 使用HashSet来加速查找
            } else {
                //list1与res取交集,list1
                List<Stock> list1intersection = list1.stream()
                        .filter(stock -> existingStockNames.contains(stock.getStockName()))
                        .collect(Collectors.toList());
                //把res中与intersection的factor_name不同的元素删除
                existingStockNames.clear();
                existingStockNames.addAll(list1intersection.stream()
                        .map(Stock::getStockName)
                        .collect(Collectors.toSet()));
                //res与intersection取交集,res
                List<Stock> resIntersection = res.stream()
                        .filter(stock -> existingStockNames.contains(stock.getStockName()))
                        .collect(Collectors.toList());
                res.clear();
                res.addAll(list1intersection);
                res.addAll(resIntersection);
            }
        }

        List<Map<String, String>> maps = mergeStockData(res);
        if(maps.size()>10) maps = maps.subList(0,10);
        return maps;
    }

    public List<Map<String, String>> mergeStockData(List<Stock> res){
        Map<String, Map<String, String>> mergedData = new HashMap<>();
        // 遍历原始数据列表
        for (Stock stock : res) {
            String stockName = stock.getStockName();

            // 如果mergedData中已经有这个stock_name的数据，就获取对应的Map
            // 否则，创建一个新的Map
            Map<String, String> stockData = mergedData.getOrDefault(stockName, new HashMap<>());

            // 将原始数据中的字段和值添加到stockData中
            stockData.put("code", stock.getCode());
            stockData.put("stock_name", stock.getStockName());
            stockData.put("pubdate", stock.getPubdate());
            stockData.put("date", stock.getDate());
            stockData.put(stock.getFactorName(), stock.getFactorValue());

            // 更新或添加合并后的数据到mergedData中
            mergedData.put(stockName, stockData);
        }
        List<Map<String, String>> mergedList = new ArrayList<>(mergedData.values());
        return mergedList;
    }

    public List<Map<String, String>> mergeStockData_specila_factor(List<Stock> res,List<String> factornameUs){
        Map<String, Map<String, String>> mergedData = new HashMap<>();
        // 遍历原始数据列表
        for (Stock stock : res) {
            String stockName = stock.getStockName();

            // 如果mergedData中已经有这个stock_name的数据，就获取对应的Map
            // 否则，创建一个新的Map
            Map<String, String> stockData = mergedData.getOrDefault(stockName, new HashMap<>());

            // 将原始数据中的字段和值添加到stockData中
            stockData.put("code", stock.getCode());
            stockData.put("stock_name", stock.getStockName());
            stockData.put("pubdate", stock.getPubdate());
            stockData.put("date", stock.getDate());
            if(factornameUs.contains(stock.getFactorName())){
                stockData.put(stock.getFactorName(), stock.getFactorValue());
            }
            // 更新或添加合并后的数据到mergedData中
            mergedData.put(stockName, stockData);
        }
        List<Map<String, String>> mergedList = new ArrayList<>(mergedData.values());
        return mergedList;
    }

}
