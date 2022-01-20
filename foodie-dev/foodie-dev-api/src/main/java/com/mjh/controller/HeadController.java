package com.mjh.controller;

import com.mjh.enums.YesOrNo;
import com.mjh.pojo.Carousel;
import com.mjh.pojo.Category;
import com.mjh.pojo.Users;
import com.mjh.pojo.bo.UserBO;
import com.mjh.pojo.vo.CategoryVO;
import com.mjh.pojo.vo.NewItemsVO;
import com.mjh.service.CarouselService;
import com.mjh.service.CategoryService;
import com.mjh.service.UserService;
import com.mjh.utils.CookieUtils;
import com.mjh.utils.JsonUtils;
import com.mjh.utils.MD5Utils;
import com.mjh.utils.MJHJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @PackageName: com.mjh.controller
 * @ClassName: HelloController
 * @Author: majiahuan
 * @Date: 2020/10/6 21:23
 * @Description:
 */

@Api(value = "首页", tags = {"首页展示的相关接口"}) //用于生成api文档说明
@RestController//声明当前类为前端访问控制层(即所有请求都必须经过该类)
@RequestMapping("index")//设置当前类资源的访问uri标识
public class HeadController {
    //注入service业务逻辑层
    @Autowired  //首页展示相关
    private CarouselService carouselService;
    @Autowired  //分类列表相关
    private CategoryService categoryService;

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "轮播图查询", notes = "轮播图集合", httpMethod = "GET")
    @GetMapping("/carousel")
    public MJHJSONResult carousel() {
        //1. 查询所有轮播图列表
        List<Carousel> carouselList = carouselService.queryAllCarousel(YesOrNo.YES.type);
        //2. 返回查询结果
        return MJHJSONResult.ok(carouselList);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "获取商品分类(一级分类)", notes = "获取商品分类(一级分类)", httpMethod = "GET")
    @GetMapping("/cats")
    public MJHJSONResult category() {
            List<Category> categoryList = categoryService.queryAllCategory();
        return MJHJSONResult.ok(categoryList);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "查询二级分类及其子分类", notes = "查询二级分类及其子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public MJHJSONResult getSubCateList(
            @ApiParam(name = "rootCatId", value = "一级分类Id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return MJHJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> subCateList = categoryService.getSubCateList(rootCatId);
        return MJHJSONResult.ok(subCateList);
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "查询一级分类下的最新6条数据", notes = "查询一级分类下的最新6条数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public MJHJSONResult getSixNewItemsLazy(
            @ApiParam(name = "rootCatId", value = "一级分类Id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return MJHJSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> subCateList = categoryService.getSixNewItemsLazy(rootCatId);
        return MJHJSONResult.ok(subCateList);
    }

    // 退出登录
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public MJHJSONResult logout() {
        return MJHJSONResult.ok();
    }
}
