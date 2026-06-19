package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.date.DateUtil;
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

    public PageResult<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        AuthUtils.requireAdmin();
        Page<Notice> page = PageUtils.page(pageNum, pageSize);
        IPage<Notice> result = noticeMapper.selectPage(page, notice);
        return PageUtils.toResult(result);
    }
}
