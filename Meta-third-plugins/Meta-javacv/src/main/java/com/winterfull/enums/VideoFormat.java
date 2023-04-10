package com.winterfull.enums;

/**
 * @author : ytxu5
 * @date: 2023/4/10
 */
public enum VideoFormat {

    WAV("wav"), MP4("mp4");

    private String format;

    private VideoFormat(String format){
        this.format = format;
    }

    public String getFormat(){
        return format;
    }
}
