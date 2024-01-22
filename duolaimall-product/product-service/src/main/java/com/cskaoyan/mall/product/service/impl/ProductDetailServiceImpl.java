package com.cskaoyan.mall.product.service.impl;

import com.alibaba.nacos.common.utils.JacksonUtils;
import com.cskaoyan.mall.product.dto.*;
import com.cskaoyan.mall.product.mapper.CategoryHierarchyMapper;
import com.cskaoyan.mall.product.mapper.PlatformAttrInfoMapper;
import com.cskaoyan.mall.product.mapper.PlatformAttrValueMapper;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.ProductDetailService;
import com.cskaoyan.mall.product.service.SkuService;
import com.cskaoyan.mall.product.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductDetailServiceImpl implements ProductDetailService {
    @Autowired
    SkuService skuService;
    @Autowired
    SpuService spuService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    PlatformAttrInfoMapper platformAttrInfoMapper;
    @Autowired
    PlatformAttrValueMapper platformAttrValueMapper;
    @Autowired
    CategoryHierarchyMapper categoryHierarchyMapper;

    @Override
    @Cacheable(value = "productDetail",key = "#skuId")
    public ProductDetailDTO getItemBySkuId(Long skuId) {
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        // 查询skuinfo
        SkuInfoDTO skuInfo = skuService.getSkuInfo(skuId);
        if(skuInfo == null){
            SkuInfoDTO skuInfoDTO = new SkuInfoDTO();
            skuInfoDTO.setId(skuId);
            productDetailDTO.setSkuInfo(skuInfoDTO);
            return productDetailDTO;
        }
        // 查询skuAttrList
        List<SkuPlatformAttributeValueDTO> skuPlatformAttributeValueDTOS = skuInfo.getSkuPlatformAttributeValueList();
        List<SkuSpecification> skuAttrList = new ArrayList<>();
        for(SkuPlatformAttributeValueDTO skuPlatformAttributeValueDTO:skuPlatformAttributeValueDTOS){
            String attrName = platformAttrInfoMapper.selectById(skuPlatformAttributeValueDTO.getAttrId()).getAttrName();
            String valueName = platformAttrValueMapper.selectById(skuPlatformAttributeValueDTO.getValueId()).getValueName();
            SkuSpecification skuSpecification = new SkuSpecification();
            skuSpecification.setAttrName(attrName);
            skuSpecification.setAttrValue(valueName);
            skuAttrList.add(skuSpecification);
        }
        //查询categoryHierarchy
        CategoryHierarchyDTO categoryHierarchy = categoryService.getCategoryViewByCategoryId(skuInfo.getThirdLevelCategoryId());
        // 查询price
        BigDecimal price = skuInfo.getPrice();
        // 查询spu销售属性
        List<SpuSaleAttributeInfoDTO> spuSaleAttrList = spuService.getSpuSaleAttrList(skuInfo.getSpuId());
        //查询sku的图片列表
        List<SpuPosterDTO> spuPosterList = spuService.findSpuPosterBySpuId(skuInfo.getSpuId());
        //获取spu中包含的所有的不同销售属性取值的组合
        Map<String, Long> skuValueIdsMap = spuService.getSkuValueIdsMap(skuInfo.getSpuId());
        String valuesSkuJson = JacksonUtils.toJson(skuValueIdsMap);
        productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setSkuAttrList(skuAttrList);
        productDetailDTO.setSkuInfo(skuInfo);
        productDetailDTO.setPrice(price);
        productDetailDTO.setSpuSaleAttrList(spuSaleAttrList);
        productDetailDTO.setSpuPosterList(spuPosterList);
        productDetailDTO.setValuesSkuJson(valuesSkuJson);
        productDetailDTO.setCategoryHierarchy(categoryHierarchy);
        return productDetailDTO;
    }
}
