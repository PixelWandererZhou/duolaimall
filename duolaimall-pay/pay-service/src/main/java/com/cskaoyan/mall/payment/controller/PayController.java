package com.cskaoyan.mall.payment.controller;

import com.alipay.api.AlipayApiException;
import com.cskaoyan.mall.order.constant.OrderStatus;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.payment.client.OrderApiClient;
import com.cskaoyan.mall.payment.constant.PaymentType;
import com.cskaoyan.mall.payment.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {
    @Autowired
    OrderApiClient orderApiClient;
    @Autowired
    PayService payService;
    @RequestMapping("alipay/submit/{orderId}")
    public String submitOrder(@PathVariable Long orderId) throws AlipayApiException {
        // 1. 校验支付对应的订单状态是否为未支付，如果是已支付或已关闭，则直接返回
        OrderInfoDTO orderInfoDTO = orderApiClient.getOrderInfoDTO(orderId);
        if(orderInfoDTO.getOrderStatus().equals(OrderStatus.PAID.name()) && orderInfoDTO.getOrderStatus().equals(OrderStatus.CLOSED.name())){
            return payService.createAliPay(orderId);
        }
        // 2. 保存支付记录到支付表
        payService.savePaymentInfo(orderInfoDTO, PaymentType.ALIPAY.name());
        // 3. 调用支付宝SDK，生成支付表单（支付表单实际上就是一个支付页面，是一个html的字符串）
        // 4. 返回支付表单
        return payService.createAliPay(orderId);
    }
}
