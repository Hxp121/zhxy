package com.example.zhxy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.zhxy.mapper.ClazzMapper;
import com.example.zhxy.pojo.Clazz;
import com.example.zhxy.service.ClazzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzsBy(Page<Clazz> page, Clazz clazz) {
        QueryWrapper<Clazz> clazzQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(clazz.getGradeName())){
            clazzQueryWrapper.eq("grade_name",clazz.getGradeName());
        }

        if (!StringUtils.isEmpty(clazz.getName())){
            clazzQueryWrapper.eq("name",clazz.getName());
        }

        Page<Clazz> clazzPage = baseMapper.selectPage(page, clazzQueryWrapper);
        return clazzPage;
    }

    @Override
    public List<Clazz> getClazzs() {
        List<Clazz> clazzList = baseMapper.selectList(null);
        return clazzList;
    }
}
