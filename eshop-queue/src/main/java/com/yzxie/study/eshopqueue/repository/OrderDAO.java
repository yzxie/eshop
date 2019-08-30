package com.yzxie.study.eshopqueue.repository;

import com.yzxie.study.eshopcommon.bo.OrderBO;
import org.springframework.stereotype.Repository;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Repository
public interface OrderDAO {
    /**
     * 插入记录
     * @param orderBO
     * @return
     */
    int insert(OrderBO orderBO);
}
