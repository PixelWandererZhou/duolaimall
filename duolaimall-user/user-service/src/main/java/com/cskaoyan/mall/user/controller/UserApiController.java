package com.cskaoyan.mall.user.controller;

import com.cskaoyan.mall.user.converter.UserAddressConverter;
import com.cskaoyan.mall.user.dto.UserAddressDTO;
import com.cskaoyan.mall.user.model.UserAddress;
import com.cskaoyan.mall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user/inner")
public class UserApiController {
    @Autowired
    UserAddressService userAddressService;
    @Autowired
    UserAddressConverter userAddressConverter;
    @GetMapping("findUserAddressListByUserId/{userId}")
    public List<UserAddressDTO> findUserAddressListByUserId(@PathVariable("userId") String userId) {
        List<UserAddress> userAddressList = userAddressService.findUserAddressListByUserId(userId);
        return userAddressConverter.userAddressPOs2DTOs(userAddressList);
    }
}
