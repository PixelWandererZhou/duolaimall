package com.cskaoyan.mall.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.product.dto.TrademarkDTO;
import com.cskaoyan.mall.product.dto.TrademarkPageDTO;
import com.cskaoyan.mall.product.model.Trademark;
import com.cskaoyan.mall.product.query.TrademarkParam;
import com.cskaoyan.mall.product.service.TrademarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product/baseTrademark")
public class TrademarkController {
    @Autowired
    TrademarkService trademarkService;
    @GetMapping("{page}/{limit}")
    public Result<TrademarkPageDTO> getBaseTrademark(@PathVariable Integer page,@PathVariable Integer limit){
        Page<Trademark> trademarkPage = new Page<>();
        trademarkPage.setSize(limit);
        trademarkPage.setCurrent(page);
        return Result.ok(trademarkService.getPage(trademarkPage));
    }
    @GetMapping("get/{id}")
    public Result<TrademarkDTO> getBaseTrademarkInfo(@PathVariable Long id){
        return Result.ok(trademarkService.getTrademarkByTmId(id));
    }
    @PutMapping("update")
    public Result updateBaseTrademark(@RequestBody TrademarkParam trademarkParam){
        trademarkService.updateById(trademarkParam);
        return Result.ok();
    }
    @DeleteMapping("remove/{id}")
    public Result removeBaseTrademark(@PathVariable Long id){
        trademarkService.removeById(id);
        return Result.ok();
    }
    @PostMapping("save")
    public Result saveBaseTrademark(@RequestBody TrademarkParam trademarkParam){
        trademarkService.save(trademarkParam);
        return Result.ok();
    }
}
