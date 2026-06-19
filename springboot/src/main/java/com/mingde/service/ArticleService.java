package com.mingde.service;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingde.entity.Article;
import com.mingde.mapper.ArticleMapper;
import com.mingde.mapper.CollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CollectMapper collectMapper;

    public void add(Article article) {
        article.setReadCount(0);
        article.setDate(DateUtil.now());
        articleMapper.insert(article);
    }

    public void updateById(Article article) {
        articleMapper.updateById(article);
    }

    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            articleMapper.deleteById(id);
        }
    }

    public Article selectById(Integer id) {
        Article article = articleMapper.selectById(id);
        Integer count = collectMapper.selectByFid(article.getId());
        article.setCollectCount(count); //设置收藏量
        return article;
    }

    public List<Article> selectAll(Article Article) {
        return articleMapper.selectAll(Article);
    }

    public PageInfo<Article> selectPage(Article Article, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.selectAll(Article);
        return PageInfo.of(list);
    }

    public List<Article> selectRecommend() {
        return articleMapper.selectRecommend();
    }

    public void updateReadCount(Integer id) {
        articleMapper.updateCount(id);
    }
}
