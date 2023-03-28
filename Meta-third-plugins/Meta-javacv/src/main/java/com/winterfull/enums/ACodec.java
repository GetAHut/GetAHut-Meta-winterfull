package com.winterfull.enums;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
public enum ACodec {

    PCM_s16le("pcm_s16le");

    private String aCodec;

    private ACodec(String aCodec){
        this.aCodec = aCodec;
    }

    public String getaCodec(){
        return aCodec;
    }
}
