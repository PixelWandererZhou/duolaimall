package com.cskaoyan.mall.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
      存储用户登录成功后，返回的前端所需的用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    String nickName;

    String token;
}
