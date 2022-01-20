package com.mjh.service;

import com.mjh.pojo.Category;
import com.mjh.pojo.vo.CategoryVO;
import com.mjh.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {

    /**
     * @return java.util.List<com.mjh.pojo.Carousel>
     * @Description //查询所有根分类列表—一级分类
     * @Date 20:10 2020/11/8
     * @Param []
     **/
    public List<Category> queryAllCategory();

    /**
     * @Description //根据一级分类Id查询二级分类及其子分类
     * @Date 21:26 2020/11/9
     * @Param [rootCatId]
     * @return java.util.List<com.mjh.pojo.vo.CategoryVO>
     **/
    public List<CategoryVO> getSubCateList(Integer rootCatId);

    /**
     * @Description //根据一级分类Id查询6条最新商品数据
     * @Date 13:19 2020/11/29
     * @Param [rootCatId]
     * @return java.util.List
     **/
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
