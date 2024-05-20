package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.StudentMapper;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Student;
import com.example.zhxy.service.StudentService;
import com.example.zhxy.uitl.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public Student Login(LoginForm loginForm) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("name",loginForm.getUsername())
                .eq("password", MD5.encrypt(loginForm.getPassword()));

        return baseMapper.selectOne(studentQueryWrapper);
    }

    @Override
    public Student getStudentById(int intValue) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.eq("id",intValue);

        return baseMapper.selectOne(studentQueryWrapper);
    }

    @Override
    public IPage<Student> getStudentsByOpr(Page<Student> studentPage, Student student) {
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(student.getClazzName())){
            studentQueryWrapper.eq("clazz_name",student.getClazzName());
        }

        if (!StringUtils.isEmpty(student.getName())){
            studentQueryWrapper.eq("name",student.getName());
        }

        Page<Student> studentPage1 = baseMapper.selectPage(studentPage, studentQueryWrapper);

        return studentPage1;
    }


}
