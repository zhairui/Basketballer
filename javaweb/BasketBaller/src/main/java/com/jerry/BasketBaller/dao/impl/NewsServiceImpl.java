package com.jerry.BasketBaller.dao.impl;

import com.github.pagehelper.PageHelper;
import com.jerry.BasketBaller.dao.INewsService;
import com.jerry.BasketBaller.mapper.NewsMapper;
import com.jerry.BasketBaller.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "newsService")
public class NewsServiceImpl implements INewsService {

    @Autowired
    NewsMapper newsMapper;
    @Override
    public List<News> getNewsList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return newsMapper.selectNewsList();
    }
}
