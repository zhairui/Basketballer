package com.jerry.BasketBaller.dao;

import com.jerry.BasketBaller.model.News;

import java.util.List;

public interface INewsService {

    List<News> getNewsList(int pageNum,int pageSize);
}
