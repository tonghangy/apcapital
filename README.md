# EC System

## 项目总结

这是一个基于 `Spring Boot 3.2 + Java 21` 的本地可运行电商交易系统，核心目标是实现“用户预存账户支付 + 商家库存交易 + 日终结算对账”。

- **业务主线**：用户下单后，在同一交易流程中完成用户扣款、商家入账、库存扣减、订单落库。
- **核心模块**：用户模块（注册/充值/余额）、商家模块（注册/补库存/余额）、订单模块（下单/查询）、结算模块（按日对账）。
- **一致性保障**：使用本地事务、Redis 分布式锁、乐观锁，减少并发场景下的超卖和资金不一致风险。
- **扩展能力**：预留 RocketMQ 事件通知能力，支持后续接入积分、消息通知、报表等异步流程。
- **工程状态**：包含完整 REST API、单元测试与本地运行脚本，可直接在本机启动验证。

Spring Boot 3.2 + Java 21 implementation of a prepaid-account ecommerce system.

## Tech Stack

- Java 21
- Spring Boot 3.2.6
- Spring Cloud 2023.0.2 (OpenFeign enabled)
- MyBatis-Plus 3.5.5
- MySQL 8.0
- Spring Data Redis
- RocketMQ 5.x (optional by configuration)
- JUnit 5 + Mockito

## Run Locally

1. Start dependencies:

```bash
docker compose up -d
```

2. Create schema in MySQL (`ec_system`) with `src/main/resources/schema.sql`.
3. Start application:

```bash
mvn spring-boot:run
```

## Key APIs

- `POST /api/v1/users`
- `POST /api/v1/users/{userId}/recharge`
- `GET /api/v1/users/{userId}/balance`
- `POST /api/v1/merchants`
- `POST /api/v1/merchants/{merchantId}/inventory`
- `GET /api/v1/merchants/{merchantId}/balance`
- `POST /api/v1/orders`
- `GET /api/v1/orders/{orderId}`
- `GET /api/v1/users/{userId}/orders`
- `GET /api/v1/merchants/{merchantId}/orders`
- `POST /api/v1/settlements/run?date=2026-06-01`

## Test

```bash
mvn test
```
