package com.cskaoyan.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cskaoyan.mall.product.converter.dto.PlatformAttributeInfoConverter;
import com.cskaoyan.mall.product.converter.param.PlatformAttributeInfoParamConverter;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.PlatformAttributeValueDTO;
import com.cskaoyan.mall.product.mapper.PlatformAttrInfoMapper;
import com.cskaoyan.mall.product.mapper.PlatformAttrValueMapper;
import com.cskaoyan.mall.product.model.PlatformAttributeInfo;
import com.cskaoyan.mall.product.model.PlatformAttributeValue;
import com.cskaoyan.mall.product.query.PlatformAttributeParam;
import com.cskaoyan.mall.product.query.PlatformAttributeValueParam;
import com.cskaoyan.mall.product.service.PlatformAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlatformAttributeServiceImpl implements PlatformAttributeService {
    @Autowired
    PlatformAttrInfoMapper platformAttrInfoMapper;
    @Autowired
    PlatformAttrValueMapper platformAttrValueMapper;
    @Autowired
    PlatformAttributeInfoConverter platformAttributeInfoConverter;
    @Autowired
    PlatformAttributeInfoParamConverter platformAttributeInfoParamConverter;

    @Override
    public List<PlatformAttributeInfoDTO> getPlatformAttrInfoList(Long firstLevelCategoryId, Long secondLevelCategoryId, Long thirdLevelCategoryId) {
        QueryWrapper<PlatformAttributeInfo> platformAttributeInfoQueryWrapper = new QueryWrapper<>();
        //secondLevelCategoryId==0 && thirdLevelCategoryId==0
        if (secondLevelCategoryId==0 && thirdLevelCategoryId==0){
            platformAttributeInfoQueryWrapper.eq("category_id",firstLevelCategoryId);
            List<PlatformAttributeInfoDTO> platformAttributeInfoDTOS = platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttrInfoMapper.selectList(platformAttributeInfoQueryWrapper));
            for(PlatformAttributeInfoDTO platformAttributeInfoDTO : platformAttributeInfoDTOS){
                platformAttributeInfoDTO.setAttrValueList(platformAttributeInfoConverter.platformAttributeValuesPO2DTOs(platformAttrValueMapper.selectList(new QueryWrapper<PlatformAttributeValue>().eq("attr_id",platformAttributeInfoDTO.getId()))));
            }
            return platformAttributeInfoDTOS;
        }

        //secondLevelCategoryId!=0 && thirdLevelCategoryId==0
        if(secondLevelCategoryId!=0 && thirdLevelCategoryId==0){
            platformAttributeInfoQueryWrapper
                    .eq("category_id",firstLevelCategoryId)
                    .or()
                    .eq("category_id",secondLevelCategoryId);
            List<PlatformAttributeInfoDTO> platformAttributeInfoDTOS = platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttrInfoMapper.selectList(platformAttributeInfoQueryWrapper));
            for(PlatformAttributeInfoDTO platformAttributeInfoDTO : platformAttributeInfoDTOS){
                platformAttributeInfoDTO.setAttrValueList(platformAttributeInfoConverter.platformAttributeValuesPO2DTOs(platformAttrValueMapper.selectList(new QueryWrapper<PlatformAttributeValue>().eq("attr_id",platformAttributeInfoDTO.getId()))));
            }
            return platformAttributeInfoDTOS;        }

        //secondLevelCategoryId!=0 && thirdLevelCategoryId!=0
        if (secondLevelCategoryId != 0) {
            platformAttributeInfoQueryWrapper
                    .eq("category_id",firstLevelCategoryId)
                    .or()
                    .eq("category_id",secondLevelCategoryId)
                    .or()
                    .eq("category_id",thirdLevelCategoryId);
            List<PlatformAttributeInfoDTO> platformAttributeInfoDTOS = platformAttributeInfoConverter.platformAttributeInfoPOs2DTOs(platformAttrInfoMapper.selectList(platformAttributeInfoQueryWrapper));
            for(PlatformAttributeInfoDTO platformAttributeInfoDTO : platformAttributeInfoDTOS){
                platformAttributeInfoDTO.setAttrValueList(platformAttributeInfoConverter.platformAttributeValuesPO2DTOs(platformAttrValueMapper.selectList(new QueryWrapper<PlatformAttributeValue>().eq("attr_id",platformAttributeInfoDTO.getId()))));
            }
            return platformAttributeInfoDTOS;        }
        return null;
    }

    @Override
    @Transactional
    public void savePlatformAttrInfo(PlatformAttributeParam platformAttributeParam) {
        UpdateWrapper<PlatformAttributeInfo> platformAttributeInfoUpdateWrapper = new UpdateWrapper<>();
        platformAttributeInfoUpdateWrapper
                .eq("category_id",platformAttributeParam.getCategoryId());
        platformAttrInfoMapper.update(platformAttributeInfoParamConverter.attributeInfoParam2Info(platformAttributeParam), platformAttributeInfoUpdateWrapper);
        if(platformAttributeParam.getAttrValueList()!=null){
            //delete all attrValueList
            Map<String,Object> map = new HashMap<>();
            map.put("attr_id",platformAttributeParam.getId());
            platformAttrValueMapper.deleteByMap(map);

            //insert new attrValueList
            for (PlatformAttributeValueParam platformAttributeValueParam : platformAttributeParam.getAttrValueList()) {
                PlatformAttributeValue platformAttributeValue = new PlatformAttributeValue();
                platformAttributeValue.setAttrId(platformAttributeValueParam.getAttrId());
                platformAttributeValue.setValueName(platformAttributeValueParam.getValueName());
                platformAttrValueMapper.insert(platformAttributeValue);
            }
        }
        else {
            //delete all attrValueList
            Map<String,Object> map = new HashMap<>();
            map.put("attr_id",platformAttributeParam.getId());
            platformAttrValueMapper.deleteByMap(map);
        }
    }

    @Override
    public List<PlatformAttributeValueDTO> getPlatformAttrInfo(Long attrId) {
        QueryWrapper<PlatformAttributeValue> platformAttributeValueQueryWrapper = new QueryWrapper<>();
        platformAttributeValueQueryWrapper.eq("attr_id",attrId);
        List<PlatformAttributeValue> platformAttributeValues = platformAttrValueMapper.selectList(platformAttributeValueQueryWrapper);
        return platformAttributeInfoConverter.platformAttributeValuesPO2DTOs(platformAttributeValues);
    }
}
