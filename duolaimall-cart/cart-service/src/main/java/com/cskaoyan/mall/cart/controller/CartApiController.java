package com.cskaoyan.mall.cart.controller;

import com.cskaoyan.mall.cart.api.dto.CartInfoDTO;
import com.cskaoyan.mall.cart.service.CartService;
import com.cskaoyan.mall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
