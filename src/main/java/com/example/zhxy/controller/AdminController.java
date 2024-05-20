package com.example.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.service.AdminService;
import com.example.zhxy.uitl.MD5;
import com.example.zhxy.uitl.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页查询所有的管理员信息")
    @GetMapping("/getAllAdmin/{pageNum}/{pageSize}")
    public Result getAllAdmin(
            @PathVariable Integer pageNum,
            @PathVariable Integer pageSize,
            Admin admin
    ){
        Page<Admin> adminPage = new Page<>(pageNum,pageSize);
        IPage<Admin> adminIPage  = adminService.getAdmins(adminPage,admin);
        return Result.ok(adminIPage);
    }



    @ApiOperation("保存和修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("管理员数据") @RequestBody Admin admin
    ){
        if (!StringUtils.isEmpty(admin.getPassword())){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }


    @ApiOperation("删除管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @ApiParam("需要的ids") @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }


}
