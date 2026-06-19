package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.date.DateUtil;
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

    public PageResult<Tourism> selectPage(Tourism tourism, Integer pageNum, Integer pageSize) {
        Page<Tourism> page = PageUtils.page(pageNum, pageSize);
        IPage<Tourism> result = tourismMapper.selectPage(page, tourism);
        return PageUtils.toResult(result);
    }
}
