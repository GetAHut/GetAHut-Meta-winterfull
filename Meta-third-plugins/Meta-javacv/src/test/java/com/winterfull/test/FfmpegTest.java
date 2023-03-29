package com.winterfull.test;

import com.winterfull.utils.IdCreator;
import com.winterfull.enums.AudioFormat;
import com.winterfull.enums.SimpleRateType;
import com.winterfull.javacv.FfmpegService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
@SpringBootTest
public class FfmpegTest {

    @Test
    public void extractAudioTest(){
        String sourcePath = "D:\\project\\iflytek-workspace\\words\\tool\\video-audio\\fanren.mp4";
        FfmpegService ffmpegService = new FfmpegService(sourcePath, IdCreator.getUUID());
        ffmpegService.videoToAudio(AudioFormat.WAV, SimpleRateType.BIT_16K);

    }
    @Test
    public void addSubtitle(){
        String sourcePath = "D:\\project\\iflytek-workspace\\words\\tool\\video-audio\\fanren.mp4";
        FfmpegService ffmpegService = new FfmpegService(sourcePath, IdCreator.getUUID());
        ffmpegService.videoAddSubtitle();
    }
}
