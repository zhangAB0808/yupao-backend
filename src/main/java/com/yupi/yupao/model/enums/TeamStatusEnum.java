package com.yupi.yupao.model.enums;

import lombok.Data;

/**
 * 队伍状态枚举
 */
public enum TeamStatusEnum {

    PUBLIC(0,"公开"),
    PRIVATE(1,"私有"),
    SECRET(2,"加密");



    private int value;

    private String text;

    /**
     * 根据值判断是哪个枚举常量
     * @param value
     * @return
     */
    public static TeamStatusEnum getEnumByValue(Integer value){
        if(value==null){
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if(teamStatusEnum.getValue()==value){
                return teamStatusEnum;
            }
        }
       return null;
    }

     TeamStatusEnum(int value,String text){
        this.value = value;
        this.text=text;
    }

    public int getValue() {
        return value;
    }


    public String getText() {
        return text;
    }

}
