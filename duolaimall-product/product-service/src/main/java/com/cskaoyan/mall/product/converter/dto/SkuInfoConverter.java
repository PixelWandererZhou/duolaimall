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

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkuInfoConverter {

    SkuInfoDTO skuInfoPO2DTO(SkuInfo skuInfo);
    SkuInfo skuInfoDTO2PO(SkuInfoDTO skuInfoDTO);

    SkuImageDTO skuImagePO2DTO(SkuImage skuImage);

    SkuPlatformAttributeValueDTO skuPlatformAttributeValuePO2DTO(
            SkuPlatformAttributeValue skuPlatformAttributeValue);
    List<SkuPlatformAttributeValueDTO> skuPlatformAttributeValuePOs2DTOs(
            List<SkuPlatformAttributeValue> skuPlatformAttributeValue);

    List<SkuSaleAttributeValueDTO> skuSaleAttributeValuePOs2DTOs(
            List<SkuSaleAttributeValue> skuSaleAttributeValue);
}
