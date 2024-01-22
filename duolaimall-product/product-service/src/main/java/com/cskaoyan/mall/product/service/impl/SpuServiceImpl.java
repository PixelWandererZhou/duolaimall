package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.SpuInfoConverter;
import com.cskaoyan.mall.product.converter.dto.SpuInfoPageConverter;
import com.cskaoyan.mall.product.converter.param.SpuInfoParamConverter;
import com.cskaoyan.mall.product.dto.SpuImageDTO;
import com.cskaoyan.mall.product.dto.SpuInfoPageDTO;
import com.cskaoyan.mall.product.dto.SpuPosterDTO;
import com.cskaoyan.mall.product.dto.SpuSaleAttributeInfoDTO;
import com.cskaoyan.mall.product.mapper.*;
import com.cskaoyan.mall.product.model.*;
import com.cskaoyan.mall.product.query.SpuInfoParam;
import com.cskaoyan.mall.product.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuPosterMapper spuPosterMapper;
    @Autowired
    SpuSaleAttrInfoMapper spuSaleAttrInfoMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SpuInfoConverter spuInfoConverter;
    @Autowired
    SpuInfoParamConverter spuInfoParamConverter;
    @Autowired
    SpuInfoPageConverter spuInfoPageConverter;

    @Override
    public SpuInfoPageDTO getSpuInfoPage(Page<SpuInfo> pageParam, Long category3Id) {
        QueryWrapper<SpuInfo> spuInfoQueryWrapper = new QueryWrapper<>();
        spuInfoQueryWrapper.eq("third_level_category_id", category3Id);
        Page<SpuInfo> spuInfoPage = spuInfoMapper.selectPage(pageParam,spuInfoQueryWrapper);
        return spuInfoPageConverter.spuInfoPage2PageDTO(spuInfoPage);
    }

    @Override
    @Transactional
    public void saveSpuInfo(SpuInfoParam spuInfoParam) {
        // 保存spuInfo信息
        SpuInfo spuInfo = spuInfoParamConverter.spuInfoParam2Info(spuInfoParam);
        spuInfoMapper.insert(spuInfo);
        //获取spuId
        Long spuId = spuInfo.getId();
        // 保存spuImage信息
        spuInfoParam.getSpuImageList().forEach(spuImageParam -> {
            SpuImage spuImage = spuInfoParamConverter.spuImageParam2Image(spuImageParam);
            spuImage.setSpuId(spuId);
            spuImageMapper.insert(spuImage);
        });
        // 保存spuPoster信息
        spuInfoParam.getSpuPosterList().forEach(spuPosterParam -> {
            SpuPoster spuPoster = spuInfoParamConverter.spuPosterParam2Poster(spuPosterParam);
            spuPoster.setSpuId(spuId);
            spuPoster.setSpuId(spuId);
            spuPosterMapper.insert(spuPoster);
        });
        // 保存spuSaleAttributeInfo信息
        spuInfoParam.getSpuSaleAttrList().forEach(spuSaleAttributeInfoParam -> {
            SpuSaleAttributeInfo spuSaleAttributeInfo = spuInfoParamConverter.spuSaleAttributeParam2Info(spuSaleAttributeInfoParam);
            spuSaleAttributeInfo.setSpuId(spuId);
            spuSaleAttrInfoMapper.insert(spuSaleAttributeInfo);
        });
        // 保存spuSaleAttributeValue信息
        spuInfoParam.getSpuSaleAttrList().forEach(spuSaleAttributeInfo -> {
            spuSaleAttributeInfo.getSpuSaleAttrValueList().forEach(spuSaleAttributeValueParam -> {
                SpuSaleAttributeValue spuSaleAttributeValue = spuInfoParamConverter.spuSaleAttributeValueParam2Value(spuSaleAttributeValueParam);
                spuSaleAttributeValue.setSpuId(spuId);
                spuSaleAttrValueMapper.insert(spuSaleAttributeValue);
            });
        });
    }

    @Override
    public List<SpuImageDTO> getSpuImageList(Long spuId) {
        return spuInfoConverter.spuImagePOs2DTOs(spuImageMapper.selectList(new QueryWrapper<SpuImage>().eq("spu_id", spuId)));
    }

    @Override
    public List<SpuSaleAttributeInfoDTO> getSpuSaleAttrList(Long spuId) {
        //查询spuSaleAttrList
        List<SpuSaleAttributeInfo> spuSaleAttrList = spuSaleAttrInfoMapper.selectList(new QueryWrapper<SpuSaleAttributeInfo>().eq("spu_id", spuId));
        for(SpuSaleAttributeInfo spuSaleAttributeInfo : spuSaleAttrList){
            //查询spuSaleAttrValueList
            List<SpuSaleAttributeValue> spuSaleAttrValueList = spuSaleAttrValueMapper.selectList(new QueryWrapper<SpuSaleAttributeValue>().eq("spu_id", spuId).eq("spu_sale_attr_id", spuSaleAttributeInfo.getId()));
            //设置spuSaleAttrValueList
            spuSaleAttributeInfo.setSpuSaleAttrValueList(spuSaleAttrValueList);
        }
        return spuInfoConverter.spuSaleAttributeInfoPOs2DTOs(spuSaleAttrList);
    }

    @Override
    public List<SpuPosterDTO> findSpuPosterBySpuId(Long spuId) {
        List<SpuPoster> spuPosters = spuPosterMapper.selectList(new QueryWrapper<SpuPoster>().eq("spu_id", spuId));
        return spuInfoConverter.spuPosterPOs2DTOs(spuPosters);
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
}
