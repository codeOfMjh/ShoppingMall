package com.mjh.service;

import com.mjh.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    /**
     * @Description //查询所有轮播图列表
     * @Date 20:10 2020/11/8
     * @Param []
     * @return java.util.List<com.mjh.pojo.Carousel>
     **/
    public List<Carousel> queryAllCarousel(Integer isShow);
}
