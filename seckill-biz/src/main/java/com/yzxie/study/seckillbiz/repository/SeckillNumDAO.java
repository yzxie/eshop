package com.yzxie.study.seckillbiz.repository;

import org.springframework.stereotype.Repository;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-29
 * Description:
 **/
@Repository
public interface SeckillNumDAO {

    /**
     * 获取需要加载的产品的数量
     * @param productId
     * @return
     */
    long loadSeckillNum(long productId);
}
