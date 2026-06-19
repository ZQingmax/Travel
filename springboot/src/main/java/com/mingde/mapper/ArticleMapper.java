package com.mingde.mapper;

import com.mingde.entity.Article;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ArticleMapper {

    int insert(Article article);

    void updateById(Article article);

    void deleteById(Integer id);

    @Select("select * from `article` where id = #{id}")
    Article selectById(Integer id);

    List<Article> selectAll(Article article);

    List<Article> selectRecommend();

    @Update("update article set read_count = read_count + 1 where id = #{id}")
    void updateCount(Integer id);
}
