package com.winterfull.filter;

import com.winterfull.enums.ActionType;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : ytxu5
 * @date: 2023/3/29
 */
@Slf4j
public class ImageFilter extends MetaFfmpegFilter{

    public ImageFilter(ActionType actionType, String filters, int imageWidth, int imageHeight) {
        super(actionType, filters, imageWidth, imageHeight);
    }
}
