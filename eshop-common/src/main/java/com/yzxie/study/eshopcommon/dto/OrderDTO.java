package com.yzxie.study.eshopcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Author: xieyizun
 * Version: 1.0
 * Date: 2019-08-25
 * Description:
 **/
@Data
public class OrderDTO implements Serializable {
    private String uuid;
    private long productId;
    private String userId;
    private List<OrderItemDTO> orderItemDTOList;
}
