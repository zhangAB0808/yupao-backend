package com.yupi.yupao.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 */
@Data
public class TagDto implements Serializable {

    private String text;

    private List<TagChildrenDto> children;
}
