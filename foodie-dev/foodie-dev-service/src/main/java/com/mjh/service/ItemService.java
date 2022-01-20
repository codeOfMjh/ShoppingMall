package com.mjh.service;

import com.mjh.pojo.Items;
import com.mjh.pojo.ItemsImg;
import com.mjh.pojo.ItemsParam;
import com.mjh.pojo.ItemsSpec;
import com.mjh.pojo.vo.CommentLevelVO;
import com.mjh.pojo.vo.ShopCartVO;
import com.mjh.utils.PagedGridResult;

import java.util.List;

public interface ItemService {
    /**
     * @return com.mjh.pojo.Items
     * @Description //根据商品Id查询商品信息
     * @Date 19:15 2020/11/29
     * @Param [itemId]
     **/
    public Items queryItemById(String itemId);

    /**
     * @return java.util.List<com.mjh.pojo.ItemsImg>
     * @Description //根据商品Id查询商品图片信息
     * @Date 19:19 2020/11/29
     * @Param [itemId]
     **/
    public List<ItemsImg> queryItemImgsById(String itemId);

    /**
     * @return java.util.List<com.mjh.pojo.ItemsSpec>
     * @Description //根据商品Id查询商品规格信息
     * @Date 19:21 2020/11/29
     * @Param [itemId]
     **/
    public List<ItemsSpec> queryItemSpecsById(String itemId);

    /**
     * @return java.util.List<com.mjh.pojo.ItemsParam>
     * @Description //根据商品Id查询商品参数信息
     * @Date 19:22 2020/11/29
     * @Param [itemId]
     **/
    public ItemsParam queryItemParamById(String itemId);

    /**
     * @return com.mjh.pojo.vo.CommentLevelVO
     * @Description //根据商品Id查询商品评论信息
     * @Date 22:17 2020/11/30
     * @Param [itemId]
     **/
    public CommentLevelVO queryCommentCounts(String itemId);

    /**
     * @return java.util.List<com.mjh.pojo.vo.ItemCommentVO>
     * @Description //根据商品Id和评价等级查询评论详情
     * @Date 22:34 2020/12/1
     * @Param [itemId, level]
     **/
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * @return com.mjh.utils.PagedGridResult
     * @Description // 根据搜索关键字查询商品，并按指定规则进行排序
     * @Date 18:09 2020/12/6
     * @Param [keyWords, sortRule, page, pageSize]
     **/
    public PagedGridResult queryPagedSearchItems(String keyWords, String sortRule, Integer page, Integer pageSize);


    /**
     * @return com.mjh.utils.PagedGridResult
     * @Description // 根据三级分类名查询商品信息，并按指定规则进行排序
     * @Date 22:29 2020/12/6
     * @Param [catId, sortRule, page, pageSize]
     **/
    public PagedGridResult queryPagedSearchItems(Integer catId, String sortRule, Integer page, Integer pageSize);

    /**
     * @return java.util.List<com.mjh.pojo.vo.ShopCartVO>
     * @Description // 根据商品Id列表查询对应商品信息
     * @Date 21:28 2020/12/7
     * @Param [specIds]
     **/
    public List<ShopCartVO> queryItemsBySpecIds(String specIds);

    /**
     * @return com.mjh.pojo.ItemsSpec
     * @Description //根据商品规格ID获取规格对象信息
     * @Date 16:47 2022/1/20
     * @Param [itemId]
     **/
    public ItemsSpec queryItemSpecById(String specId);

    /**
     * @return java.lang.String
     * @Description //根据商品ID获取商品主图的URL
     * @Date 17:06 2022/1/20
     * @Param [itemId]
     **/
    public String queryItemMainImgById(String itemId);

    /**
     * @Description //减少库存
     * @Date 17:37 2022/1/20
     * @Param [specId, buyCounts]
     * @return java.lang.String
     **/
    public void decreaseItemSpecStock(String specId, int buyCounts);
}
