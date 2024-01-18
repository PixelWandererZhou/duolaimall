package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.product.converter.dto.PlatformAttributeInfoConverter;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.PlatformAttributeValueDTO;
import com.cskaoyan.mall.product.mapper.PlatformAttrInfoMapper;
import com.cskaoyan.mall.product.model.PlatformAttributeInfo;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformAttributeServiceImpl implements PlatformAttributeService {
    @Autowired
    PlatformAttrInfoMapper platformAttrInfoMapper;
    @Autowired
    PlatformAttributeInfoConverter platformAttributeInfoConverter;

    @Override
    public List<PlatformAttributeInfoDTO> getPlatformAttrInfoList(Long firstLevelCategoryId, Long secondLevelCategoryId, Long thirdLevelCategoryId) {
        QueryWrapper<PlatformAttributeInfo> platformAttributeInfoQueryWrapper = new QueryWrapper<>();
        //secondLevelCategoryId==0 && thirdLevelCategoryId==0
        if (secondLevelCategoryId==0 && thirdLevelCategoryId==0){
            platformAttributeInfoQueryWrapper.eq("category_id",firstLevelCategoryId);
            return platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttrInfoMapper.selectList(platformAttributeInfoQueryWrapper));
        }

        //secondLevelCategoryId!=0 && thirdLevelCategoryId==0
        if(secondLevelCategoryId!=0 && thirdLevelCategoryId==0){
            platformAttributeInfoQueryWrapper
                    .eq("category_id",firstLevelCategoryId)
                    .or()
                    .eq("category_id",secondLevelCategoryId);
            return platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttrInfoMapper.selectList(platformAttributeInfoQueryWrapper));
        }

        //secondLevelCategoryId!=0 && thirdLevelCategoryId!=0
        if (secondLevelCategoryId != 0) {
            platformAttributeInfoQueryWrapper
                    .eq("category_id",firstLevelCategoryId)
                    .or()
                    .eq("category_id",secondLevelCategoryId)
                    .or()
                    .eq("category_id",thirdLevelCategoryId);
            return platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttrInfoMapper.selectList(platformAttributeInfoQueryWrapper));
        }
        return null;
    }

    @Override
    public void savePlatformAttrInfo(PlatformAttributeParam platformAttributeParam) {

    }

    @Override
    public List<PlatformAttributeValueDTO> getPlatformAttrInfo(Long attrId) {
        return null;
    }
}
