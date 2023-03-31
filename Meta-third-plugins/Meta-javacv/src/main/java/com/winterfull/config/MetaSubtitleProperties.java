package com.winterfull.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : ytxu5
 * @date: 2023/3/31
 */
@Data
public class MetaSubtitleProperties {

    /** 字体文件路径 */
    private String fontPath = "font/arialbi.ttf";

}
