package com.cskaoyan.mall.order.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderApiController {
    @Autowired
    OrderService orderService;
    @GetMapping("/api/order/inner/getOrderInfo/{orderId}")
    public OrderInfoDTO getOrderInfoDTO(@PathVariable(value = "orderId") Long orderId){
        return orderService.getOrderInfo(orderId);
    }


    // 支付成功，修改订单状态
    @PostMapping("/api/order/inner/success/{orderId}")
    Result successPay(@PathVariable(value = "orderId") Long orderId){
        orderService.successPay(orderId);
        return Result.ok();
    }
}
