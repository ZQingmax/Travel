package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.entity.Account;
import com.mingde.entity.Comment;
import com.mingde.mapper.CommentMapper;
import com.mingde.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层方法
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public void add(Comment comment) {
        Account currentUser = TokenUtils.getCurrentUser();
        comment.setUserId(currentUser.getId());
        comment.setTime(DateUtil.now());
        commentMapper.insert(comment);//插入数据库，自增id,这个id作为自己的root_id

        if(comment.getPid() != null){ //子评论必须要root_ip
            Comment parentComment = this.selectById(comment.getPid());
            comment.setRootId(parentComment.getRootId());
        }else{ //如果是根节点，root_id就是自己的id
            comment.setRootId(comment.getId());
        }

        this.updateById(comment);
    }

    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    public void deleteById(Integer id) {
        this.deepDelete(id);
    }

    //递归删除
    public void deepDelete(Integer pid) {
        List<Comment> children = commentMapper.selectByPid(pid);
        commentMapper.deleteById(pid);
        for (Comment child : children) {
            deepDelete(child.getId());
        }
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    public List<Comment> selectAll(Comment comment) {
        return commentMapper.selectAll(comment);
    }
    public Integer selectCount(Integer fid,String module) {
        return commentMapper.selectCount(fid,module);
    }

    public PageInfo<Comment> selectTree(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectRoot(comment);  // 查询一级节点
        for (Comment root : list) {
            List<Comment> children = commentMapper.selectByRootId(root.getId());
            root.setChildren(children);
        }
        return PageInfo.of(list);
    }

    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = commentMapper.selectAll(comment);
        return PageInfo.of(list);
    }
}
