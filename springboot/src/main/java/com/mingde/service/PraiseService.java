package com.mingde.service;

import com.mingde.entity.Praise;
import com.mingde.mapper.PraiseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PraiseService {

    @Autowired
    private PraiseMapper praiseMapper;

    //点赞 或 取消点赞
    public void add(Praise praise) {
        Praise dbPraise = praiseMapper.selectCountByFidAndUserID(praise.getFid(), praise.getUserId());
        if (dbPraise == null) { //未点赞
            praiseMapper.insert(praise);
        } else { //已点赞
            praiseMapper.deleteByFidAndUserID(praise.getFid(), praise.getUserId());
        }
    }
}
