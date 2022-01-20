package com.mjh.controller;

import com.mjh.pojo.bo.ShopCartBO;
import com.mjh.service.ItemService;
import com.mjh.utils.MJHJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @PackageName: com.mjh.controller
 * @ClassName: HelloController
 * @Author: majiahuan
 * @Date: 2020/10/6 21:23
 * @Description:
 */

@Api(value = "购物车", tags = {"购物车展示的相关接口"}) //用于生成api文档说明
@RestController//声明当前类为前端访问控制层(即所有请求都必须经过该类)
@RequestMapping("shopcart")//设置当前类资源的访问uri标识
public class ShopCartController {
    //注入service业务逻辑层
    @Autowired  //商品service
    private ItemService itemService;

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "添加购物车数据", notes = "添加购物车数据", httpMethod = "POST")
    @PostMapping("/add")
    public MJHJSONResult addCart(@RequestParam String userId,
                                 @RequestBody ShopCartBO shopCartBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return MJHJSONResult.errorMsg("用户Id不能为null");
        }
        // todo 前端用户在登录情况下，添加商品到购物车；会同时在后端同步购物车到redis缓存
        return MJHJSONResult.ok();
    }

    // 用于为当前方法生成API文档注释
    @ApiOperation(value = "删除购物车数据", notes = "删除购物车数据", httpMethod = "POST")
    @PostMapping("/del")
    public MJHJSONResult delCart(@RequestParam String userId,
                                 @RequestParam String itemSpecId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return MJHJSONResult.errorMsg("用户Id不能为null");
        }
        
        // todo 前端用户在登录情况下，从购物车删除商品；会同时在后端同步购物车到redis缓存
        return MJHJSONResult.ok();
    }
}
