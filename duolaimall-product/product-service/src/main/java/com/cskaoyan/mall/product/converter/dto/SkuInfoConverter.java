package com.cskaoyan.mall.product.converter.dto;

import com.cskaoyan.mall.product.dto.SkuImageDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.SkuPlatformAttributeValueDTO;
import com.cskaoyan.mall.product.dto.SkuSaleAttributeValueDTO;
import com.cskaoyan.mall.product.model.SkuImage;
import com.cskaoyan.mall.product.model.SkuInfo;
import com.cskaoyan.mall.product.model.SkuPlatformAttributeValue;
import com.cskaoyan.mall.product.model.SkuSaleAttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkuInfoConverter {

    SkuInfoDTO skuInfoPO2DTO(SkuInfo skuInfo);
    SkuInfo skuInfoDTO2PO(SkuInfoDTO skuInfoDTO);

    SkuImageDTO skuImagePO2DTO(SkuImage skuImage);
    List<SkuImageDTO> skuImagePOs2DTOs(List<SkuImage> skuImages);

    SkuPlatformAttributeValueDTO skuPlatformAttributeValuePO2DTO(
            SkuPlatformAttributeValue skuPlatformAttributeValue);
    List<SkuPlatformAttributeValueDTO> skuPlatformAttributeValuePOs2DTOs(
            List<SkuPlatformAttributeValue> skuPlatformAttributeValue);
    @Mapping(source = "skuSaleAttributeValue.spuSaleAttrValueId", target = "saleAttrValueId")
    SkuSaleAttributeValueDTO skuSaleAttributeValuePO2DTO(SkuSaleAttributeValue skuSaleAttributeValue);
    List<SkuSaleAttributeValueDTO> skuSaleAttributeValuePOs2DTOs(
            List<SkuSaleAttributeValue> skuSaleAttributeValue);
}
