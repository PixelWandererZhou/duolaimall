package com.cskaoyan.mall.payment.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.payment.client.OrderApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayApiController {
    @Autowired
    OrderApiClient orderApiClient;
    @GetMapping("pay/auth")
    public Result<OrderInfoDTO> payIndex(Long orderId){
        // 调用订单服务获取订单信息
        OrderInfoDTO orderInfoDTO = orderApiClient.getOrderInfoDTO(orderId);
        return Result.ok(orderInfoDTO);
    }

}
