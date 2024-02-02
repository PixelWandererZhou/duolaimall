package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.product.dto.CategoryHierarchyDTO;
import com.cskaoyan.mall.product.dto.PlatformAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SkuInfoDTO;
import com.cskaoyan.mall.product.dto.TrademarkDTO;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.SkuService;
import com.cskaoyan.mall.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class ProductApiController {
    @Autowired
    SkuService skuService;
    @Autowired
    TrademarkService trademarkService;
    @Autowired
    CategoryService categoryService;
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    SkuInfoDTO getSkuInfo(@PathVariable("skuId") Long skuId){
        return skuService.getSkuInfo(skuId);
    }
    @GetMapping("/api/product/inner/getCategoryView/{thirdLevelCategoryId}")
    CategoryHierarchyDTO getCategoryView(@PathVariable("thirdLevelCategoryId")Long thirdLevelCategoryId){
        return categoryService.getCategoryViewByCategoryId(thirdLevelCategoryId);
    }
    @GetMapping("/api/product/inner/getAttrList/{skuId}")
    List<PlatformAttributeInfoDTO> getAttrList(@PathVariable("skuId") Long skuId){
        return null;
    }
    @GetMapping("/api/product/inner/getTrademark/{tmId}")
    TrademarkDTO getTrademark(@PathVariable("tmId")Long tmId){
        return trademarkService.getTrademarkByTmId(tmId);
    }
    @GetMapping("/api/product/inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable(value = "skuId") Long skuId){
        return skuService.getSkuPrice(skuId);
    }
}
