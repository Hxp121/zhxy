package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.AdminMapper;
import com.example.zhxy.pojo.Admin;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.service.AdminService;
import com.example.zhxy.uitl.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Admin Login(LoginForm loginForm) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginForm.getUsername())
                .eq("password", MD5.encrypt(loginForm.getPassword()));
        Admin admin = baseMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Integer userType) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("id",userType);

        return baseMapper.selectOne(adminQueryWrapper);
    }

    @Override
    public IPage<Admin> getAdmins(Page<Admin> adminPage, Admin admin) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(admin.getName())){
            adminQueryWrapper.like("name",admin.getName());
        }
        adminQueryWrapper.orderByAsc("id");
        adminQueryWrapper.orderByDesc("name");
        Page<Admin> adminPage1 = baseMapper.selectPage(adminPage, adminQueryWrapper);
        return adminPage1;
    }


}
