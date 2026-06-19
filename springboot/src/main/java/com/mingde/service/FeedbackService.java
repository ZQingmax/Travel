package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.date.DateUtil;
import com.mingde.common.enums.RoleEnum;
import com.mingde.entity.Account;
import com.mingde.entity.Feedback;
import com.mingde.mapper.FeedbackMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    public void add(Feedback feedback) {
        Account currentUser = AuthUtils.currentUser();
        feedback.setUserId(currentUser.getId());
        feedback.setTime(DateUtil.now());
        feedback.setReply(null);
        feedbackMapper.insert(feedback);
    }

    public void updateById(Feedback feedback) {
        Feedback dbFeedback = feedbackMapper.selectById(feedback.getId());
        if (dbFeedback == null) {
            return;
        }
        Account currentUser = AuthUtils.currentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            AuthUtils.requireOwnerOrAdmin(dbFeedback.getUserId());
            Feedback update = new Feedback();
            update.setId(dbFeedback.getId());
            update.setTitle(feedback.getTitle());
            update.setContent(feedback.getContent());
            feedbackMapper.updateById(update);
            return;
        }
        feedback.setUserId(null);
        feedback.setTime(null);
        feedbackMapper.updateById(feedback);
    }

    public void deleteById(Integer id) {
        Feedback dbFeedback = feedbackMapper.selectById(id);
        if (dbFeedback == null) {
            return;
        }
        AuthUtils.requireOwnerOrAdmin(dbFeedback.getUserId());
        feedbackMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    public Feedback selectById(Integer id) {
        Feedback feedback = feedbackMapper.selectById(id);
        if (feedback != null) {
            AuthUtils.requireOwnerOrAdmin(feedback.getUserId());
        }
        return feedback;
    }

    public List<Feedback> selectAll(Feedback feedback) {
        AuthUtils.requireAdmin();
        return feedbackMapper.selectAll(feedback);
    }

    public PageResult<Feedback> selectPage(Feedback feedback, Integer pageNum, Integer pageSize) {
        Account currentUser = AuthUtils.currentUser();
        if (currentUser.getRole().equals(RoleEnum.USER.name())) {
            feedback.setUserId(currentUser.getId());
        }
        Page<Feedback> page = PageUtils.page(pageNum, pageSize);
        IPage<Feedback> result = feedbackMapper.selectPage(page, feedback);
        return PageUtils.toResult(result);
    }
}
