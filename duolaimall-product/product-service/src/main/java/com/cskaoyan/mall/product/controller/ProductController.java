package com.cskaoyan.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.SaleAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SpuInfoDTO;
import com.cskaoyan.mall.product.dto.SpuInfoPageDTO;
import com.cskaoyan.mall.product.mapper.SpuSaleAttrInfoMapper;
import com.cskaoyan.mall.product.service.SalesAttributeService;
import com.cskaoyan.mall.product.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class ProductController {
    @Autowired
    SpuService spuService;
    @Autowired
    SalesAttributeService  salesAttributeService;
//    http://localhost/admin/product/1/10?category3Id=2
    @GetMapping("{page}/{size}")
    public Result<SpuInfoPageDTO> productList(@PathVariable int page, @PathVariable int size, Long category3Id){
        SpuInfoPageDTO spuInfoPage = spuService.getSpuInfoPage(new Page<>(page, size), category3Id);
        return Result.ok(spuInfoPage);
    }
    @GetMapping("baseSaleAttrList")
    public Result<List<SaleAttributeInfoDTO>> baseSaleAttrList(Long spuId){
        return Result.ok(salesAttributeService.getSaleAttrInfoList());
    }
}
