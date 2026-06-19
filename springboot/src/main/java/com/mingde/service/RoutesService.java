package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
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

    public PageResult<Routes> selectPage(Routes routes, Integer pageNum, Integer pageSize) {
        Page<Routes> page = PageUtils.page(pageNum, pageSize);
        IPage<Routes> result = routesMapper.selectPage(page, routes);
        return PageUtils.toResult(result);
    }
}
