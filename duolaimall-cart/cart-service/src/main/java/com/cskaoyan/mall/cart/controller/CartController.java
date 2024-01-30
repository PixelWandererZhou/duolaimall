package com.cskaoyan.mall.cart.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.cskaoyan.mall.cart.service.CartService;
import com.cskaoyan.mall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController()
public class CartController {
    @Autowired
    CartService cartService;

    @RequestMapping("/cart/add/{skuId}/{skuNum}")
    Result addCart(@PathVariable Long skuId, @PathVariable Integer skuNum, HttpServletRequest request) {
        String userId;
        boolean login = StpUtil.isLogin();
        if (login) {
            userId = (String) StpUtil.getLoginId();
        } else {
            userId = request.getHeader("userTempId");
        }
        cartService.addToCart(skuId, userId, skuNum);
        return Result.ok();
    }

    @GetMapping("cart")
    Result getCartList(HttpServletRequest request) {
        String userId = "";
        String userTempId = request.getHeader("userTempId");
        boolean login = StpUtil.isLogin();
        if (login) {
            userId = (String) StpUtil.getLoginId();
        }
        return Result.ok(cartService.getCartList(userId, userTempId));
    }

    @DeleteMapping("/cart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId, HttpServletRequest request) {
        String userId;
        boolean login = StpUtil.isLogin();
        if (login) {
            userId = (String) StpUtil.getLoginId();
        } else {
            userId = request.getHeader("userTempId");
        }
        cartService.deleteCart(skuId, userId);
        return Result.ok();
    }

    @PutMapping("cart/check/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable Long skuId, @PathVariable Integer isChecked,
                            HttpServletRequest request) {
        String userId;
        boolean login = StpUtil.isLogin();
        if (login) {
            userId = (String) StpUtil.getLoginId();
        } else {
            userId = request.getHeader("userTempId");
        }
        cartService.checkCart(userId, isChecked, skuId);
        return Result.ok();
    }

    @DeleteMapping("/cart/checked")
    public Result deleteChecked(HttpServletRequest request) {
        String userId;
        boolean login = StpUtil.isLogin();
        if (login) {
            userId = (String) StpUtil.getLoginId();
        } else {
            userId = request.getHeader("userTempId");
        }
        cartService.deleteChecked(userId);
        return Result.ok();
    }
}
