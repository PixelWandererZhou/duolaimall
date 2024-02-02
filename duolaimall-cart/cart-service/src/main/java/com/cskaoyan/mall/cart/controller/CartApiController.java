package com.cskaoyan.mall.cart.controller;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.cart.service.CartService;
import com.cskaoyan.mall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart/inner")
public class CartApiController {
    @Autowired
    CartService cartService;
    @GetMapping("getCartCheckedList/{userId}")
    public List<CartInfoDTO> getCartCheckedList(@PathVariable(value = "userId") String userId){
        return cartService.getCartCheckedList(userId);
    }
    @GetMapping("refresh/{userId}/{skuId}")
    public Result refreshCartPrice(@PathVariable(value = "userId") String userId, @PathVariable(value = "skuId") Long skuId){
        cartService.refreshCartPrice(userId,skuId);
        return Result.ok();
    }
    @PutMapping("delete/order/cart/{userId}")
    public Result removeCartProductsInOrder(@PathVariable("userId") String userId, @RequestBody List<Long> skuIds){
        cartService.delete(userId,skuIds);
        return Result.ok();
    }
}
