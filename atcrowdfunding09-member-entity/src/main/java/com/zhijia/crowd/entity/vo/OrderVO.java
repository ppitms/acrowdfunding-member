package com.zhijia.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhijia
 * @create 2022-04-03 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    //订单号
    private String orderNum;
    //流水单号
    private String payOrderNum;
    //订单金额
    private Double orderAmount;
    //是否开发票
    private Integer invoice;
    //发票抬头
    private String invoiceTitle;
    //备注
    private String orderRemark;
    private String addressId;
    private OrderProjectVO orderProjectVO;
}
