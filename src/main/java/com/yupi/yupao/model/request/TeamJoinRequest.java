package com.yupi.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamJoinRequest implements Serializable {

    /**
     * teamId
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;

}
