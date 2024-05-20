package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.service.StudentService;
import com.example.zhxy.uitl.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @ApiOperation("根据班级和姓名查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNum}/{pageSize}")
    public Result getStudentsByOpr(
            @ApiParam("页码数") @PathVariable Integer pageNum,
            @ApiParam("每页记录数") @PathVariable Integer pageSize,
            @ApiParam("查询条件") Student student
    ){
        Page<Student> studentPage = new Page<>();
        IPage<Student> iPage = studentService.getStudentsByOpr(studentPage,student);
        return Result.ok(iPage);
    }


    @ApiOperation("添加或者修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("json数据转换为学生信息") @RequestBody Student student
    ){
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


    @ApiOperation("删除学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @ApiParam("传过来的id条件") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }
}
