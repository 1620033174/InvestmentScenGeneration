package org.spring.finance.controller;

import org.spring.finance.service.SceneService;
import org.spring.finance.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scene")
public class ScenePictureController {
    @Autowired
    private SceneService sceneService;
//    @GetMapping("add")
//    public Result<Page<SceneVO>> getByPage(int page, int pageSize, String name) {
//        return Result.success();
//    }
    @PostMapping
    public Result<Void> addScene() {
        return Result.success();
    }
}
