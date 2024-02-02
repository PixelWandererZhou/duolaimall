package com.cskaoyan.mall.order.mq;

import com.cskaoyan.mall.order.constant.OrderStatus;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.service.OrderService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "orderDelayTopic", consumerGroup = "${rocketmq.consumer.group}")
public class OrderCancelCustomer implements RocketMQListener<String> {
    @Autowired
    OrderService orderService;
    @Override
    public void onMessage(String orderId) {
        // 1. 查询订单状态
        // 2. 如果是未支付，取消订单
        // 3. 如果是已支付，不做处理
        OrderInfoDTO orderInfo = orderService.getOrderInfo(Long.valueOf(orderId));
        if(orderInfo.getOrderStatus().equals(OrderStatus.UNPAID.name())){
            orderService.execExpiredOrder(Long.valueOf(orderId));
        }
    }
}