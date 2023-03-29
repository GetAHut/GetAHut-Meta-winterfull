package com.winterfull.domain;

import lombok.Data;

/**
 * subtitle style
 *
 * @author : ytxu5
 * @date: 2023/3/29
 */
@Data
public class Subtitle {

    /*
     * rawtext=fontfile='../sucai/st.ttf':fontsize=50:fontcolor=white:box=1:
     *  boxcolor=black@0.5:boxborderw=50:x=(w-text_w)/2:y=h-text_h-150:text='你好'
     *
     */

    private String fontPath = "D:\\project\\idea-workspace\\Meta-winterfull\\Meta-third-plugins\\Meta-javacv\\src\\main\\resources\\font\\arialbi.ttf";
    private String fontFile;
    private Integer fontSize;
    private String fontColor;
    private String boxColor;
    private String boxRate;
    private Integer boxPadding;
    private String x;
    private Integer y;
    private String text;
}
