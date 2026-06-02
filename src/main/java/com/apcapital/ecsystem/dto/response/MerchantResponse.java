package com.apcapital.ecsystem.dto.response;

import java.math.BigDecimal;

/**
 * 商家注册结果响应。
 *
 * <p>用于在商家创建成功后返回商家基本信息及账户初始余额。
 */
public record MerchantResponse(Long id, String name, BigDecimal balance) {
}
