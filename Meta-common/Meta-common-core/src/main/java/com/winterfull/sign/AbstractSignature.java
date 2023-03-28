package com.winterfull.sign;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.SignatureException;

/**
 * 签名抽象类
 *
 * @author : ytxu5
 * @date: 2023/3/28
 */
@Data
@Slf4j
public abstract class AbstractSignature {

    private String appId;
    private String secret;
    private String url;
    private String encryptType;
    private String originSign;
    protected String sign;
    private String timestamp;
    protected String requestMethod = "GET";

    public AbstractSignature(String appId, String secret, String url){
        this.appId = appId;
        this.secret = secret;
        this.url = url;
        this.timestamp = generateTimestamp();

    }

    public AbstractSignature(String appId, String secret, String url, boolean isPost){
        this(appId, secret, url);
        if (isPost){
            this.requestMethod = "POST";
        }
    }

    private String generateTimestamp(){
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    /**
     * 依据SDK规则生成签名
     * @return
     * @throws SignatureException
     */
    public abstract String doSign() throws SignatureException;

    /**
     * 原始签名
     * @return
     * @throws SignatureException
     */
    public String generateOriginSign() throws SignatureException {
        try {
            URL url = new URL(this.getUrl());

            return "host: " + url.getHost() + "\n" +
                    "date: " + this.getTimestamp() + "\n" +
                    "GET " + url.getPath() + " HTTP/1.1";
        } catch (MalformedURLException e) {
            throw new SignatureException("MalformedURLException:" + e.getMessage());
        }
    }
}
