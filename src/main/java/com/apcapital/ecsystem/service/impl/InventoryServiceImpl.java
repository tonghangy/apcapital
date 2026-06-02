package com.apcapital.ecsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.apcapital.ecsystem.dto.request.AddInventoryRequest;
import com.apcapital.ecsystem.dto.response.InventoryResponse;
import com.apcapital.ecsystem.entity.Merchant;
import com.apcapital.ecsystem.entity.Product;
import com.apcapital.ecsystem.exception.BusinessException;
import com.apcapital.ecsystem.exception.ErrorCode;
import com.apcapital.ecsystem.mapper.MerchantMapper;
import com.apcapital.ecsystem.mapper.ProductMapper;
import com.apcapital.ecsystem.service.InventoryService;
import java.math.BigDecimal;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * InventoryServiceImpl 是预存账户电商系统中的类。
 *
 * <p>该类型属于服务实现层，用于编排核心业务事务。
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private static final String PRODUCT_CACHE_KEY_PREFIX = "inventory:";
    private final MerchantMapper merchantMapper;
    private final ProductMapper productMapper;
    private final StringRedisTemplate redisTemplate;

    /**
     * 创建实例并注入所需依赖。
     *
     * <p>该构造方法用于完成组件初始化，确保运行期可直接使用。
     * @param merchantMapper 本方法的输入参数
     * @param productMapper 本方法的输入参数
     * @param redisTemplate 本方法的输入参数
     */
    public InventoryServiceImpl(MerchantMapper merchantMapper, ProductMapper productMapper, StringRedisTemplate redisTemplate) {
        this.merchantMapper = merchantMapper;
        this.productMapper = productMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 通过 `addInventory` 操作新增或增加业务状态。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     * @param request 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InventoryResponse addInventory(Long merchantId, AddInventoryRequest request) {
        ensureMerchantExists(merchantId);
        Product existing = productMapper.selectOne(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId)
                .eq(Product::getSku, request.sku())
        );
        if (existing == null) {
            Product product = new Product();
            product.setMerchantId(merchantId);
            product.setSku(request.sku());
            product.setName(request.name());
            product.setPrice(request.price());
            product.setQuantity(request.quantity());
            product.setVersion(0);
            productMapper.insert(product);
            cacheProduct(product);
            return new InventoryResponse(product.getId(), product.getSku(), product.getQuantity());
        }

        existing.setName(request.name());
        existing.setPrice(request.price());
        existing.setQuantity(existing.getQuantity() + request.quantity());
        if (productMapper.updateById(existing) != 1) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE, "inventory update conflict");
        }
        cacheProduct(existing);
        return new InventoryResponse(existing.getId(), existing.getSku(), existing.getQuantity());
    }

    /**
     * 返回 `getByMerchantAndSku` 操作对应的数据结果。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     * @param sku 本方法的输入参数
     * @return 本方法的返回结果
     */
    @Override
    public Product getByMerchantAndSku(Long merchantId, String sku) {
        ensureMerchantExists(merchantId);
        String cacheKey = cacheKey(merchantId, sku);
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null && !cached.isBlank()) {
            Product product = decode(cached);
            if (product != null) {
                return product;
            }
        }
        Product product = productMapper.selectOne(
            new LambdaQueryWrapper<Product>()
                .eq(Product::getMerchantId, merchantId)
                .eq(Product::getSku, sku)
        );
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        cacheProduct(product);
        return product;
    }

    /**
     * 执行 `deductStock` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param product 本方法的输入参数
     * @param quantity 本方法的输入参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductStock(Product product, Integer quantity) {
        if (product.getQuantity() < quantity) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }
        product.setQuantity(product.getQuantity() - quantity);
        if (productMapper.updateById(product) != 1) {
            throw new BusinessException(ErrorCode.CONCURRENT_UPDATE, "stock deduct conflict");
        }
        cacheProduct(product);
    }

    /**
     * 执行 `ensureMerchantExists` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     */
    private void ensureMerchantExists(Long merchantId) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(ErrorCode.MERCHANT_NOT_FOUND);
        }
    }

    /**
     * 执行 `cacheProduct` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param product 本方法的输入参数
     */
    private void cacheProduct(Product product) {
        String value = encode(product);
        redisTemplate.opsForValue().set(cacheKey(product.getMerchantId(), product.getSku()), value);
    }

    /**
     * 执行 `cacheKey` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param merchantId 本方法的输入参数
     * @param sku 本方法的输入参数
     * @return 本方法的返回结果
     */
    private String cacheKey(Long merchantId, String sku) {
        return PRODUCT_CACHE_KEY_PREFIX + merchantId + ":" + sku;
    }

    /**
     * 执行 `encode` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param product 本方法的输入参数
     * @return 本方法的返回结果
     */
    private String encode(Product product) {
        BigDecimal price = product.getPrice() == null ? BigDecimal.ZERO : product.getPrice();
        return String.join(
            "|",
            String.valueOf(product.getId()),
            String.valueOf(product.getMerchantId()),
            product.getSku(),
            product.getName() == null ? "" : product.getName(),
            price.toPlainString(),
            String.valueOf(product.getQuantity()),
            String.valueOf(product.getVersion() == null ? 0 : product.getVersion())
        );
    }

    /**
     * 执行 `decode` 业务逻辑。
     *
     * <p>该方法提供详细说明，便于后续维护与扩展。
     * @param encoded 本方法的输入参数
     * @return 本方法的返回结果
     */
    private Product decode(String encoded) {
        String[] parts = encoded.split("\\|", -1);
        if (parts.length != 7) {
            return null;
        }
        try {
            Product product = new Product();
            product.setId(Long.valueOf(parts[0]));
            product.setMerchantId(Long.valueOf(parts[1]));
            product.setSku(parts[2]);
            product.setName(parts[3]);
            product.setPrice(new BigDecimal(parts[4]));
            product.setQuantity(Integer.valueOf(parts[5]));
            product.setVersion(Integer.valueOf(parts[6]));
            return product;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
