package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {

    @Select("select * from `admin` where username = #{username}")
    Admin selectByUsername(String username);

    List<Admin> selectAll(Admin admin);

    IPage<Admin> selectPage(IPage<Admin> page, @Param("query") Admin query);

}
