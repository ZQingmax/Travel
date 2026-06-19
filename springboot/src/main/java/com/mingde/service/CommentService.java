package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.date.DateUtil;
import com.mingde.entity.Account;
import com.mingde.entity.Comment;
import com.mingde.mapper.CommentMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public void add(Comment comment) {
        Account currentUser = AuthUtils.currentUser();
        comment.setUserId(currentUser.getId());
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);

        if (comment.getPid() != null) {
            Comment parentComment = this.selectById(comment.getPid());
            comment.setRootId(parentComment.getRootId());
        } else {
            comment.setRootId(comment.getId());
        }
        commentMapper.updateById(comment);
    }

    public void updateById(Comment comment) {
        Comment dbComment = commentMapper.selectById(comment.getId());
        if (dbComment != null) {
            AuthUtils.requireOwnerOrAdmin(dbComment.getUserId());
        }
        comment.setUserId(null);
        comment.setFid(null);
        comment.setModule(null);
        comment.setRootId(null);
        commentMapper.updateById(comment);
    }

    public void deleteById(Integer id) {
        Comment dbComment = commentMapper.selectById(id);
        if (dbComment == null) {
            return;
        }
        AuthUtils.requireOwnerOrAdmin(dbComment.getUserId());
        this.deepDelete(id);
    }

    private void deepDelete(Integer pid) {
        List<Comment> children = commentMapper.selectByPid(pid);
        commentMapper.deleteById(pid);
        for (Comment child : children) {
            deepDelete(child.getId());
        }
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    public List<Comment> selectAll(Comment comment) {
        AuthUtils.requireAdmin();
        return commentMapper.selectAll(comment);
    }

    public Integer selectCount(Integer fid, String module) {
        return commentMapper.selectCommentCount(fid, module);
    }

    public PageResult<Comment> selectTree(Comment comment, Integer pageNum, Integer pageSize) {
        Page<Comment> page = PageUtils.page(pageNum, pageSize);
        IPage<Comment> result = commentMapper.selectRootPage(page, comment);
        List<Integer> rootIds = result.getRecords().stream()
                .map(Comment::getId)
                .toList();
        if (!rootIds.isEmpty()) {
            Map<Integer, List<Comment>> childrenMap = commentMapper.selectByRootIds(rootIds).stream()
                    .collect(Collectors.groupingBy(Comment::getRootId));
            result.getRecords().forEach(root -> root.setChildren(
                    childrenMap.getOrDefault(root.getId(), Collections.emptyList())));
        }
        return PageUtils.toResult(result);
    }

    public PageResult<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        AuthUtils.requireAdmin();
        Page<Comment> page = PageUtils.page(pageNum, pageSize);
        IPage<Comment> result = commentMapper.selectAllPage(page, comment);
        return PageUtils.toResult(result);
    }
}
