package com.mingde.mapper;


import com.mingde.entity.Praise;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface PraiseMapper {

    //点赞
    @Insert("insert into praise(fid,user_id) values(#{fid},#{userId})")
    void insert(Praise praise);

    //取消点赞
    @Insert("delete from praise where fid = #{fid} and user_id = #{userId}")
    void deleteByFidAndUserID(@Param("fid") Integer fid, @Param("userId") Integer userId);

    //查询是否点赞
    @Select("select * from praise where fid = #{fid} and user_id = #{userId}")
    Praise selectCountByFidAndUserID(@Param("fid") Integer fid, @Param("userId") Integer userId);

    //查询点赞数
    @Select("select count(*) from praise where fid = #{fid}")
    Integer selectCount(Integer fid);
}
