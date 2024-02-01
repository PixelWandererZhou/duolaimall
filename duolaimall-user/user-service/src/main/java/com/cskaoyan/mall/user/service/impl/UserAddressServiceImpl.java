package com.cskaoyan.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cskaoyan.mall.user.mapper.UserAddressMapper;
import com.cskaoyan.mall.user.model.UserAddress;
import com.cskaoyan.mall.user.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    UserAddressMapper userAddressMapper;
    @Override
    public List<UserAddress> findUserAddressListByUserId(String userId) {
       return userAddressMapper.selectList(new QueryWrapper<UserAddress>().eq("user_id", userId));
    }
}
