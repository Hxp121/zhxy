package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.pojo.LoginForm;

public interface AdminService extends IService<Admin> {
    Admin Login(LoginForm loginForm);

    Admin getAdminById(Integer userType);


    IPage<Admin> getAdmins(Page<Admin> adminPage, Admin admin);
}
