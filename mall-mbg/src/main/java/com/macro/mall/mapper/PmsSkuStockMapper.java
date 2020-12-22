package com.macro.mall.mapper;

import com.macro.mall.model.PmsSkuStock;
import com.macro.mall.model.PmsSkuStockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsSkuStockMapper {
    long countByExample(PmsSkuStockExample example);

    int deleteByExample(PmsSkuStockExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsSkuStock record);

    int insertSelective(PmsSkuStock record);

    List<PmsSkuStock> selectByExample(PmsSkuStockExample example);

    PmsSkuStock selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsSkuStock record, @Param("example") PmsSkuStockExample example);

    int updateByExample(@Param("record") PmsSkuStock record, @Param("example") PmsSkuStockExample example);

    int updateByPrimaryKeySelective(PmsSkuStock record);

    int updateByPrimaryKey(PmsSkuStock record);

    /**
     * 查询所有可用库存sku
     * @return
     */
    List<PmsSkuStock> selectAllAvailableSku();

    /**
     * 锁定库存
     * @param productSkuId
     * @param quantity
     * @return
     */
    int updateLockStock(@Param("productSkuId") Long productSkuId, @Param("quantity")  Integer quantity);
}
