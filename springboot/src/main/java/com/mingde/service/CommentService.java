package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.entity.Account;
import com.mingde.entity.Comment;
import com.mingde.mapper.CommentMapper;
import com.mingde.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return commentMapper.selectCount(fid, module);
    }

    public PageInfo<Comment> selectTree(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Comment> list = commentMapper.selectRoot(comment);
        for (Comment root : list) {
            List<Comment> children = commentMapper.selectByRootId(root.getId());
            root.setChildren(children);
        }
        return PageInfo.of(list);
    }

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        AuthUtils.requireAdmin();
        PageHelper.startPage(pageNum, Math.min(pageSize, 100));
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }
}
