package com.yzxie.study.eshopqueue.repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-30
 * Description:
 **/
@Repository
public interface ProductQuantityDAO {

    /**
     * 递减产品的库存数量
     * @param productId
     */
    void decrQuantity(@Param("productId") long productId);
}
