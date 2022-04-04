package com.zhijia.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhijia
 * @create 2022-04-01 11:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortalTypeVO {

    private Integer id;
    private String name;
    private String remark;

    private List<PortalProjectVO> portalProjectVOList;
}
