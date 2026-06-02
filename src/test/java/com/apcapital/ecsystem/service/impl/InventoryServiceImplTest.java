package com.apcapital.ecsystem.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.dto.request.AddInventoryRequest;
import com.apcapital.ecsystem.dto.response.InventoryResponse;
import com.apcapital.ecsystem.entity.Merchant;
import com.apcapital.ecsystem.entity.Product;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.mapper.MerchantMapper;
import com.apcapital.ecsystem.mapper.ProductMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * InventoryServiceImplTest 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@ExtendWith(MockitoExtension.class)
class InventoryServiceImplTest {

    @Mock
    private MerchantMapper merchantMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        inventoryService = new InventoryServiceImpl(merchantMapper, productMapper, redisTemplate);
    }

    @Test
    void addInventory_shouldInsertWhenProductAbsent() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        when(merchantMapper.selectById(1L)).thenReturn(merchant);
        when(productMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);

        AddInventoryRequest request = new AddInventoryRequest("SKU001", "Tea", new BigDecimal("10.00"), 5);
        InventoryResponse response = inventoryService.addInventory(1L, request);

        assertEquals("SKU001", response.sku());
        assertEquals(5, response.quantity());
        verify(productMapper).insert(any(Product.class));
    }

    @Test
    void addInventory_shouldIncreaseWhenProductExists() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        Product existing = new Product();
        existing.setId(2L);
        existing.setMerchantId(1L);
        existing.setSku("SKU001");
        existing.setPrice(new BigDecimal("10.00"));
        existing.setQuantity(7);

        when(merchantMapper.selectById(1L)).thenReturn(merchant);
        when(productMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existing);
        when(productMapper.updateById(existing)).thenReturn(1);

        InventoryResponse response = inventoryService.addInventory(
            1L, new AddInventoryRequest("SKU001", "Tea", new BigDecimal("12.00"), 3)
        );

        assertEquals(10, response.quantity());
        assertEquals(new BigDecimal("12.00"), existing.getPrice());
    }

    @Test
    void getByMerchantAndSku_shouldReturnFromCache() {
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        when(merchantMapper.selectById(1L)).thenReturn(merchant);
        when(valueOperations.get("inventory:1:SKU001")).thenReturn("2|1|SKU001|Tea|9.99|4|0");

        Product product = inventoryService.getByMerchantAndSku(1L, "SKU001");

        assertEquals("SKU001", product.getSku());
        assertEquals(4, product.getQuantity());
        verify(productMapper, never()).selectOne(any(LambdaQueryWrapper.class));
    }

    @Test
    void deductStock_shouldThrowWhenInsufficient() {
        Product product = new Product();
        product.setQuantity(1);
        assertThrows(BusinessException.class, () -> inventoryService.deductStock(product, 2));
    }

    @Test
    void deductStock_shouldUpdateWhenEnough() {
        Product product = new Product();
        product.setId(1L);
        product.setMerchantId(1L);
        product.setSku("SKU001");
        product.setName("Tea");
        product.setPrice(new BigDecimal("10"));
        product.setVersion(0);
        product.setQuantity(8);
        when(productMapper.updateById(product)).thenReturn(1);

        inventoryService.deductStock(product, 3);

        assertEquals(5, product.getQuantity());
        assertTrue(product.getQuantity() >= 0);
    }
}
