package com.mjh.controller;

import com.mjh.pojo.Items;
import com.mjh.pojo.ItemsImg;
import com.mjh.pojo.ItemsParam;
import com.mjh.pojo.ItemsSpec;
import com.mjh.pojo.vo.CommentLevelVO;
import com.mjh.pojo.vo.ItemInfoVO;
import com.mjh.pojo.vo.ShopCartVO;
import com.mjh.service.ItemService;
import com.mjh.utils.MJHJSONResult;
import com.mjh.utils.PagedGridResult;
import com.mjh.utils.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @PackageName: com.mjh.controller
 * @ClassName: HelloController
 * @Author: majiahuan
 * @Date: 2020/10/6 21:23
 * @Description:
 */

@Api(value = "商品详情", tags = {"商品详情展示的相关接口"}) //用于生成api文档说明
@RestController//声明当前类为前端访问控制层(即所有请求都必须经过该类)
@RequestMapping("items")//设置当前类资源的访问uri标识
public class ItemsController {
    //注入service业务逻辑层
    @Autowired  //首页展示相关
    private ItemService itemService;

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public MJHJSONResult getItemInfo(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return MJHJSONResult.errorMsg(null);
        }

        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgsById(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecsById(itemId);
        ItemsParam itemsParam = itemService.queryItemParamById(itemId);

        //创建用于返回给前端数据的对象
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);
        return MJHJSONResult.ok(itemInfoVO);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public MJHJSONResult getCommentLevel(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return MJHJSONResult.errorMsg(null);
        }

        CommentLevelVO commentCounts = itemService.queryCommentCounts(itemId);
        return MJHJSONResult.ok(commentCounts);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "查询商品评价详情", notes = "查询商品评价详情", httpMethod = "GET")
    @GetMapping("/comments")
    public MJHJSONResult getCommentDetails(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评论等级")
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "当前页数")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页显示记录数")
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return MJHJSONResult.errorMsg(null);
        }

        // 为page和pageSize赋初值
        if (page == null || pageSize == null) {
            page = ProjectConstant.PAGE_NUMBER;
            pageSize = ProjectConstant.COMMENT_PAGE_SIZE_COUNTS;
        }

        PagedGridResult gridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return MJHJSONResult.ok(gridResult);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public MJHJSONResult getSearchItems(
            @ApiParam(name = "keywords", value = "搜索关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序方式")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页数")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页显示记录数")
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return MJHJSONResult.errorMsg(null);
        }

        // 为page和pageSize赋初值
        if (page == null || pageSize == null) {
            page = ProjectConstant.PAGE_NUMBER;
            pageSize = ProjectConstant.PAGE_SIZE_COUNTS;
        }

        PagedGridResult gridResult = itemService.queryPagedSearchItems(keywords, sort, page, pageSize);
        return MJHJSONResult.ok(gridResult);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "根据三级Id搜索商品列表", notes = "根据三级Id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public MJHJSONResult getSearchItemsByCatId(
            @ApiParam(name = "catId", value = "三级分类Id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序方式")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "当前页数")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页显示记录数")
            @RequestParam Integer pageSize) {
        if (catId == null) {
            return MJHJSONResult.errorMsg(null);
        }

        // 为page和pageSize赋初值
        if (page == null || pageSize == null) {
            page = ProjectConstant.PAGE_NUMBER;
            pageSize = ProjectConstant.PAGE_SIZE_COUNTS;
        }

        PagedGridResult gridResult = itemService.queryPagedSearchItems(catId, sort, page, pageSize);
        return MJHJSONResult.ok(gridResult);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "根据商品规格Ids查询商品最新信息", notes = "根据商品规格Ids查询商品最新信息", httpMethod = "GET")
    @GetMapping("/refresh")
    public MJHJSONResult getItemsBySpecIds(
            @ApiParam(name = "itemSpecIds", value = "商品规格数组", required = true, example = "1,2,3")
            @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return MJHJSONResult.errorMsg(null);
        }
        List<ShopCartVO> shopCarts = itemService.queryItemsBySpecIds(itemSpecIds);
        return MJHJSONResult.ok(shopCarts);
    }
}
