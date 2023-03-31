package com.winterfull.test;

import com.winterfull.config.MetaSubtitleProperties;
import com.winterfull.domain.Subtitle;
import com.winterfull.utils.IdCreator;
import com.winterfull.enums.AudioFormat;
import com.winterfull.enums.SimpleRateType;
import com.winterfull.javacv.FfmpegService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
@SpringBootTest
public class FfmpegTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MetaSubtitleProperties metaSubtitleProperties;

    @Test
    public void extractAudioTest(){
        String sourcePath = "D:\\project\\iflytek-workspace\\words\\tool\\video-audio\\fanren.mp4";
        FfmpegService ffmpegService = new FfmpegService(sourcePath, IdCreator.getUUID());
        ffmpegService.videoToAudio(AudioFormat.WAV, SimpleRateType.BIT_16K);

    }
    @Test
    public void addSubtitle() {

        String subtitle = "drawtext=text='Hello, world':fontfile='../fonts/arial.ttf':fontsize=24:x=100:y=100:" +
                "fontcolor=0xff0000:box=1:boxcolor=0x000000@0.5:shadowx=2:shadowy=2:borderw=1:bordercolor=0xffffff";

        String sourcePath = "D:\\project\\iflytek-workspace\\words\\tool\\video-audio\\fanren.mp4";
        FfmpegService ffmpegService = new FfmpegService(sourcePath, IdCreator.getUUID());
        ffmpegService.videoAddSubtitle(createSubtitle());
    }

    private Subtitle createSubtitle(){
        Subtitle subtitle = new Subtitle();
        subtitle.setFontPath("../fonts/arial.ttf")
                .setFontSize(24).setX("100").setY(100)
                .setFontColor("0xff0000").setBoxColor("0x000000")
                .setBoxRate("0.5").setShadowX(2).setShadowY(2)
                .setText("Hello world");

        return subtitle;

    }
}
