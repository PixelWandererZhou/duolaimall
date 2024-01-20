package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.PlatformAttributeInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SkuInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SkuInfoPageConverter;
import com.cskaoyan.mall.product.converter.param.SkuInfoParamConverter;
import com.cskaoyan.mall.product.dto.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    }

    @Override
    public void offSale(Long skuId) {
        // 修改skuInfo的isSale字段为0
        SkuInfoDTO skuInfoDTO = getSkuInfo(skuId);
        skuInfoDTO.setIsSale(0);
        skuInfoMapper.updateById(skuInfoConverter.skuInfoDTO2PO(skuInfoDTO));
    }

    @Override
    public SkuInfoDTO getSkuInfo(Long skuId) {
        // 查询skuInfo
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        // 查询skuAttrValueList
        List<SkuPlatformAttributeValue> skuPlatformAttributeValueList = skuPlatformAttrValueMapper.selectList(new QueryWrapper<SkuPlatformAttributeValue>().eq("sku_id", skuId));
        // 查询skuSaleAttrValueList
        List<SkuSaleAttributeValue> skuSaleAttributeValueList = skuSaleAttrValueMapper.selectList(new QueryWrapper<SkuSaleAttributeValue>().eq("sku_id", skuId));
        // 将skuInfo转换为skuInfoDTO
        SkuInfoDTO skuInfoDTO = skuInfoConverter.skuInfoPO2DTO(skuInfo);
        // 将skuAttrValueList转换为skuAttrValueDTOList
        skuInfoDTO.setSkuPlatformAttributeValueList(skuInfoConverter.skuPlatformAttributeValuePOs2DTOs(skuPlatformAttributeValueList));
        // 将skuSaleAttrValueList转换为skuSaleAttrValueDTOList
        skuInfoDTO.setSkuSaleAttributeValueList(skuInfoConverter.skuSaleAttributeValuePOs2DTOs(skuSaleAttributeValueList));
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
    public Map<String, Long> getSkuValueIdsMap(Long spuId) {
        //{"4478|4481":283,"4478|4480":282,"4479|4480":284,"4479|4481":286}
        List<SkuSaleAttributeValue> skuSaleAttributeValueList = skuSaleAttrValueMapper.selectList(new QueryWrapper<SkuSaleAttributeValue>().eq("spu_id", spuId));
        //查询skuid集合
        List<Long> skuIds = skuSaleAttributeValueList.stream().map(SkuSaleAttributeValue::getSkuId).collect(Collectors.toList());
        Map<String, Long> skuValueIdsMap = new HashMap<>();
        for(Long skuid : skuIds){
            //根据skuid查询spuattrvalueid集合
            List<Long> spuSaleAttrValueIds = skuSaleAttributeValueList.stream().filter(skuSaleAttributeValue -> skuSaleAttributeValue.getSkuId().equals(skuid)).map(SkuSaleAttributeValue::getSpuSaleAttrValueId).collect(Collectors.toList());
            //将spuattrvalueid集合转换为字符串,使用"|"拼接
            String spuSaleAttrValueIdsStr = spuSaleAttrValueIds.stream().map(String::valueOf).collect(Collectors.joining("|"));
            //将spuSaleAttrValueIdsStr和skuid封装到map中
            skuValueIdsMap.put(spuSaleAttrValueIdsStr, skuid);
        }

        return skuValueIdsMap;
    }

    @Override
    public List<SkuPlatformAttributeValueDTO> getSkuPlatformAttributeValueBySku(Long skuId) {
        return skuInfoConverter.skuPlatformAttributeValuePOs2DTOs(skuPlatformAttrValueMapper.selectList(new QueryWrapper<SkuPlatformAttributeValue>().eq("sku_id", skuId)));
    }
}
