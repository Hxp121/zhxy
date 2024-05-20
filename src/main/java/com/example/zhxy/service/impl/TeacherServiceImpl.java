package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.TeacherMapper;
import com.example.zhxy.pojo.LoginForm;
import com.example.zhxy.pojo.Teacher;
import com.example.zhxy.service.TeacherService;
import com.example.zhxy.uitl.MD5;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher Login(LoginForm loginForm) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("name",loginForm.getUsername())
                        .eq("password", MD5.encrypt(loginForm.getPassword()));

        return baseMapper.selectOne(teacherQueryWrapper);
    }

    @Override
    public Teacher getTeacherById(int intValue) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("id",intValue);

        return baseMapper.selectOne(teacherQueryWrapper);
    }

    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> teacherPage, Teacher teacher) {
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(teacher.getClazzName())){
            teacherQueryWrapper.eq("clazz_name",teacher.getClazzName());
        }

        if (!StringUtils.isEmpty(teacher.getName())){
            teacherQueryWrapper.eq("name",teacher.getName());
        }

        Page<Teacher> teacherPage1 = baseMapper.selectPage(teacherPage, teacherQueryWrapper);
        return teacherPage1;
    }

}
