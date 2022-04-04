package com.zhijia.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhijia
 * @create 2022-03-29 16:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO implements Serializable{

    public static final long serialVersionUID=1L;

    private Integer id;

    private String username;

    private String email;
}
