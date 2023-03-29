package com.winterfull.enums;

/**
 * @author : ytxu5
 * @date: 2023/3/29
 */
public enum ActionType {

    SUBTITLE(0);

    private int type;

    private ActionType(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }
}
