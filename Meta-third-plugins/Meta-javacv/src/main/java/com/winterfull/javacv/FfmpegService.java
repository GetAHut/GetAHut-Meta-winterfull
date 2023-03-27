package com.winterfull.javacv;

import com.winterfull.enums.AudioFormat;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author : ytxu5
 * @date: 2023/3/27
 */
@Slf4j
@Service
public class FfmpegService {


    /**
     * TODO 文件路径 -> 文件流
     * @param traceId
     * @param sourcePath 源文件路径
     */
    public void videoToAudio(String traceId, String sourcePath, AudioFormat format){
        log.info("[traceId = {}]extract audio from video begin, video path : {}", traceId, sourcePath);
        String fileName = null;
        FFmpegFrameGrabber grabber = null;
        Frame frame = null;
        FFmpegFrameRecorder recorder = null;
        try {
            File file = new File(sourcePath);
            grabber = new FFmpegFrameGrabber(sourcePath);
            grabber.start();
            fileName = file.getAbsolutePath() + traceId + format.getFormat();


        } catch (Exception e){

        }

    }

}
