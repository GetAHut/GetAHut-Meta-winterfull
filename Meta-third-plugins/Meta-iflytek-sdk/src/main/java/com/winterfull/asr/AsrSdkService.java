package com.winterfull.asr;

import com.alibaba.fastjson2.JSONObject;
import com.winterfull.asr.domain.AsrResult;
import com.winterfull.asr.http.MetaIflytekHttpUtils;
import com.winterfull.asr.sign.MetaIflytekSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private static final String CALLBACK_FAILED = "-1";

    // TODO 本地缓存 -> redis
    private static Map<String, String> cache = new ConcurrentHashMap<>();


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
        if (cache.get(traceId) != null){
            return cache.get(traceId);
        }
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
        if (StringUtils.isNotBlank(callBackUrl)){
            map.put("callbackUrl", callBackUrl);
        }

        String paramString = MetaIflytekHttpUtils.parseMapToPathParam(map);
        log.debug("[traceId = {}] iflytek asr sdk upload paramString : {}", traceId, paramString);
        String url = HOST + "/v2/api/upload" + "?" + paramString;
        log.debug("[traceId = {}] iflytek asr sdk upload url : {}", traceId, url);
        String response = MetaIflytekHttpUtils.iflytekUpload(url, new FileInputStream(audio));
        log.info("[traceId = {}] iflytek asr sdk call upload success, result : {}", traceId, response);
        String orderId = getOrderId(response);
        cache.put(traceId, orderId);
        return orderId;
    }

    private static String getOrderId(String response){
        JSONObject json = JSONObject.parse(response);
        String content = json.getString("content");
        return JSONObject.parse(content).getString("orderId");
    }

    public static String getResult(String traceId, String orderId, boolean isSync) throws InterruptedException, SignatureException {
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
        return doGetResult(traceId, url, isSync);
    }

    /**
     *
     * @param traceId
     * @param orderId
     * @return
     * @throws InterruptedException
     * @throws SignatureException
     */
    public static String getResult(String traceId, String orderId) throws InterruptedException, SignatureException {
        return getResult(traceId, orderId, true);
    }

    /**
     * GET http://ip:prot/server/xxx?orderId=DKHJQ202004291620042916580FBC96690001F&status=1
     * @param orderId
     * @param status
     * @return
     */
    public static String callBack(String orderId, String status) throws SignatureException, InterruptedException {
        if (CALLBACK_FAILED.equals(status)){
            // TODO
            throw new RuntimeException("回调失败");
        }
        if (!cache.get(orderId).equals(orderId)){
            throw new RuntimeException("订单丢失");
        }
        return getResult(cache.get(orderId), orderId, false);
    }

    private static String doGetResult(String traceId, String url, boolean isSync) throws InterruptedException {
        while (isSync) {
            String response = MetaIflytekHttpUtils.iflyrecGet(url);
            log.debug("[traceId = {}] iflytek asr sdk call get result : {}", traceId, response);
            Integer status = getResultCode(response);
            if (status == 4) {
                log.debug("[traceId = {}] iflytek asr sdk call get result end : {}", traceId, response);
                return response;
            } else if (status == -1) {
                log.debug("[traceId = {}] iflytek asr sdk call get result failed : {}", traceId, response);
                // TODO
                throw new RuntimeException("");
            } else {
                log.debug("[traceId = {}] iflytek asr sdk call get result doing...  status : {}", traceId, status);
                Thread.sleep(7000);
            }
        }
        String response = MetaIflytekHttpUtils.iflyrecGet(url);
        log.debug("[traceId = {}] iflytek asr sdk call get result : {}", traceId, response);
        Integer status = getResultCode(response);
        if (status == 4){
            log.debug("[traceId = {}] iflytek asr sdk call get result end : {}", traceId, response);
            return response;
        }
        return null;
    }

    private static Integer getResultCode(String response){
        AsrResult result = JSONObject.parseObject(response, AsrResult.class);
        JSONObject content = result.getContent();
        String orderInfo = content.getString("orderInfo");
        return JSONObject.parse(orderInfo).getInteger("status");
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
