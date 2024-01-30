package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.client.SearchApiClient;
import com.cskaoyan.mall.product.converter.dto.SkuInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SkuInfoPageConverter;
import com.cskaoyan.mall.product.converter.param.SkuInfoParamConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.SkuImage;
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
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SkuInfoConverter skuInfoConverter;
    @Autowired
    SkuInfoParamConverter skuInfoParamConverter;
    @Autowired
    SkuInfoPageConverter skuInfoPageConverter;
    @Autowired
    SearchApiClient searchApiClient;
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
            skuSaleAttributeValue.setSpuId(skuInfoParam.getSpuId());
            skuSaleAttributeValue.setSkuId(skuId);
            skuSaleAttrValueMapper.insert(skuSaleAttributeValue);
        });
    }

    @Override
    public SkuInfoPageDTO getPage(Page<SkuInfo> pageParam) {
        // 查询skuInfo分页数据
        Page<SkuInfo> skuInfoPage = skuInfoMapper.selectPage(pageParam, null);
        // 将skuInfoPage转换为skuInfoPageDTO
        SkuInfoPageDTO skuInfoPageDTO = skuInfoPageConverter.skuInfoPagePO2PageDTO(skuInfoPage);
        List<SkuInfoDTO> records = skuInfoPageDTO.getRecords();
        for(SkuInfoDTO skuInfoDTO : records){
            skuInfoDTO.setSkuPlatformAttributeValueList(getSkuPlatformAttributeValueBySku(skuInfoDTO.getId()));
        }
        skuInfoPageDTO.setRecords(records);
        return skuInfoPageDTO;
    }

    @Override
    public void onSale(Long skuId) {
        // 修改skuInfo的isSale字段为1
        SkuInfoDTO skuInfoDTO = getSkuInfo(skuId);
        skuInfoDTO.setIsSale(1);
        skuInfoMapper.updateById(skuInfoConverter.skuInfoDTO2PO(skuInfoDTO));
        //调用搜索服务上架商品
        searchApiClient.upperGoods(skuId);
    }

    @Override
    public void offSale(Long skuId) {
        // 修改skuInfo的isSale字段为0
        SkuInfoDTO skuInfoDTO = getSkuInfo(skuId);
        skuInfoDTO.setIsSale(0);
        skuInfoMapper.updateById(skuInfoConverter.skuInfoDTO2PO(skuInfoDTO));
        //调用搜索服务下架商品
        searchApiClient.lowerGoods(skuId);
    }

    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {
        // 查询skuInfo
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        // 查询skuAttrValueList
        List<SkuPlatformAttributeValue> skuPlatformAttributeValueList = skuPlatformAttrValueMapper.selectList(new QueryWrapper<SkuPlatformAttributeValue>().eq("sku_id", skuId));
        // 查询skuSaleAttrValueList
        List<SkuSaleAttributeValue> skuSaleAttributeValueList = skuSaleAttrValueMapper.selectList(new QueryWrapper<SkuSaleAttributeValue>().eq("sku_id", skuId));
        //查询skuImageList
        List<SkuImageDTO> skuImageList = getSkuImageList(skuId);
        // 将skuInfo转换为skuInfoDTO
        SkuInfoDTO skuInfoDTO = skuInfoConverter.skuInfoPO2DTO(skuInfo);
        // 将skuAttrValueList转换为skuAttrValueDTOList
        skuInfoDTO.setSkuPlatformAttributeValueList(skuInfoConverter.skuPlatformAttributeValuePOs2DTOs(skuPlatformAttributeValueList));
        // 将skuSaleAttrValueList转换为skuSaleAttrValueDTOList
        skuInfoDTO.setSkuSaleAttributeValueList(skuInfoConverter.skuSaleAttributeValuePOs2DTOs(skuSaleAttributeValueList));
        skuInfoDTO.setSkuImageList(skuImageList);
        return skuInfoDTO;
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


    @Override
    public List<SkuPlatformAttributeValueDTO> getSkuPlatformAttributeValueBySku(Long skuId) {
        return skuInfoConverter.skuPlatformAttributeValuePOs2DTOs(skuPlatformAttrValueMapper.selectList(new QueryWrapper<SkuPlatformAttributeValue>().eq("sku_id", skuId)));
    }
    @Override
    public List<SkuImageDTO> getSkuImageList(Long skuId){
        List<SkuImage> skuImages = skuImageMapper.selectList(new QueryWrapper<SkuImage>().eq("sku_id", skuId));
        for (SkuImage skuImage : skuImages) {
            //查询imageUrl和imageName
            skuImage.setImgUrl(spuImageMapper.selectById(skuImage.getSpuImgId()).getImgUrl());
            skuImage.setImgName(spuImageMapper.selectById(skuImage.getSpuImgId()).getImgName());
        }
        return skuInfoConverter.skuImagePOs2DTOs(skuImages);
    }
}
