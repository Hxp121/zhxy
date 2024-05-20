package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Clazz;
import com.example.zhxy.service.ClazzService;
import com.example.zhxy.uitl.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @ApiOperation("根据查询条件分页查询，得到班级信息")
    @GetMapping("/getClazzsByOpr/{pageNum}/{pageSize}")
    public Result getClazzsByOpr(
            @ApiParam("页码数") @PathVariable("pageNum") Integer pageNum,
            @ApiParam("每页记录条数") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询条件") Clazz clazz
    ){
        Page<Clazz> page = new Page<>();
        IPage<Clazz> iPage = clazzService.getClazzsBy(page,clazz);
        return Result.ok(iPage);
    }


    @ApiOperation("保存或者修改班级信息")
    @PostMapping("saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
           @ApiParam("JSON转换后端Clazz数据") @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }


    @ApiOperation("根据Id删除班级信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazzByIds(
           @ApiParam("需要的id条件") @RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);
        return Result.ok();
    }


    @ApiOperation("将班级信息响应给浏览器")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzList = clazzService.getClazzs();
        return Result.ok(clazzList);
    }

}
