package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Feedback;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface FeedbackMapper extends BaseMapper<Feedback> {

    List<Feedback> selectAll(Feedback feedback);

    IPage<Feedback> selectPage(IPage<Feedback> page, @Param("query") Feedback query);

}
