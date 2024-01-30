package com.cskaoyan.mall.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.cskaoyan.mall.user.dto.UserLoginDTO;
import com.cskaoyan.mall.user.mapper.UserInfoMapper;
import com.cskaoyan.mall.user.model.UserInfo;
import com.cskaoyan.mall.user.query.UserInfoParam;
import com.cskaoyan.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserInfoMapper userInfoMapper;
    @Override
        public UserLoginDTO login(UserInfoParam userInfo, String ip) {
        Map map = new HashMap();
        map.put("login_name", userInfo.getLoginName());
        List<UserInfo> userInfoList = userInfoMapper.selectByMap(map);
        String token = "";
        if (!userInfoList.isEmpty()) {
            UserInfo user = userInfoList.get(0);
            if(user.getPasswd().equals(userInfo.getPasswd())) {
                StpUtil.login(user.getId());
                token=StpUtil.getTokenValue();
            }
        }
        return new UserLoginDTO(userInfo.getLoginName(), token);
    }

    @Override
    public void logout(Object userId) {
        StpUtil.logout(userId);
    }
}
