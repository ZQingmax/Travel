package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Travels;
import com.mingde.exception.CustomException;
import com.mingde.mapper.PraiseMapper;
import com.mingde.mapper.TravelsMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelsService {

    @Autowired
    private TravelsMapper travelsMapper;

    @Autowired
    private PraiseMapper praiseMapper;

    public void add(Travels travels) {
        Account currentUser = AuthUtils.currentUser();
        travels.setReadCount(0);
        travels.setStatus("待审核");
        travels.setTime(DateUtil.now());
        travels.setUserId(currentUser.getId());
        travelsMapper.insert(travels);
    }

    public void updateById(Travels travels) {
        Travels dbTravels = travelsMapper.selectById(travels.getId());
        if (dbTravels == null) {
            throw new CustomException("404", "游记不存在");
        }
        Account currentUser = AuthUtils.currentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            AuthUtils.requireOwnerOrAdmin(dbTravels.getUserId());
            travels.setUserId(null);
            travels.setStatus("待审核");
            travels.setReadCount(null);
        }
        travelsMapper.updateById(travels);
    }

    public void deleteById(Integer id) {
        Travels dbTravels = travelsMapper.selectById(id);
        if (dbTravels == null) {
            return;
        }
        AuthUtils.requireOwnerOrAdmin(dbTravels.getUserId());
        travelsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    public Travels selectById(Integer id) {
        Travels travels = travelsMapper.selectById(id);
        if (travels != null) {
            setTravelData(travels);
        }
        return travels;
    }

    public List<Travels> selectAll(Travels travels) {
        AuthUtils.requireAdmin();
        return travelsMapper.selectAll(travels);
    }

    public PageInfo<Travels> selectPage(Travels travels, Integer pageNum, Integer pageSize) {
        Account currentUser = AuthUtils.currentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            travels.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Travels> list = travelsMapper.selectAll(travels);
        return PageInfo.of(list);
    }

    public PageInfo<Travels> selectFrontPage(Travels travels, Integer pageNum, Integer pageSize) {
        travels.setStatus("通过");
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Travels> list = travelsMapper.selectAll(travels);
        for (Travels t : list) {
            setTravelData(t);
        }
        return PageInfo.of(list);
    }

    public void updateReadCount(Integer id) {
        travelsMapper.updateReadCount(id);
    }

    private void setTravelData(Travels travels) {
        Integer count = praiseMapper.selectCount(travels.getId());
        travels.setPraiseCount(count);
    }
}
