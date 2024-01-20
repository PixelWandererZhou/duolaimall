package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.ProductDetailDTO;
import com.cskaoyan.mall.product.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    ProductDetailService productDetailService;
    @GetMapping("{skuId}")
    public Result<ProductDetailDTO> getItemBySkuId(@PathVariable Long skuId){
        return Result.ok(productDetailService.getItemBySkuId(skuId));
    }
}
