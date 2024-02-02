package com.cskaoyan.mall.order.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.common.constant.ResultCodeEnum;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.client.CartApiClient;
import com.cskaoyan.mall.order.client.WareApiClient;
import com.cskaoyan.mall.order.converter.OrderInfoConverter;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderDetailParam;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.order.service.OrderService;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order/auth")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CartApiClient cartApiClient;
    @Autowired
    WareApiClient wareApiClient;
    @Autowired
    OrderInfoConverter orderInfoConverter;
    @Autowired
    RocketMQTemplate rocketMQTemplate;
    //交易确认页面
    @GetMapping("trade")
    public Result<OrderTradeDTO> trade(){
        //获取当前用户的userId
        String userId = (String) StpUtil.getLoginId();
        return Result.ok(orderService.trade(userId));
    }
    @PostMapping("submitOrder")
    public Result submitOrder(@RequestBody OrderInfoParam orderInfoParam) {
        //获取用户id
        String userId = (String) StpUtil.getLoginId();
        //将购物车商品转换为订单详情
        List<OrderDetailParam> orderDetailList = orderInfoParam.getOrderDetailList();
        // 1. 校验库存是否足够
        for(OrderDetailParam orderDetailParam : orderDetailList){
            Result result = wareApiClient.hasStock(orderDetailParam.getSkuId(), orderDetailParam.getSkuNum());
            if(result.getCode()!=200)
                return result;
        }
        // 2. 校验商品价格是否产生变动
        Boolean isModified = false;
        for(OrderDetailParam orderDetailParam : orderDetailList){
            isModified = orderService.checkPrice(orderDetailParam.getSkuId(), orderDetailParam.getOrderPrice());
            if(isModified){
                // 价格发生变动，刷新购物车中的价格
                cartApiClient.refreshCartPrice(userId, orderDetailParam.getSkuId());
            }
        }
        if(isModified){
            return Result.build(Result.fail(), ResultCodeEnum.ORDER_PRICE_CHANGED);
        }
        // 3. 保存订单以及订单详情
        OrderInfo orderInfo = orderInfoConverter.convertOrderInfoParam(orderInfoParam);
        orderInfo.setUserId(Long.valueOf(userId));
        orderService.saveOrderInfo(orderInfo);
        // 4. 删除购物车中已经下单的商品
        List<CartInfoDTO> cartInfoDTOList = cartApiClient.getCartCheckedList(userId);
        //获取购物车中已经下单的商品的skuId集合
        List<Long> skuIds = cartInfoDTOList.stream().map(CartInfoDTO::getSkuId).collect(Collectors.toList());
        cartApiClient.removeCartProductsInOrder(userId, skuIds);
        // 5. 发送订单超时自动取消的消息
        // 创建异步延时消息，延时10分钟
        Long orderId = orderInfo.getId();
        long currentTimeMillis = System.currentTimeMillis();
        rocketMQTemplate.asyncSend("orderDelayTopic", MessageBuilder.withPayload(orderInfo.getId()).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                //设置orderInfo的过期时间
                //根据orderId查询订单
                OrderInfo unUpdatedOrderInfo = orderInfoConverter.copyOrderInfo(orderService.getOrderInfo(orderId));
                unUpdatedOrderInfo.setExpireTime(new Date(currentTimeMillis + 1000 * 60 * 10));
                orderService.updateOrderInfo(unUpdatedOrderInfo);
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送订单超时自动取消的消息失败");
            }
        }, 1000 * 60 * 10);
        return Result.ok();
    }
    @GetMapping("{page}/{limit}")
    public Result<IPage<OrderInfoDTO>> index(@PathVariable Long page, @PathVariable Long limit) {
        // 获取到用户Id
        String userId = (String) StpUtil.getLoginId();


        Page<OrderInfo> pageParam = new Page<>(page, limit);

        // 获取用户订单的分页列表
        // 注意：
        // 1. 建议使用MybatisPlus的分页查询功能
        // 2. 思考一下哪些订单状态不用在用户页面展示出来？
        // 3. 查询结果需要设置OrderInfoDTO中的orderStatusName(如'未支付'| '已支付' | '已发货')
        IPage<OrderInfoDTO> pageModel = orderService.getPage(pageParam, userId);

        return Result.ok(pageModel);
    }
}
