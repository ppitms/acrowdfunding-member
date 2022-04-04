package com.zhijia.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhijia
 * @create 2022-04-02 18:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProjectVO implements Serializable{
    public static final long serialVersionUID=1L;


    private Integer id;

    private String projectName;

    private String launchName;

    private String returnContent;

    private Integer returnCount;

    private Integer supportPrice;

    private Integer freight;

    private Integer orderId;

    private Integer signalPurchase;

    private Integer purchase;
}
