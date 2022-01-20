package com.mjh.service;

import com.mjh.pojo.Carousel;
import com.mjh.pojo.bo.SubmitOrderBO;

import java.util.List;

public interface OrderService {

    /**
     * @Description //用于创建订单相关信息
     * @Date 15:30 2022/1/20
     * @Param [submitOrderBO]
     * @return java.util.List<com.mjh.pojo.Carousel>
     **/
    public String createOrder(SubmitOrderBO submitOrderBO);
}
