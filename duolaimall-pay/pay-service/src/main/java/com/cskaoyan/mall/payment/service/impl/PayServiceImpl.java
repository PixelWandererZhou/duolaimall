package com.cskaoyan.mall.payment.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.cskaoyan.mall.order.dto.OrderInfoDTO;
import com.cskaoyan.mall.pay.api.dto.PaymentInfoDTO;
import com.cskaoyan.mall.payment.client.OrderApiClient;
import com.cskaoyan.mall.payment.constant.PaymentStatus;
import com.cskaoyan.mall.payment.converter.PaymentInfoConverter;
import com.cskaoyan.mall.payment.mapper.PaymentInfoMapper;
import com.cskaoyan.mall.payment.model.PaymentInfo;
import com.cskaoyan.mall.payment.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class PayServiceImpl implements PayService {
    @Autowired
    AlipayClient alipayClient;
    @Autowired
    OrderApiClient orderApiClient;
    @Autowired
    PaymentInfoConverter paymentInfoConverter;
    @Autowired
    PaymentInfoMapper paymentInfoMapper;
    @Override
    public String createAliPay(Long orderId) throws AlipayApiException {
        //查对应的订单信息
        OrderInfoDTO orderInfoDTO = orderApiClient.getOrderInfoDTO(orderId);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderInfoDTO.getOutTradeNo());
        model.setTotalAmount(orderInfoDTO.getTotalAmount().toString());
        model.setSubject(orderInfoDTO.getTradeBody());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(model);
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        return response.getBody();
    }

    @Override
    public void savePaymentInfo(OrderInfoDTO orderInfo, String paymentTypeName) {
        PaymentInfo paymentInfo = paymentInfoConverter.contvertOrderInfoDTO2PaymentInfo(orderInfo);
        paymentInfo.setPaymentType(paymentTypeName);
        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public PaymentInfoDTO queryPaymentInfoByOutTradeNoAndPaymentType(String outTradeNo, String payTypeName) {
        return null;
    }

    @Override
    public Boolean successPay(String outTradeNo, String name, Map<String, String> paramsMap) {
        return null;
    }

    @Override
    public void updatePaymentStatus(String outTradeNo, String name, PaymentStatus paymentStatus) {

    }
}
