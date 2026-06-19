package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Notice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface NoticeMapper extends BaseMapper<Notice> {

    List<Notice> selectAll(Notice notice);

    IPage<Notice> selectPage(IPage<Notice> page, @Param("query") Notice query);

}
