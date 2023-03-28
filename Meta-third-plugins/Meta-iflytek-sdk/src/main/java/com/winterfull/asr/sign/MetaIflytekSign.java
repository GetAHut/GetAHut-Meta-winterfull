package com.winterfull.asr.sign;

import com.winterfull.sign.AbstractSignature;
import com.winterfull.utils.CryptTools;
import org.apache.commons.lang3.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * 讯飞 语音转写SDK
 * url: https://www.xfyun.cn/doc/asr/ifasr_new/API.html
 *
 * @author : ytxu5
 * @date: 2023/3/28
 */
public class MetaIflytekSign extends AbstractSignature {

    public MetaIflytekSign(String appId, String secret){
        super(appId, secret, null);
    }

    @Override
    public String doSign() throws SignatureException {
        if (StringUtils.isEmpty(this.sign)){
            this.setOriginSign(generateOriginSign());
            this.sign = generateSignature();
        }
        return this.sign;
    }


    /**
     * 生成最终的签名，需要先生成原始sign
     *
     * @throws SignatureException
     */
    public String generateSignature() throws SignatureException {
        return CryptTools.hmacEncrypt(CryptTools.HMAC_SHA1, this.getOriginSign(), this.getSecret());
    }

    /**
     * 生成待加密原始字符
     *
     * @throws NoSuchAlgorithmException
     */
    @Override
    public String generateOriginSign() throws SignatureException {
        return CryptTools.md5Encrypt(this.getAppId() + this.getTimestamp());
    }
}
