package com.mingde.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mingde.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Article> selectAll(Article article);

    IPage<Article> selectPage(IPage<Article> page, @Param("query") Article query);

    List<Article> selectRecommend();

    @Update("update article set read_count = read_count + 1 where id = #{id}")
    void updateCount(Integer id);
}
