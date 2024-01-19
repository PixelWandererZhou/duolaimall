package com.cskaoyan.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.model.SkuInfo;
import com.cskaoyan.mall.product.query.SkuInfoParam;
import com.cskaoyan.mall.product.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
public class SkuProductController {
    @Autowired
    SkuService skuService;
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfoParam skuInfoParam){
        skuService.saveSkuInfo(skuInfoParam);
        return Result.ok();
    }
//    http://localhost/admin/product/list/1/10
    @GetMapping("list/{page}/{size}")
    public Result list(@PathVariable int page, @PathVariable int size){
        Page<SkuInfo> skuInfoPage = new Page<SkuInfo>(page, size);
        return Result.ok(skuService.getPage(skuInfoPage));
    }
    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable Long skuId){
        skuService.onSale(skuId);
        return Result.ok();
    }
//    http://localhost/admin/product/cancelSale/3
    @GetMapping("cancelSale/{skuId}")
    public Result offSale(@PathVariable Long skuId){
        skuService.offSale(skuId);
        return Result.ok();
    }
}
