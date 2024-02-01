package com.cskaoyan.mall.order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.order.model.OrderInfo;
import com.cskaoyan.mall.order.query.OrderInfoParam;
import com.cskaoyan.mall.order.service.OrderService;
import com.cskaoyan.mall.ware.api.dto.WareOrderTaskDTO;
import com.cskaoyan.mall.ware.api.dto.WareSkuDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Boolean checkPrice(Long skuId, BigDecimal skuPrice) {
        return null;
    }

    @Override
    public void refreshPrice(Long skuId, String userId) {

    }

    @Override
    public Long saveOrderInfo(OrderInfo orderInfo) {
        return null;
    }

    @Override
    public OrderInfoDTO getOrderInfo(Long orderId) {
        return null;
    }

    @Override
    public IPage<OrderInfoDTO> getPage(Page<OrderInfoDTO> pageParam, String userId) {
        return null;
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

    }

    @Override
    public Long saveSeckillOrder(OrderInfoParam orderInfoParam) {
        return null;
    }
}
