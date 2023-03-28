package com.winterfull.test;

import com.winterfull.utils.IdCreator;
import com.winterfull.enums.AudioFormat;
import com.winterfull.enums.SimpleRateType;
import com.winterfull.javacv.FfmpegService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
@SpringBootTest
public class FfmpegTest {

    @Autowired
    private FfmpegService ffmpegService;



    @Test
    public void extractAudioTest(){
        String sourcePath = "D:\\project\\iflytek-workspace\\words\\tool\\video-audio\\fanren.mp4";
        ffmpegService.videoToAudio(IdCreator.getUUID(), sourcePath, AudioFormat.WAV, SimpleRateType.BIT_16K);
    }
}
