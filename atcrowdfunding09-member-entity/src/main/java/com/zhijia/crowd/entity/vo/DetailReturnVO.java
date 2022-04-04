package com.zhijia.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhijia
 * @create 2022-04-01 17:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnVO {

    //回报信息主键
    private Integer returnId;

    //需支持的金额
    private Integer supportMoney;

    //单笔限购
    private Integer signalPurchase;

    //具体限额数量
    private Integer purchase;

    //支持者数量
    private Integer supproterCount;

    //运费 0为包邮
    private Integer freight;

    //多少天发货
    private Integer returnDate;

    //回报内容
    private String content;

}
