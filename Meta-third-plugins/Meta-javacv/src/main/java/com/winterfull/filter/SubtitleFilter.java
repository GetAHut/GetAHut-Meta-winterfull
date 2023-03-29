package com.winterfull.filter;

import com.winterfull.domain.Subtitle;
import com.winterfull.enums.ActionType;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.Frame;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author : ytxu5
 * @date: 2023/3/29
 */
@Slf4j
public class SubtitleFilter extends ImageFilter{

    private Subtitle settings;

    public SubtitleFilter(Subtitle subtitle) {
        super(ActionType.SUBTITLE, "", 0, 0);
        this.settings = subtitle;
    }

    public void subtitleProcess(String subtitle, int imageHeight, int imageWidth) throws Exception {
        if (start){
            this.stop();
            this.start = false;
        }
        this.filters = createSubtitleFilter(subtitle);
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        this.start();
    }

    private String createSubtitleFilter(String subtitle){
        StringBuilder subtitleFilterBuilder = new StringBuilder();
        subtitleFilterBuilder.append("drawtext=");
        String fontPath = this.settings.getFontPath();

        if (StringUtils.hasText(fontPath)) {
            if (Files.notExists(Paths.get(fontPath))) {
                // TODO
                throw new RuntimeException();
            }
            subtitleFilterBuilder.append("fontfile='").append(fontPath).append("'").append(":");
        }
        Integer fontSize = this.settings.getFontSize();
        if (fontSize != null) {
            subtitleFilterBuilder.append("fontsize=").append(fontSize).append(":");
        }
        String fontColor = this.settings.getFontColor();
        if (StringUtils.hasText(fontColor)) {
            subtitleFilterBuilder.append("fontcolor=").append(fontColor).append(":");
        }
        subtitleFilterBuilder.append("box=1:");
        String bgColor = this.settings.getBoxColor();
        if (StringUtils.hasText(bgColor)) {
            subtitleFilterBuilder.append("boxcolor=").append(bgColor);
            String bgTrans = this.settings.getBoxRate();
            if (StringUtils.hasText(bgTrans)) {
                subtitleFilterBuilder.append("@").append(bgTrans);
            }
            subtitleFilterBuilder.append(":");
        }
        Integer padding = this.settings.getBoxPadding();
        if (padding != null) {
            subtitleFilterBuilder.append("boxborderw=").append(padding).append(":");
        }
        subtitleFilterBuilder.append("x=(w-text_w)/2:");
        Integer bottom = this.settings.getY();
        if (bottom != null) {
            subtitleFilterBuilder.append("y=h-text_h-").append(bottom).append(":");
        } else {
            subtitleFilterBuilder.append("y=h-text_h-100:");
        }
        subtitleFilterBuilder.append("text='").append(subtitle).append("'");
        log.debug("subtitle filter : {}", subtitleFilterBuilder);
        return subtitleFilterBuilder.toString();
    }

    @Override
    public void start() throws Exception {
        super.start();
        this.start = true;
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        this.start = false;
    }

    @Override
    public void push(Frame frame) throws Exception {
        this.push(0, frame, avutil.AV_PIX_FMT_BGRA);
    }
}
