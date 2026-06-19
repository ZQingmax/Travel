package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Collect;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface CollectMapper extends BaseMapper<Collect> {

    List<Collect> selectAll(Collect collect);

    IPage<Collect> selectPage(IPage<Collect> page, @Param("query") Collect query);

    @Select("select * from `collect` where user_id = #{userId} and fid = #{fid}")
    Collect selectUserCollect(@Param("userId")Integer userId, @Param("fid") Integer fid);

    @Select("select count(*) from `collect` where fid = #{fid}")
    Integer selectByFid(Integer id);
}
