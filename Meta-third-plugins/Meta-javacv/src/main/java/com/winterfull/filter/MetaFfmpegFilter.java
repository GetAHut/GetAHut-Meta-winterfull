package com.winterfull.filter;

import com.winterfull.enums.ActionType;
import org.bytedeco.javacv.FFmpegFrameFilter;

/**
 * @author : ytxu5
 * @date: 2023/3/29
 */
public class MetaFfmpegFilter extends FFmpegFrameFilter {

    /** 状态控制 */
    public volatile boolean started = false;

    protected ActionType actionType;

    public MetaFfmpegFilter(String videoFilters, String audioFilters, int imageWidth, int imageHeight, int audioChannels) {
        super(videoFilters, audioFilters, imageWidth, imageHeight, audioChannels);
    }

    public MetaFfmpegFilter(ActionType actionType, String filters, int imageWidth, int imageHeight) {
        super(filters, imageWidth, imageHeight);
        this.actionType = actionType;
    }

    public MetaFfmpegFilter(ActionType actionType, String aFilters, int audioChannels) {
        super(aFilters, audioChannels);
        this.actionType = actionType;
    }

    public ActionType getActionType(){
        return this.actionType;
    }


}
