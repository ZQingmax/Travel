package com.mingde.controller;

import com.mingde.common.PageResult;
import com.mingde.common.Result;
import com.mingde.entity.Travels;
import com.mingde.service.TravelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/travels")
public class TravelsController {

    @Autowired
    private TravelsService travelsService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Travels travels) {
        travelsService.add(travels);
        return Result.success(travels);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Travels travels) {
        travelsService.updateById(travels);
        return Result.success();
    } /**
     * 更新阅读量
     */
    @PutMapping("/updateReadCount/{id}")
    public Result updateReadCount(@PathVariable Integer id) {
        travelsService.updateReadCount(id);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        travelsService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        travelsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Travels travels = travelsService.selectById(id);
        return Result.success(travels);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Travels travels) {
        List<Travels> list = travelsService.selectAll(travels);
        return Result.success(list);
    }
    /**
     * 用户分页查询
     */
    @GetMapping("/selectFrontPage")
    public Result selectFrontPage(Travels travels,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Travels> pageInfo = travelsService.selectFrontPage(travels, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Travels travels,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<Travels> pageInfo = travelsService.selectPage(travels, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
