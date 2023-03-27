package com.winterfull.enums;

/**
 * @author : ytxu5
 * @date: 2023/3/27
 */
public enum AudioFormat {

    WAV("wav"), MP3("mp3"), PCM("pcm");

    private String format;

    private AudioFormat(String format){
        this.format = format;
    }

    public String getFormat(){
        return format;
    }
}
