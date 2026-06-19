package com.mingde.mapper;

import com.mingde.entity.Feedback;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FeedbackMapper {

    int insert(Feedback feedback);

    void updateById(Feedback feedback);

    void deleteById(Integer id);

    Feedback selectById(Integer id);

    List<Feedback> selectAll(Feedback feedback);

}
