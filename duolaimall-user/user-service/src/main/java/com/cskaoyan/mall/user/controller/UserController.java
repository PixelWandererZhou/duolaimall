package com.cskaoyan.mall.user.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.cskaoyan.mall.common.result.Result;
import com.cskaoyan.mall.user.dto.UserLoginDTO;
import com.cskaoyan.mall.user.query.UserInfoParam;
import com.cskaoyan.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("login")
    Result login(@RequestBody UserInfoParam userInfoParam, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        UserLoginDTO login = userService.login(userInfoParam, ip);
        return Result.ok(login);
    }
    @GetMapping("logout")
    Result logout() {
        Object loginId = StpUtil.getLoginId();
        userService.logout(loginId);
        return Result.ok();
    }
}
