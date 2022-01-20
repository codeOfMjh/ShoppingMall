package com.mjh.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mjh.enums.CommentLevel;
import com.mjh.enums.YesOrNo;
import com.mjh.mapper.*;
import com.mjh.pojo.*;
import com.mjh.pojo.vo.CommentLevelVO;
import com.mjh.pojo.vo.ItemCommentVO;
import com.mjh.pojo.vo.SearchItemsVO;
import com.mjh.pojo.vo.ShopCartVO;
import com.mjh.service.ItemService;
import com.mjh.utils.DesensitizationUtil;
import com.mjh.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @PackageName: com.mjh.service.impl
 * @ClassName: CarouselServiceImpl
 * @Author: majiahuan
 * @Date: 2020/11/8 20:07
 * @Description:
 */

@Service
public class ItemServiceImpl implements ItemService {
    // 注入数据访问层
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;

    //评论Mapper
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    //根据商品Id查询商品信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    //根据商品Id查询商品图片信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsImg> queryItemImgsById(String itemId) {
        //1. 构建Example对象
        Example itemsImgExp = new Example(ItemsImg.class);
        //2. 创建查询条件对象
        Example.Criteria criteria = itemsImgExp.createCriteria();
        //3. 设置查询条件
        criteria.andEqualTo("itemId", itemId);
        //4. 返回查询结果集
        return itemsImgMapper.selectByExample(itemsImgExp);
    }

    //根据商品Id查询商品规格信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ItemsSpec> queryItemSpecsById(String itemId) {
        //1. 构建Example对象
        Example itemsSpecExp = new Example(ItemsSpec.class);
        //2. 创建查询条件对象
        Example.Criteria criteria = itemsSpecExp.createCriteria();
        //3. 设置查询条件
        criteria.andEqualTo("itemId", itemId);
        //4. 返回查询结果集
        return itemsSpecMapper.selectByExample(itemsSpecExp);
    }

    //根据商品Id查询商品参数信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsParam queryItemParamById(String itemId) {
        //1. 构建Example对象
        Example itemsParamExp = new Example(ItemsParam.class);
        //2. 创建查询条件对象
        Example.Criteria criteria = itemsParamExp.createCriteria();
        //3. 设置查询条件
        criteria.andEqualTo("itemId", itemId);
        //4. 返回查询结果集
        return itemsParamMapper.selectOneByExample(itemsParamExp);
    }

    //根据商品Id查询商品评论信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public CommentLevelVO queryCommentCounts(String itemId) {
        //分别查询出每类评价的数据，并计算总数
        Integer goodCounts = getComments(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getComments(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getComments(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        //创建封装评论总数的对象
        CommentLevelVO commentLevelVO = new CommentLevelVO();
        commentLevelVO.setTotalCounts(totalCounts);
        commentLevelVO.setGoodCounts(goodCounts);
        commentLevelVO.setNormalCounts(normalCounts);
        commentLevelVO.setBadCounts(badCounts);

        //返回封装好数据
        return commentLevelVO;
    }

    //根据商品Id和评价等级查询评论详情
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        // 查询之前使用PageHelper进行分页；Page-页数，PageSize-每页显示条数
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVO> itemComments = itemsMapperCustom.queryItemComments(map);
        // 进行用户昵称脱敏
        for (ItemCommentVO itemComment : itemComments) {
            // 获取从数据库查询的用户昵称
            String nickname = itemComment.getNickname();
            // 对昵称进行脱敏
            String name = DesensitizationUtil.commonDisplay(nickname);
            // 设置脱敏后的昵称
            itemComment.setNickname(name);
        }
        // 调用分页的方法
        PagedGridResult gridResult = getPagedGrid(page, itemComments);
        // 返回分页后的数据
        return gridResult;
    }

    // 根据搜索关键字查询商品，并按指定规则进行排序
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedSearchItems(String keyWords, String sortRule, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keyWords);
        map.put("sort", sortRule);
        // 查询之前使用PageHelper进行分页；Page-页数，PageSize-每页显示条数
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItems = itemsMapperCustom.searchItems(map);
        PagedGridResult pagedGrid = getPagedGrid(page, searchItems);
        return pagedGrid;
    }

    // 根据三级分类名查询商品信息，并按指定规则进行排序
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedGridResult queryPagedSearchItems(Integer catId, String sortRule, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sortRule);
        // 查询之前使用PageHelper进行分页；Page-页数，PageSize-每页显示条数
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItems = itemsMapperCustom.searchItemsByThirdCategory(map);
        PagedGridResult pagedGrid = getPagedGrid(page, searchItems);
        return pagedGrid;
    }

    // 根据商品Id列表查询对应商品信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {
        // 1. 获取Id集合
        String[] Ids = specIds.split(",");
        // 2. 创建目标集合
        List<String> specIdsList = new ArrayList<>();
        // 3. 使用工具类对目标集合进行元素添加
        Collections.addAll(specIdsList, Ids);
        List<ShopCartVO> shopCarts = itemsMapperCustom.queryItemsBySpecIds(specIdsList);
        return shopCarts;
    }

    // 根据商品规格ID获取规格对象信息
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    // 根据商品ID获取商品主图的URL
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public String queryItemMainImgById(String itemId) {
        // 根据商品Id构造查询对象
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        // 使用构造对象查询主图URL
        ItemsImg itemMainImg = itemsImgMapper.selectOne(itemsImg);
        return itemMainImg != null ? itemMainImg.getUrl() : "";
    }

    // 减少库存
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseItemSpecStock(String specId, int buyCounts) {
        /**
         * @Description // TODO 超卖问题处理
         * @Date 17:44 2022/1/20
         * @Param [specId, buyCounts]
         * @return java.lang.String
         * synchronized 不推荐使用，集群环境下失效，性能低下
         * 锁数据库，导致数据库性能低下，不使用
         * 分布式锁，zookeeper，Redis
         **/
        int stock = itemsMapperCustom.decreaseItemSpecStock(specId, buyCounts);
        if (stock != 1) {
            throw new RuntimeException("库存不足，订单创建失败");
        }
    }

    // 进行数据封装
    //@Transactional(propagation = Propagation.SUPPORTS)
    private PagedGridResult getPagedGrid(Integer page, List list) {
        PageInfo<ItemCommentVO> commentPageInfo = new PageInfo<ItemCommentVO>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page);
        gridResult.setRows(list);
        gridResult.setTotal(commentPageInfo.getPages());
        gridResult.setRecords(commentPageInfo.getTotal());
        return gridResult;
    }

    // 获取评论总数
    //@Transactional(propagation = Propagation.SUPPORTS)
    private Integer getComments(String itemId, Integer level) {
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        if (level != null) {
            itemsComments.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(itemsComments);
    }
}
