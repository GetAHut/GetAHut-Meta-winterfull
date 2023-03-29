package com.winterfull.javacv;

import com.winterfull.asr.domain.Lattices;
import com.winterfull.domain.Subtitle;
import com.winterfull.enums.AudioFormat;
import com.winterfull.enums.SimpleRateType;
import com.winterfull.filter.SubtitleFilter;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.*;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.winterfull.utils.FileCommonUtils.filePathAppend;

/**
 * @author : ytxu5
 * @date: 2023/3/27
 */
@Slf4j
public class FfmpegService implements AutoCloseable{

    private String sourcePath;

    private double frameRate;

    private final String traceId;
    private final FFmpegFrameGrabber grabber;

    private FFmpegFrameRecorder audioRecorder;
    private FFmpegFrameRecorder subTitleRecorder;

    private SubtitleFilter subtitleFilter;

    public FfmpegService(String sourcePath, String traceId){
        this.sourcePath = sourcePath;
        this.traceId = traceId;
        this.grabber = new FFmpegFrameGrabber(sourcePath);
    }

    /**
     * TODO 文件路径 -> 文件流
     * @param format
     * @param simpleRateType
     */
    public void videoToAudio(AudioFormat format, SimpleRateType simpleRateType){
        log.info("[traceId = {}]extract audio from video begin, video path : {}", traceId, sourcePath);
        String fileName;
        Frame frame;
        try {
            grabber.start();
            fileName = filePathAppend(new File(sourcePath), "audio", traceId, format.getFormat());
            log.info("[traceId = {}] output audio file path : {}", traceId, fileName);
            this.frameRate = this.grabber.getFrameRate();
            int sampleRate = grabber.getSampleRate();
            log.debug("[traceId = {}]source video sampleRate : {}", traceId, sampleRate);

            audioRecorder = new FFmpegFrameRecorder(fileName, 1);
            audioRecorder.setSampleRate(simpleRateType.getRate());
            audioRecorder.setFormat(format.getFormat());
            audioRecorder.setTimestamp(grabber.getTimestamp());
            audioRecorder.start();

            int index = 0;
            while ((frame = grabber.grab()) != null){
                // 抽帧
                if (frame.samples != null){
                    audioRecorder.recordSamples(frame.sampleRate, frame.audioChannels, frame.samples);
                }
                log.debug("[traceId = {}] video extract audio, current process : {} frame", traceId, index ++);
            }
            audioRecorder.stop();
            audioRecorder.release();
            grabber.stop();

            log.info("[traceId = {}] extract audio from video success, audio path : {}", traceId, fileName);

        } catch (Exception e){
            log.error("[traceId = {}] extract audio from video error, message : {}", traceId, e.getMessage());
            e.printStackTrace();
        }

    }

    public void videoAddSubtitle(){
        String fileName;
        Frame frame;
        try {
            this.grabber.start();
            //
            fileName = filePathAppend(new File(sourcePath), "video", this.traceId, "mp4");
            subTitleRecorder = new FFmpegFrameRecorder(fileName, 1);

            subTitleRecorder.setFormat("mp4");
            subTitleRecorder.setTimestamp(grabber.getTimestamp());
            subTitleRecorder.setImageWidth(grabber.getImageWidth());
            subTitleRecorder.setImageHeight(grabber.getImageHeight());
            subTitleRecorder.start();
            setSubtitleFilter("你好你好你好你好", grabber.getImageHeight(), grabber.getImageWidth());
            while ((frame = this.grabber.grab()) != null){
                if (this.subtitleFilter != null && this.subtitleFilter.start){
                    this.subtitleFilter.push(frame);
                    frame = this.subtitleFilter.pullImage();
                }
                subTitleRecorder.record(frame);
            }
            subTitleRecorder.stop();
            subTitleRecorder.release();
            grabber.stop();

        } catch (Exception e){

        }
    }

    public void setSubtitleFilter(String subtitle, int height, int width) throws FrameFilter.Exception {
        this.subtitleFilter = new SubtitleFilter(new Subtitle());
        this.subtitleFilter.subtitleProcess(subtitle, height, width);
    }

    @Override
    public void close() throws Exception {

    }
}
