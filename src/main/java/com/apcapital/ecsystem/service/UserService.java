package com.apcapital.ecsystem.service;

import com.apcapital.ecsystem.dto.request.UserRegisterRequest;
import com.apcapital.ecsystem.dto.response.UserResponse;

/**
 * UserService 是预存账户电商系统中的接口。
 *
 * <p>负责用户基础生命周期能力，当前聚焦“注册并初始化预存账户”场景。
 */
public interface UserService {

    /**
     * 注册新用户并创建默认预存账户。
     *
     * @param request 用户注册请求（仅包含名称）
     * @return 注册成功后的用户信息
     */
    UserResponse register(UserRegisterRequest request);
}
