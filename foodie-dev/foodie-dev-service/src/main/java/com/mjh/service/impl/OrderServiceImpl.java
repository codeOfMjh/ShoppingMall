package com.mjh.service.impl;

import com.mjh.enums.OrderStatusEnum;
import com.mjh.enums.YesOrNo;
import com.mjh.mapper.OrderItemsMapper;
import com.mjh.mapper.OrderStatusMapper;
import com.mjh.mapper.OrdersMapper;
import com.mjh.pojo.*;
import com.mjh.pojo.bo.SubmitOrderBO;
import com.mjh.service.ItemService;
import com.mjh.service.OrderService;
import com.mjh.service.UserAddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @PackageName: com.mjh.service.impl
 * @ClassName: CarouselServiceImpl
 * @Author: majiahuan
 * @Date: 2020/11/8 20:07
 * @Description:
 */

@Service
public class OrderServiceImpl implements OrderService {
    // 注入订单数据访问层
    @Autowired
    private OrdersMapper ordersMapper;

    // 注入子订单数据访问层
    @Autowired
    private OrderItemsMapper orderItemsMapper;

    // 注入订单状态数据访问层
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    // 注入Sid
    @Autowired
    private Sid sid;

    // 注入地址业务层
    @Autowired
    private UserAddressService userAddressService;

    // 注入商品信息业务层
    @Autowired
    private ItemService itemService;

    // 用于创建订单相关信息
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 默认包邮
        Integer postAmount = 0;
        // 生成订单ID
        String orderId = this.sid.nextShort();
        // 根据用户ID和地址ID获取具体地址信息
        UserAddress userAddress = userAddressService.queryUserAddres(userId, addressId);
        // 拼接地址
        String address = userAddress.getProvince() + " " + userAddress.getCity() + " "
                + userAddress.getDistrict() + " " + userAddress.getDetail();
        // 1. 新订单数据保存
        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setReceiverName(userAddress.getReceiver());
        newOrder.setReceiverMobile(userAddress.getMobile());
        newOrder.setReceiverAddress(address);
        newOrder.setPostAmount(postAmount);
        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());
        // 2. 根据itemSpecIds循环保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String itemSpaecId : itemSpecIdArr) {
            // todo 整合Redis后，商品购买的数量重新从Redis中获取
            int buyCounts = 1;
            // 2.1 根据规格Id，查询规格信息，获取商品价格
            ItemsSpec itemsSpec = itemService.queryItemSpecById(itemSpaecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemsSpec.getPriceDiscount() * buyCounts;
            // 2.2 根据商品Id，查询商品信息以及图片
            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);
            // 2.3 循环保存子订单到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItems = new OrderItems();
            subOrderItems.setId(subOrderId);
            subOrderItems.setOrderId(orderId);
            subOrderItems.setItemId(itemId);
            subOrderItems.setItemName(item.getItemName());
            subOrderItems.setItemImg(imgUrl);
            subOrderItems.setBuyCounts(buyCounts);
            subOrderItems.setItemSpecId(itemSpaecId);
            subOrderItems.setItemSpecName(itemsSpec.getName());
            subOrderItems.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItems);
            // 2.4 用户提交订单以后，规格表中需要扣除库存数量
            itemService.decreaseItemSpecStock(itemSpaecId, buyCounts);
        }
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);
        // 3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);
        return orderId;
    }
}
