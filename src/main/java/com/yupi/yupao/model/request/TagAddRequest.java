package com.yupi.yupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class TagAddRequest implements Serializable {

    private String sonTag;
    private String parentTag;
}
