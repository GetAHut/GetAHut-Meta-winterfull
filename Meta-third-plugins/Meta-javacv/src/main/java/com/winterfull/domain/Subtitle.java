package com.winterfull.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.file.Path;

/**
 * subtitle style
 *
 * @author : ytxu5
 * @date: 2023/3/29
 */
@Data
@Accessors(chain = true)
public class Subtitle {

    /*
     * rawtext=fontfile='D:\project\idea-workspace\Meta-winterfull\Meta-third-plugins\Meta-javacv\target\test-classes\font\arialbi.ttf':fontsize=50:fontcolor=white:box=1:
     *  boxcolor=black@0.5:boxborderw=50:x=(w-text_w)/2:y=h-text_h-150:text='你好'
     *
     */

    private String fontPath;
    private Integer fontSize;
    private String fontColor;
    private String boxColor;
    private String boxRate;
    private Integer boxPadding;
    private String x;
    private Integer y;
    private double shadowX;
    private double shadowY;
    private String text;
}
