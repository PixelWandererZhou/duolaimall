package com.cskaoyan.mall.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.order.client.CartApiClient;
import com.cskaoyan.mall.order.client.ProductApiClient;
import com.cskaoyan.mall.order.client.UserApiClient;
import com.cskaoyan.mall.order.constant.OrderStatus;
import com.cskaoyan.mall.order.converter.CartInfoConverter;
import com.cskaoyan.mall.order.converter.OrderDetailConverter;
import com.cskaoyan.mall.order.converter.OrderInfoConverter;
import com.cskaoyan.mall.order.dto.OrderDetailDTO;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.dto.OrderTradeDTO;
import com.cskaoyan.mall.order.mapper.OrderDetailMapper;
import com.cskaoyan.mall.order.mapper.OrderInfoMapper;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.order.service.OrderService;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareSkuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    UserApiClient userApiClient;
    @Autowired
    ProductApiClient productApiClient;
    @Autowired
    CartApiClient cartApiClient;
    @Autowired
    CartInfoConverter cartInfoConverter;
    @Autowired
    OrderInfoConverter orderInfoConverter;
    @Autowired
    OrderDetailConverter orderDetailConverter;
    @Override
    public OrderTradeDTO trade(String userId) {
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
        return orderTradeDTO;
    }

    @Override
    public Boolean checkPrice(Long skuId, BigDecimal skuPrice) {
        if (productApiClient.getSkuPrice(skuId).compareTo(skuPrice)==0) {
            return false;
        }
        return true;
    }

    @Override
    public void refreshPrice(Long skuId, String userId) {

    }

    @Override
    public Long saveOrderInfo(OrderInfo orderInfo) {
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        orderInfoMapper.insert(orderInfo);
        return orderInfo.getId();
    }

    @Override
    public OrderInfoDTO getOrderInfo(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        return orderInfoConverter.convertOrderInfoToOrderInfoDTO(orderInfo);
    }

    @Override
    public void updateOrderInfo(OrderInfo orderInfo) {
        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public Page<OrderInfoDTO> getPage(Page<OrderInfo> pageParam, String userId) {
        Page<OrderInfo> orderInfoPage = orderInfoMapper.selectPage(pageParam, null);
        Page<OrderInfoDTO> orderInfoDTOPage = orderInfoConverter.convertOrderInfoPageToOrderInfoDTOPage(orderInfoPage);
        return orderInfoDTOPage;
    }

    @Override
    public void successPay(Long orderId) {

    }

    @Override
    public void successLockStock(String orderId, String taskStatus) {

    }

    @Override
    public List<WareOrderTaskDTO> orderSplit(String orderId, List<WareSkuDTO> wareSkuDTOList) {
        return null;
    }

    @Override
    public void execExpiredOrder(Long orderId) {
        //取消订单
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.setOrderStatus(OrderStatus.CLOSED.name());
        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public Long saveSeckillOrder(OrderInfoParam orderInfoParam) {
        return null;
    }
}
