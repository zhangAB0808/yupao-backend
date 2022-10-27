package com.yupi.yupao.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 队伍和用户信息封装类
 */
@Data
public class TeamUserVo implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 创建人用户id
     */
    private Long userId;

    /**
     * 队伍状态 0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人用户信息
     */
    UserVo createUser;

    /**
     * 队员用户信息
     */
//    List<UserVo> teamMemberList;

    /**
     * 是否已加入队伍
     */
    private boolean hasJoin;

    /**
     * 队伍已经加入的人数
     */
    private Integer hasJoinNum;

}
