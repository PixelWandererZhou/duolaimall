package com.cskaoyan.mall.product.controller;

import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.TrademarkDTO;
import com.cskaoyan.mall.product.query.CategoryTrademarkParam;
import com.cskaoyan.mall.product.service.CategoryService;
import com.cskaoyan.mall.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product/baseCategoryTrademark")
public class CategoryTrademarkController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    TrademarkService trademarkService;
    @PostMapping("save")
    public Result saveBaseCategoryTrademark(@RequestBody CategoryTrademarkParam categoryTrademarkParam) {
        categoryService.save(categoryTrademarkParam);
        return Result.ok();
    }
    @GetMapping("findTrademarkList/{thirdLevelCategoryId}")
    public Result findTrademarkList(@PathVariable Long thirdLevelCategoryId) {
        List<TrademarkDTO> trademarkList = categoryService.findTrademarkList(thirdLevelCategoryId);
        return Result.ok(trademarkList);
    }
    @GetMapping("findCurrentTrademarkList/{thirdLevelCategoryId}")
    public Result findCurrentTrademarkList(@PathVariable Long thirdLevelCategoryId) {
        List<TrademarkDTO> trademarkList = categoryService.findUnLinkedTrademarkList(thirdLevelCategoryId);
        return Result.ok(trademarkList);
    }
    @DeleteMapping("remove/{thirdLevelCategoryId}/{trademarkId}")
    public Result remove(@PathVariable Long thirdLevelCategoryId, @PathVariable Long trademarkId) {
        categoryService.remove(thirdLevelCategoryId, trademarkId);
        return Result.ok();
    }
}
