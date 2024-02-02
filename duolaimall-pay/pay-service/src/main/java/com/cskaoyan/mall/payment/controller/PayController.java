package com.cskaoyan.mall.payment.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {
    @RequestMapping("alipay/submit/{orderId}")
    @ResponseBody
    public String submitOrder(@PathVariable Long orderId){

        //String form = alipayService.createAliPay(orderId);
        // 在上面这个支付的方法中，需要做的事情有如下几个

        // 1. 校验支付对应的订单状态是否为未支付，如果是已支付或已关闭，则直接返回

        // 2. 保存支付记录到支付表

        // 3. 调用支付宝SDK，生成支付表单（支付表单实际上就是一个支付页面，是一个html的字符串）

        // 4. 返回支付表单
        return null;
    }
}
