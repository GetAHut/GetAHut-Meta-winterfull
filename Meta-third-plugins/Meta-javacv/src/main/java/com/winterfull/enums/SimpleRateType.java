package com.winterfull.enums;

/**
 * @author : ytxu5
 * @date: 2023/3/27
 */
public enum SimpleRateType {

    BIT_8K(8000), BIT_16K(16000);

    private Integer rate;

    private SimpleRateType(Integer rate){
        this.rate = rate;
    }

    public Integer getRate(){
        return rate;
    }
}
