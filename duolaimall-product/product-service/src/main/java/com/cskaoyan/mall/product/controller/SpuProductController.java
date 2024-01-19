package com.cskaoyan.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.SaleAttributeInfoDTO;
import com.cskaoyan.mall.product.dto.SpuInfoPageDTO;
import com.cskaoyan.mall.product.query.SpuInfoParam;
import com.cskaoyan.mall.product.service.SalesAttributeService;
import com.cskaoyan.mall.product.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
public class SpuProductController {
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
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfoParam spuInfoParam){
        spuService.saveSpuInfo(spuInfoParam);
        return Result.ok();
    }
    @GetMapping("spuImageList/{spuId}")
    public Result spuImageList(@PathVariable Long spuId){
        return Result.ok(spuService.getSpuImageList(spuId));
    }
    @GetMapping("spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable Long spuId){
        return Result.ok(spuService.getSpuSaleAttrList(spuId));
    }
}
