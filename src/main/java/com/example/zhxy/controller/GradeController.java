package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Grade;
import com.example.zhxy.service.GradeService;
import com.example.zhxy.uitl.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("根据路径参数，分页模糊查询年级信息")
    @RequestMapping("/getGrades/{pageNum}/{pageSize}")
    public Result GetGrades(
            @ApiParam("查询的页码") @PathVariable(value = "pageNum") Integer pageNum,
            @ApiParam("查询的页码的记录条数") @PathVariable(value = "pageSize") Integer pageSize,
            @ApiParam("根据这个字符串模糊查询") String gradeName
    ){
        Page<Grade> page = new Page<>(pageNum, pageSize);
        IPage<Grade> iPage = gradeService.getGradeByOpr(page,gradeName);

        return Result.ok(iPage);
    }


    @ApiOperation("添加或者修改年级信息，有id是修改，没有是增加(因为id是自增的)")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
           @ApiParam("保存年级信息的grade对象") @RequestBody Grade grade
    ){
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }


    @ApiOperation("通过id删除年级信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGradeById(
            @ApiParam("保存需要删除的id") @RequestBody List<Integer> ids
    ){
        gradeService.removeByIds(ids);
        return Result.ok();
    }


    @ApiOperation("回显所有的年级信息")
    @GetMapping("/getGrades")
    public Result GetGrades(){
        List<Grade> gradeList = gradeService.getGrades();
        return Result.ok(gradeList);
    }
}
