package com.muyu.interview.model.dto.user;

import java.io.Serializable;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户注册请求体
 *
 *   
 *  
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 4, max = 16, message = "用户账号长度在4~16位之间")
    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
