package com.mingde.controller;


import com.mingde.common.Result;
import com.mingde.entity.Praise;
import com.mingde.service.PraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/praise")
public class PraiseController {

    @Autowired
    private PraiseService praiseService;

    /**
     * 点赞 / 取消点赞
     */
    @PostMapping("/add")
    public Result add(@RequestBody Praise praise) {
        praiseService.add(praise);
        return Result.success();
    }

}
