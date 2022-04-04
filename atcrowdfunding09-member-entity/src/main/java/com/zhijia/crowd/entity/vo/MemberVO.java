package com.zhijia.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**注册用户表单数据
 * @author zhijia
 * @create 2022-03-29 12:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO{

    private String loginacct;

    private String userpswd;

    private String username;

    private String email;

    private String phoneNum;

    private String code;
}
