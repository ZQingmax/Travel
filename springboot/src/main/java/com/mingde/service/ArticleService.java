package com.mingde.service;

import com.mingde.common.PageUtils;
import com.mingde.common.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.date.DateUtil;
import com.mingde.entity.Article;
import com.mingde.mapper.ArticleMapper;
import com.mingde.mapper.CollectMapper;
import com.mingde.utils.AuthUtils;
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
        AuthUtils.requireAdmin();
        article.setReadCount(0);
        article.setDate(DateUtil.now());
        articleMapper.insert(article);
    }

    public void updateById(Article article) {
        AuthUtils.requireAdmin();
        articleMapper.updateById(article);
    }

    public void deleteById(Integer id) {
        AuthUtils.requireAdmin();
        articleMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        AuthUtils.requireAdmin();
        for (Integer id : ids) {
            articleMapper.deleteById(id);
        }
    }

    public Article selectById(Integer id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            Integer count = collectMapper.selectByFid(article.getId());
            article.setCollectCount(count);
        }
        return article;
    }

    public List<Article> selectAll(Article article) {
        return articleMapper.selectAll(article);
    }

    public PageResult<Article> selectPage(Article article, Integer pageNum, Integer pageSize) {
        Page<Article> page = PageUtils.page(pageNum, pageSize);
        IPage<Article> result = articleMapper.selectPage(page, article);
        return PageUtils.toResult(result);
    }

    public List<Article> selectRecommend() {
        return articleMapper.selectRecommend();
    }

    public void updateReadCount(Integer id) {
        articleMapper.updateCount(id);
    }
}
