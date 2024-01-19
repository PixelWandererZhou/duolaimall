package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.SkuInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SkuInfoPageConverter;
import com.cskaoyan.mall.product.converter.param.SkuInfoParamConverter;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoPageDTO;
import com.cskaoyan.mall.product.dto.SpuSaleAttributeInfoDTO;
import com.cskaoyan.mall.product.mapper.SkuInfoMapper;
import com.cskaoyan.mall.product.mapper.SkuPlatformAttrValueMapper;
import com.cskaoyan.mall.product.mapper.SkuSaleAttrValueMapper;
import com.cskaoyan.mall.product.model.SkuInfo;
import com.cskaoyan.mall.product.model.SkuPlatformAttributeValue;
import com.cskaoyan.mall.product.model.SkuSaleAttributeValue;
import com.cskaoyan.mall.product.query.SkuInfoParam;
import com.cskaoyan.mall.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuPlatformAttrValueMapper skuPlatformAttrValueMapper;
    @Autowired
    SkuInfoConverter skuInfoConverter;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SkuInfoParamConverter skuInfoParamConverter;
    @Autowired
    SkuInfoPageConverter skuInfoPageConverter;
    @Override
    public void saveSkuInfo(SkuInfoParam skuInfoParam) {
        // 保存skuInfo信息
        SkuInfo skuInfo = skuInfoParamConverter.SkuInfoParam2Info(skuInfoParam);
        skuInfoMapper.insert(skuInfo);
        // 获取skuId
        Long skuId = skuInfo.getId();
        // 保存skuAttrValue信息
        skuInfoParam.getSkuAttrValueList().forEach(skuAttrValueParam -> {
            SkuPlatformAttributeValue skuPlatformAttributeValue = skuInfoParamConverter.skuPlatformAttributeValueParam2Value(skuAttrValueParam);
            skuPlatformAttributeValue.setSkuId(skuId);
            skuPlatformAttrValueMapper.insert(skuPlatformAttributeValue);
        });
        // 保存skuSaleAttrValue信息
        skuInfoParam.getSkuSaleAttrValueList().forEach(skuSaleAttrValueParam -> {
            SkuSaleAttributeValue skuSaleAttributeValue = skuInfoParamConverter.skuSaleAttributeValueParam2Value(skuSaleAttrValueParam);
            skuSaleAttributeValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insert(skuSaleAttributeValue);
        });
    }

    @Override
    public SkuInfoPageDTO getPage(Page<SkuInfo> pageParam) {
        // 查询skuInfo分页数据
        Page<SkuInfo> skuInfoPage = skuInfoMapper.selectPage(pageParam, null);
        // 将skuInfoPage转换为skuInfoPageDTO
        return skuInfoPageConverter.skuInfoPagePO2PageDTO(skuInfoPage);
    }

    @Override
    public void onSale(Long skuId) {

    }

    @Override
    public void offSale(Long skuId) {

    }

    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {
        return null;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return null;
    }

    @Override
    public List<SpuSaleAttributeInfoDTO> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return null;
    }

    @Override
    public List<PlatformAttributeInfoDTO> getPlatformAttrInfoBySku(Long skuId) {
        return null;
    }
}
