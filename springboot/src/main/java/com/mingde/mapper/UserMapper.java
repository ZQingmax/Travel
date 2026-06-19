package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);

    List<User> selectAll(User user);

    IPage<User> selectPage(IPage<User> page, @Param("query") User query);

}
