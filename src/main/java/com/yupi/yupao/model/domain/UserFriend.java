package com.yupi.yupao.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 存储用户与好友之间的对应关系及聊天内容
 * @author Administrator
 * @TableName user_friend
 */
@TableName(value ="user_friend")
@Data
public class UserFriend implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long userId;

    /**
     * 
     */
    private Long friendId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;


    private String chatContent;

    /**
     * 
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}