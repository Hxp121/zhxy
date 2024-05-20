package com.example.zhxy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import org.springframework.stereotype.Service;


public interface StudentService extends IService<Student> {
    Student Login(LoginForm loginForm);

    Student getStudentById(int intValue);

    IPage<Student> getStudentsByOpr(Page<Student> studentPage, Student student);
}
