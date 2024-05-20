package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.TeacherService;
import com.example.zhxy.uitl.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "老师控制器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @ApiOperation("分页查询老师信息")
    @GetMapping("/getTeachers/{pageNum}/{pageSize}")
    public Result getTeachers(
            @PathVariable Integer pageNum,
            @PathVariable Integer pageSize,
            Teacher teacher
    ){
        Page<Teacher> teacherPage = new Page<>(pageNum,pageSize);
        IPage<Teacher> teacherIPage = teacherService.getTeachersByOpr(teacherPage,teacher);
        return Result.ok(teacherIPage);
    }


    @ApiOperation("保存和修改老师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @RequestBody Teacher teacher
    ){
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }



    @ApiOperation("删除老师信息")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
