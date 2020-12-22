package com.macro.mall.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: jeason
 * @Description:
 * @Date: 2020/12/22 22:10
 */
@Data
@AllArgsConstructor
@ToString
public class SkuParam implements Serializable {
    Long skuId;
    Integer quantity;
}
