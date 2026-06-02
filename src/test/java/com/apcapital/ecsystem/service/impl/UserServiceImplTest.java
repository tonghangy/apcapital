package com.apcapital.ecsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.apcapital.ecsystem.dto.request.UserRegisterRequest;
import com.apcapital.ecsystem.mapper.UserAccountMapper;
import com.apcapital.ecsystem.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * UserServiceImplTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;
    @Mock
    private UserAccountMapper userAccountMapper;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userMapper, userAccountMapper);
    }

    @Test
    void register_shouldCreateUserAndAccount() {
        doAnswer(invocation -> {
            var user = invocation.getArgument(0, com.apcapital.ecsystem.entity.User.class);
            user.setId(11L);
            return 1;
        }).when(userMapper).insert(any(com.apcapital.ecsystem.entity.User.class));

        var response = userService.register(new UserRegisterRequest("Alice"));

        assertEquals(11L, response.id());
        assertEquals("Alice", response.name());
    }
}
