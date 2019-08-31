package com.yzxie.study.eshopqueue.repository;

import com.yzxie.study.eshopcommon.bo.OrderItemBO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-30
 * Description:
 **/
@Repository
public interface OrderItemDAO {
    /**
     * 批量插入订单项记录
     * @param orderItemBOList
     */
    void bulkInsert(@Param("orderItems") List<OrderItemBO> orderItemBOList);
}
