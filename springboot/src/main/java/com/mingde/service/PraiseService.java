package com.mingde.service;

import com.mingde.entity.Account;
import com.mingde.entity.Praise;
import com.mingde.mapper.PraiseMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PraiseService {

    @Autowired
    private PraiseMapper praiseMapper;

    public void add(Praise praise) {
        Account currentUser = AuthUtils.currentUser();
        praise.setUserId(currentUser.getId());
        Praise dbPraise = praiseMapper.selectCountByFidAndUserID(praise.getFid(), currentUser.getId());
        if (dbPraise == null) {
            praiseMapper.insert(praise);
        } else {
            praiseMapper.deleteByFidAndUserID(praise.getFid(), currentUser.getId());
        }
    }
}
