package com.winterfull.javacv;

import com.winterfull.enums.AudioFormat;
import com.winterfull.enums.SimpleRateType;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.winterfull.utils.FileCommonUtils.filePathAppend;

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
    public void videoToAudio(String traceId, String sourcePath, AudioFormat format, SimpleRateType simpleRateType){
        log.info("[traceId = {}]extract audio from video begin, video path : {}", traceId, sourcePath);
        String fileName;
        FFmpegFrameGrabber grabber;
        Frame frame;
        FFmpegFrameRecorder recorder;
        try {
            File file = new File(sourcePath);
            grabber = new FFmpegFrameGrabber(sourcePath);
            grabber.start();
            fileName = filePathAppend(file, traceId, format.getFormat());
            log.info("[traceId = {}] output audio file path : {}", traceId, fileName);

            int sampleRate = grabber.getSampleRate();
            log.debug("[traceId = {}]source video sampleRate : {}", traceId, sampleRate);

            recorder = new FFmpegFrameRecorder(fileName, 1);
            recorder.setSampleRate(simpleRateType.getRate());
            recorder.setFormat(format.getFormat());
            recorder.setTimestamp(grabber.getTimestamp());
            recorder.start();

            int index = 0;
            while ((frame = grabber.grab()) != null){
                // 抽帧
                if (frame.samples != null){
                    recorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                }
                log.debug("[traceId = {}] video extract audio, current process : {} frame", traceId, index ++);
            }
            recorder.stop();
            recorder.release();
            grabber.stop();

            log.info("[traceId = {}] extract audio from video success, audio path : {}", traceId, fileName);

        } catch (Exception e){
            log.error("[traceId = {}] extract audio from video error, message : {}", traceId, e.getMessage());
            e.printStackTrace();
        }

    }

}
