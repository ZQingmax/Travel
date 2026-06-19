package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Collect;
import com.mingde.exception.CustomException;
import com.mingde.mapper.CollectMapper;
import com.mingde.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class CollectService {

    @Autowired
    private CollectMapper collectMapper;

    public void add(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        // 判断当前用户有没有收藏过
        Collect dbCollect = collectMapper.selectUserCollect(currentUser.getId(), collect.getFid());
        if (dbCollect != null) {
            throw new CustomException("500","您已经收藏过了");
        }
        collect.setUserId(currentUser.getId());
        collect.setTime(DateUtil.now());
        collectMapper.insert(collect);
    }

    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(currentUser.getRole().equals(RoleEnum.USER.name())){
            collect.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

}
