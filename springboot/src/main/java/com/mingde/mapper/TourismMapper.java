package com.mingde.mapper;

import com.mingde.entity.Tourism;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TourismMapper {

    int insert(Tourism tourism);

    void updateById(Tourism tourism);

    void deleteById(Integer id);

    @Select("select * from `tourism` where id = #{id}")
    Tourism selectById(Integer id);

    List<Tourism> selectAll(Tourism tourism);

    @Update("update tourism set store = store - #{num} where id = #{id} and store >= #{num}")
    int decreaseStore(Integer id, Integer num);
}
