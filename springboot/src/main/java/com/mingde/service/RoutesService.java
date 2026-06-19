package com.mingde.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.entity.Routes;
import com.mingde.mapper.RoutesMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutesService {

    @Autowired
    private RoutesMapper routesMapper;

    public void add(Routes routes) {
        AuthUtils.requireAdmin();
        routesMapper.insert(routes);
    }

    public void updateById(Routes routes) {
        AuthUtils.requireAdmin();
        routesMapper.updateById(routes);
    }

    public void deleteById(Integer id) {
        AuthUtils.requireAdmin();
        routesMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        AuthUtils.requireAdmin();
        for (Integer id : ids) {
            routesMapper.deleteById(id);
        }
    }

    public Routes selectById(Integer id) {
        return routesMapper.selectById(id);
    }

    public List<Routes> selectAll(Routes routes) {
        return routesMapper.selectAll(routes);
    }

    public PageInfo<Routes> selectPage(Routes routes, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Routes> list = routesMapper.selectAll(routes);
        return PageInfo.of(list);
    }
}
