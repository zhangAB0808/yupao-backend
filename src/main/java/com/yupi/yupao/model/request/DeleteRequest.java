package com.yupi.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的删除队伍请求体
 */
@Data
public class DeleteRequest implements Serializable {

    private long id;
}
