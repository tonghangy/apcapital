package com.apcapital.ecsystem.service.impl;

import com.apcapital.ecsystem.dto.request.UserRegisterRequest;
import com.apcapital.ecsystem.dto.response.UserResponse;
import com.apcapital.ecsystem.entity.User;
import com.apcapital.ecsystem.entity.UserAccount;
import com.apcapital.ecsystem.mapper.UserAccountMapper;
import com.apcapital.ecsystem.mapper.UserMapper;
import com.apcapital.ecsystem.service.UserService;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserServiceImpl 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserAccountMapper userAccountMapper;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param userMapper 本方法的输入参数
     * @param userAccountMapper 本方法的输入参数
     */
    public UserServiceImpl(UserMapper userMapper, UserAccountMapper userAccountMapper) {
        this.userMapper = userMapper;
        this.userAccountMapper = userAccountMapper;
    }

    /**
     * 执行 `register` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param request 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserResponse register(UserRegisterRequest request) {
        User user = new User();
        user.setName(request.name());
        userMapper.insert(user);

        UserAccount account = new UserAccount();
        account.setUserId(user.getId());
        account.setBalance(BigDecimal.ZERO);
        account.setVersion(0);
        userAccountMapper.insert(account);
        return new UserResponse(user.getId(), user.getName());
    }
}
