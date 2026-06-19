package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mingde.common.IdCount;
import com.mingde.entity.Praise;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface PraiseMapper extends BaseMapper<Praise> {
    //取消点赞
    @Delete("delete from praise where fid = #{fid} and user_id = #{userId}")
    void deleteByFidAndUserID(@Param("fid") Integer fid, @Param("userId") Integer userId);

    //查询是否点赞
    @Select("select * from praise where fid = #{fid} and user_id = #{userId}")
    Praise selectCountByFidAndUserID(@Param("fid") Integer fid, @Param("userId") Integer userId);

    //查询点赞数
    @Select("select count(*) from praise where fid = #{fid}")
    Integer selectPraiseCount(Integer fid);

    @Select({
            "<script>",
            "select fid as id, count(*) as count from praise where fid in",
            "<foreach collection='fids' item='fid' open='(' separator=',' close=')'>#{fid}</foreach>",
            "group by fid",
            "</script>"
    })
    List<IdCount> selectPraiseCounts(@Param("fids") List<Integer> fids);
}
