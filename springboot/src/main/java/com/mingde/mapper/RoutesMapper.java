package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Routes;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface RoutesMapper extends BaseMapper<Routes> {

    List<Routes> selectAll(Routes routes);

    IPage<Routes> selectPage(IPage<Routes> page, @Param("query") Routes query);

}
