package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Travels;
import com.mingde.mapper.PraiseMapper;
import com.mingde.mapper.TravelsMapper;
import com.mingde.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class TravelsService{

    @Autowired
    private TravelsMapper travelsMapper;

    @Autowired
    private PraiseMapper praiseMapper;

    public void add(Travels travels) {
        travels.setReadCount(0);
        travels.setStatus("待审核");
        travels.setTime(DateUtil.now());
        Account currentUser = TokenUtils.getCurrentUser();
        assert currentUser != null;
        travels.setUserId(currentUser.getId());
        travelsMapper.insert(travels);
    }

    public void updateById(Travels travels) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            travels.setStatus("待审核");
        }
        travelsMapper.updateById(travels);
    }

    public void deleteById(Integer id) {
        travelsMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            travelsMapper.deleteById(id);
        }
    }

    public Travels selectById(Integer id) {
        Travels travels = travelsMapper.selectById(id);
        setTravelData(travels);
        return travels;
    }

    public List<Travels> selectAll(Travels travels) {
        return travelsMapper.selectAll(travels);
    }

    public PageInfo<Travels> selectPage(Travels travels, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if(RoleEnum.USER.name().equals(currentUser.getRole())){
            travels.setUserId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Travels> list = travelsMapper.selectAll(travels);
        return PageInfo.of(list);
    }

    // 查询用户可见的游记
    public PageInfo<Travels> selectFrontPage(Travels travels, Integer pageNum, Integer pageSize) {
        travels.setStatus("通过");
        PageHelper.startPage(pageNum, pageSize);
        List<Travels> list = travelsMapper.selectAll(travels);
        for (Travels t : list){
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
