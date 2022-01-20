package com.mjh.service.impl;

import com.mjh.enums.CategoryLevel;
import com.mjh.mapper.CategoryMapper;
import com.mjh.mapper.CategoryMapperCustom;
import com.mjh.pojo.Category;
import com.mjh.pojo.vo.CategoryVO;
import com.mjh.pojo.vo.NewItemsVO;
import com.mjh.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @PackageName: com.mjh.service.impl
 * @ClassName: CarouselServiceImpl
 * @Author: majiahuan
 * @Date: 2020/11/8 20:07
 * @Description:
 */

@Service
public class CategoryServiceImpl implements CategoryService {
    // 注入数据访问层
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    // 查询所有一级分类
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllCategory() {
        // 1. 构造Category的查询对象
        Example example = new Example(Category.class);
        // 2. 创建查询条件
        Example.Criteria criteria = example.createCriteria();
        // 3. 添加查询条件 // 补充categoryType枚举类
        criteria.andEqualTo("type", CategoryLevel.ONELEVEL.type);
        // 4. 执行查询
        List<Category> categoryList = categoryMapper.selectByExample(example);
        return categoryList;
    }

    //查询二级分类及其子分类
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCateList(Integer rootCatId) {
        List subCatList = categoryMapperCustom.getSubCatList(rootCatId);
        return subCatList;
    }

    //根据一级分类Id查询6条最新商品数据
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId) {
        Map<String, Object> map = new HashMap<>();
        map.put("rootCatId", rootCatId);
        List<NewItemsVO> newItemsLazy = categoryMapperCustom.getSixNewItemsLazy(map);
        return newItemsLazy;
    }
}
