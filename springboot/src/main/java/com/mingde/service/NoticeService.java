package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.entity.Notice;
import com.mingde.mapper.NoticeMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    public void add(Notice notice) {
        AuthUtils.requireAdmin();
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public void updateById(Notice notice) {
        AuthUtils.requireAdmin();
        noticeMapper.updateById(notice);
    }

    public void deleteById(Integer id) {
        AuthUtils.requireAdmin();
        noticeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        AuthUtils.requireAdmin();
        for (Integer id : ids) {
            noticeMapper.deleteById(id);
        }
    }

    public Notice selectById(Integer id) {
        return noticeMapper.selectById(id);
    }

    public List<Notice> selectAll(Notice notice) {
        return noticeMapper.selectAll(notice);
    }

    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        AuthUtils.requireAdmin();
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Notice> list = noticeMapper.selectAll(notice);
        return PageInfo.of(list);
    }
}
