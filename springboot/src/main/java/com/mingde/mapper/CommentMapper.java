package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {

    List<Comment> selectAll(Comment comment);

    IPage<Comment> selectAllPage(IPage<Comment> page, @Param("query") Comment query);

    @Select("select count(*) from comment where fid = #{fid} and module = #{module}")
    Integer selectCommentCount(@Param("fid") Integer fid, @Param("module") String module);

    List<Comment> selectRoot(Comment comment);

    IPage<Comment> selectRootPage(IPage<Comment> page, @Param("query") Comment query);

    List<Comment> selectByRootId(Integer rootId);

    List<Comment> selectByRootIds(@Param("rootIds") List<Integer> rootIds);

    @Select("select * from comment where pid = #{pid}")
    List<Comment> selectByPid(Integer pid);
}
