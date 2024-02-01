package com.cskaoyan.mall.order.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.order.client.CartApiClient;
import com.cskaoyan.mall.order.client.UserApiClient;
import com.cskaoyan.mall.order.converter.CartInfoConverter;
import com.cskaoyan.mall.order.converter.OrderDetailConverter;
import com.cskaoyan.mall.order.converter.OrderInfoConverter;
import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/order/auth")
public class OrderController {
    @Autowired
    UserApiClient userApiClient;
    @Autowired
    CartApiClient cartApiClient;
    @Autowired
    CartInfoConverter cartInfoConverter;
    //交易确认页面
    @GetMapping("trade")
    public Result<OrderTradeDTO> trade(){
        //获取当前用户的userId
        String userId = (String) StpUtil.getLoginId();
        OrderTradeDTO orderTradeDTO = new OrderTradeDTO();
        orderTradeDTO.setUserAddressList(userApiClient.findUserAddressListByUserId(userId));
        List<OrderDetailDTO> orderDetailDTOS = cartInfoConverter.convertCartInfoDTOToOrderDetailDTOList(cartApiClient.getCartCheckedList(userId));
        orderTradeDTO.setDetailArrayList(orderDetailDTOS);
        orderTradeDTO.setTotalNum(orderDetailDTOS.size());
        //计算订单总额
        BigDecimal totalAmount = new BigDecimal(0);
        for(OrderDetailDTO orderDetailDTO : orderDetailDTOS){
            //计算每种商品的总价
            BigDecimal multiply = orderDetailDTO.getOrderPrice().multiply(new BigDecimal(orderDetailDTO.getSkuNum()));
            totalAmount = totalAmount.add(multiply);
        }
        orderTradeDTO.setTotalAmount(totalAmount);
        return Result.ok(orderTradeDTO);
    }
}
