package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Tourism;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface TourismMapper extends BaseMapper<Tourism> {

    List<Tourism> selectAll(Tourism tourism);

    IPage<Tourism> selectPage(IPage<Tourism> page, @Param("query") Tourism query);

    @Update("update tourism set store = store - #{num} where id = #{id} and store >= #{num}")
    int decreaseStore(Integer id, Integer num);
}
