package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Teacher;
import org.springframework.stereotype.Service;


public interface TeacherService extends IService<Teacher> {
    Teacher Login(LoginForm loginForm);

    Teacher getTeacherById(int intValue);

    IPage<Teacher> getTeachersByOpr(Page<Teacher> teacherPage, Teacher teacher);
}
