package com.yzxie.study.eshopbiz.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-29
 * Description:
 **/
@Repository
public interface ProductQuantityDAO {

    /**
     * 获取需要加载的产品的数量
     * @param productId
     * @return
     */
    long getProductQuantity(@Param("productId") long productId);
}
