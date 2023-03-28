package com.winterfull.asr;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.winterfull.asr.domain.AsrResult;
import com.winterfull.asr.http.MetaIflytekHttpUtils;
import com.winterfull.asr.sign.MetaIflytekSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.SignatureException;
import java.util.HashMap;

/**
 * 讯飞转写sdk调用
 * https://www.xfyun.cn/doc/asr/ifasr_new/API.html
 *
 * @author : ytxu5
 * @date: 2023/3/28
 */
@Slf4j
public class AsrSdkService {

    private static final String HOST = "https://raasr.xfyun.cn";
    private static final String appid = "d96b9cbe";
    private static final String secret = "9ee261d10c340a2d1f36b3a1b8bca187";
    private static final String callBackUrl = "";


    /**
     * 文件上传
     * @param traceId
     * @param filePath
     * @param audioDuration
     * @return orderId 依据此Id 获取结果
     * @throws FileNotFoundException
     */
    public static String upload(String traceId, String filePath, String audioDuration) throws FileNotFoundException, SignatureException {
        log.info("[traceId = {}] iflytek asr sdk call upload start...", traceId );
        HashMap<String, Object> map = new HashMap<>(16);
        File audio = new File(filePath);
        String fileName = audio.getName();
        long fileSize = audio.length();
        map.put("appId", appid);
        map.put("fileSize", fileSize);
        map.put("fileName", fileName);
        map.put("duration", audioDuration);
        MetaIflytekSign metaIflytekSign = new MetaIflytekSign(appid, secret);
        map.put("signa", metaIflytekSign.doSign());
        map.put("ts", metaIflytekSign.getTimestamp());
//        map.put("callbackUrl", callBackUrl);

        String paramString = MetaIflytekHttpUtils.parseMapToPathParam(map);
        System.out.println("upload paramString:" + paramString);

        String url = HOST + "/v2/api/upload" + "?" + paramString;
        log.debug("[traceId = {}] iflytek asr sdk upload url : {}", traceId, url);
        String response = MetaIflytekHttpUtils.iflytekUpload(url, new FileInputStream(audio));

        log.info("[traceId = {}] iflytek asr sdk call upload success, result : {}", traceId, response);
        return response;
    }

    public static String getResult(String traceId, String orderId) throws InterruptedException, SignatureException {
        log.info("[traceId = {}] iflytek asr sdk call get result start...", traceId);
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("orderId", orderId);
        MetaIflytekSign metaIflytekSign = new MetaIflytekSign(appid, secret);
        map.put("signa", metaIflytekSign.doSign());
        map.put("ts", metaIflytekSign.getTimestamp());
        map.put("appId", appid);
        map.put("resultType", "transfer");
        String paramString = MetaIflytekHttpUtils.parseMapToPathParam(map);
        String url = HOST + "/v2/api/getResult" + "?" + paramString;
        log.info("[traceId = {}] iflytek asr sdk call get result url : {}", traceId, url);
        while (true) {
            String response = MetaIflytekHttpUtils.iflyrecGet(url);
            log.debug("[traceId = {}] iflytek asr sdk call get result : {}", traceId, response);
            AsrResult result = JSONObject.parseObject(response, AsrResult.class);
//            Integer status = result.getContent().getOrderInfo().getInteger("status");
            JSONObject content = result.getContent();
            String orderInfo = content.getString("orderInfo");
            Integer status = JSONObject.parse(orderInfo).getInteger("status");
            if (status == 4) {
                log.debug("[traceId = {}] iflytek asr sdk call get result end : {}", traceId, response);
                return response;
            } else if (status == -1) {
                log.debug("[traceId = {}] iflytek asr sdk call get result failed : {}", traceId, response);
                break;
            } else {
                log.debug("[traceId = {}] iflytek asr sdk call get result doing...  status : {}", traceId, status);
                //建议使用回调的方式查询结果，查询接口有请求频率限制
                Thread.sleep(7000);
            }
        }
        return null;
    }

    class JsonParse {
        Content content;
    }

    class Content {
        OrderInfo orderInfo;
    }

    class OrderInfo {
        Integer status;
    }

    public static void main(String[] args) throws FileNotFoundException, InterruptedException, SignatureException {
        String filePath = "D:\\project\\iflytek-workspace\\words\\tool\\video-audio\\合成音频.wav";
        String result = upload("123", filePath, "98000");
        /*
         * {
                "code": "000000",
                "descInfo": "success",
                "content": {
                    "orderId": "DKHJQ202209021522090215490FAAE7DD0008C",
                    "taskEstimateTime": 28000
                }
            }
         */
        JSONObject json = JSONObject.parse(result);
        String content = json.getString("content");
        String orderId = JSONObject.parse(content).getString("orderId");
        System.out.println(orderId);
        // DKHJQ2023032817181956000063C003E000000
        String resp = getResult("123", orderId);
        System.out.println(resp);
    }
}
