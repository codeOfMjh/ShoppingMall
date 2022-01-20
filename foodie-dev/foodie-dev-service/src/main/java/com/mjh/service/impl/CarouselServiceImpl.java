package com.mjh.service.impl;

import com.mjh.mapper.CarouselMapper;
import com.mjh.pojo.Carousel;
import com.mjh.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @PackageName: com.mjh.service.impl
 * @ClassName: CarouselServiceImpl
 * @Author: majiahuan
 * @Date: 2020/11/8 20:07
 * @Description:
 */

@Service
public class CarouselServiceImpl implements CarouselService {
    // 注入数据访问层
    @Autowired
    private CarouselMapper carouselMapper;

    // 查询所有轮播图
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAllCarousel(Integer isShow) {
        // 1. 构造Carousel的查询对象
        Example example = new Example(Carousel.class);
        // 1.1 为轮播图排序;倒序排列
        example.orderBy("sort").desc();
        // 2. 创建查询条件
        Example.Criteria criteria = example.createCriteria();
        // 3. 添加查询条件
        criteria.andEqualTo("isShow", isShow);
        // 4. 执行查询
        List<Carousel> carouselList = carouselMapper.selectByExample(example);
        return carouselList;
    }
}
