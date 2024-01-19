package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.product.converter.dto.SpuInfoPageConverter;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.SpuInfoMapper;
import com.cskaoyan.mall.product.model.SpuInfo;
import com.cskaoyan.mall.product.query.SpuInfoParam;
import com.cskaoyan.mall.product.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    SpuInfoMapper spuInfoMapper;
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
    public void saveSpuInfo(SpuInfoParam spuInfo) {

    }

    @Override
    public List<SpuImageDTO> getSpuImageList(Long spuId) {
        return null;
    }

    @Override
    public List<SpuSaleAttributeInfoDTO> getSpuSaleAttrList(Long spuId) {
        return null;
    }

    @Override
    public List<SpuPosterDTO> findSpuPosterBySpuId(Long spuId) {
        return null;
    }

    @Override
    public Map<String, Long> getSkuValueIdsMap(Long spuId) {
        return null;
    }
}
