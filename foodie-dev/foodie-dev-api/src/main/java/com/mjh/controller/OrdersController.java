package com.mjh.controller;

import com.mjh.pojo.bo.SubmitOrderBO;
import com.mjh.service.OrderService;
import com.mjh.utils.CookieUtils;
import com.mjh.utils.MJHJSONResult;
import com.mjh.utils.ProjectConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController {
    // 注入业务层 todo 订单业务逻辑层
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public MJHJSONResult createOrder(@RequestBody SubmitOrderBO submitOrderBO,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        System.out.println(submitOrderBO.toString());
        // 1. 创建订单
        String orderId = orderService.createOrder(submitOrderBO);
        // 2. 创建订单以后，移除购物车中已结算(已提交)的商品
        // todo 整合订单以后，完善购物车中的已结算商品清除，并且同步前端cookie中
        // CookieUtils.setCookie(request, response, ProjectConstant.FOODIE_SHORTCART, "", true);
        // 3. 向支付中心发送当前订单，保存支付中心的订单数据
        return MJHJSONResult.ok(orderId);
    }
}
