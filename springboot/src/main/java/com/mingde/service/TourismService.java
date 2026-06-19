package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.entity.Tourism;
import com.mingde.mapper.TourismMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourismService {

    @Autowired
    private TourismMapper tourismMapper;

    public void add(Tourism tourism) {
        AuthUtils.requireAdmin();
        tourism.setDate(DateUtil.today());
        tourismMapper.insert(tourism);
    }

    public void updateById(Tourism tourism) {
        AuthUtils.requireAdmin();
        tourismMapper.updateById(tourism);
    }

    public void deleteById(Integer id) {
        AuthUtils.requireAdmin();
        tourismMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        AuthUtils.requireAdmin();
        for (Integer id : ids) {
            tourismMapper.deleteById(id);
        }
    }

    public Tourism selectById(Integer id) {
        return tourismMapper.selectById(id);
    }

    public List<Tourism> selectAll(Tourism tourism) {
        return tourismMapper.selectAll(tourism);
    }

    public PageInfo<Tourism> selectPage(Tourism tourism, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Tourism> list = tourismMapper.selectAll(tourism);
        return PageInfo.of(list);
    }
}
