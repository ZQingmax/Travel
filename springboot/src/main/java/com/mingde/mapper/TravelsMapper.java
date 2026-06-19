package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Travels;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface TravelsMapper extends BaseMapper<Travels> {

    @Select("select travels.*,user.name AS userName,user.avatar AS userAvatar from `travels` " +
            "left join user on travels.user_id = user.id where travels.id = #{id}")
    Travels selectById(Integer id);

    List<Travels> selectAll(Travels travels);

    IPage<Travels> selectPage(IPage<Travels> page, @Param("query") Travels query);

    @Update("update travels set read_count = read_count + 1 where id = #{id}")
    void updateReadCount(Integer id);
}
