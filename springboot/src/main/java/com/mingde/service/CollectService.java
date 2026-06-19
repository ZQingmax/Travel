package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Collect;
import com.mingde.exception.CustomException;
import com.mingde.mapper.CollectMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectService {

    @Autowired
    private CollectMapper collectMapper;

    public void add(Collect collect) {
        Account currentUser = AuthUtils.currentUser();
        Collect dbCollect = collectMapper.selectUserCollect(currentUser.getId(), collect.getFid());
        if (dbCollect != null) {
            throw new CustomException("500", "您已经收藏过了");
        }
        collect.setUserId(currentUser.getId());
        collect.setTime(DateUtil.now());
        collectMapper.insert(collect);
    }

    public void updateById(Collect collect) {
        Collect dbCollect = selectById(collect.getId());
        collect.setUserId(dbCollect.getUserId());
        collectMapper.updateById(collect);
    }

    public void deleteById(Integer id) {
        Collect dbCollect = collectMapper.selectById(id);
        if (dbCollect == null) {
            return;
        }
        AuthUtils.requireOwnerOrAdmin(dbCollect.getUserId());
        collectMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    public Collect selectById(Integer id) {
        Collect collect = collectMapper.selectById(id);
        if (collect != null) {
            AuthUtils.requireOwnerOrAdmin(collect.getUserId());
        }
        return collect;
    }

    public List<Collect> selectAll(Collect collect) {
        AuthUtils.requireAdmin();
        return collectMapper.selectAll(collect);
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        Account currentUser = AuthUtils.currentUser();
        if (currentUser.getRole().equals(RoleEnum.USER.name())) {
            collect.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }
}
