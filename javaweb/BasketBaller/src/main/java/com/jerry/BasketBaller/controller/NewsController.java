package com.jerry.BasketBaller.controller;

import com.jerry.BasketBaller.common.ServerResponse;
import com.jerry.BasketBaller.dao.INewsService;
import com.jerry.BasketBaller.model.News;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "NewsController",description = "新闻相关")
@RequestMapping("/news/")
public class NewsController {

    @Autowired
    private INewsService newsService;

    @ResponseBody
    @RequestMapping(value = "newslist",method = RequestMethod.GET)
    @ApiOperation(value = "新闻列表")
    //@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
//    @PostAuthorize("hasRole('ROLE_ADMIN')")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Secured("ROLE_ADMIN")
    public ServerResponse<List<News>> getNewsList(@RequestParam int pageNum,@RequestParam int pageSize){
        List<News> newsList = newsService.getNewsList(pageNum,pageSize);
        return ServerResponse.createBySuccess(newsList);
    }
}
