package com.yupi.yupao.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
public class ChatContentDto implements Serializable {

    private Long id;

    private String content;
}
