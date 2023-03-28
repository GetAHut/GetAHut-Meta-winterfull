package com.winterfull.asr.domain;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @author : ytxu5
 * @date: 2023/3/28
 */
@Data
public class AsrResult {

    private String code;
    private String descInfo;
    private JSONObject content;

    @Data
    public static class Content {
        private JSONObject orderInfo;
        private JSONObject orderResult;
        private int taskEstimateTime;
    }
}


