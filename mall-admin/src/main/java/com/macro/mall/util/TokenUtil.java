package com.macro.mall.util;
import cn.hutool.core.util.ObjectUtil;
import com.macro.mall.dto.TokenValidResultDto;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * Token工具类
 */
public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    public final static String PROXY_NAME = "proxyName";
    public final static String PROXY_ID = "proxyId";
    public final static String TYPE = "type";

    /**
     * 生成Token
     * @param account
     * @return
     */
    public static String buildJWT(String openId, Long id,String mobile) {
        try {
            /**
             * 1.创建一个32-byte的密匙
             */
            MACSigner macSigner = new MACSigner(secretKey);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 7);
            /**
             * 2. 建立payload 载体
             */
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject("doi")
                    .issuer("http://www.doiduoyi.com")
                    .expirationTime(cal.getTime())
                    .claim("openId",openId)
                    .claim("id", id)
                    .build();

            /**
             * 3. 建立签名
             */
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(macSigner);

            /**
             * 4. 生成token
             */
            String token = signedJWT.serialize();
            return token;
        } catch (KeyLengthException e) {
            e.printStackTrace();
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String createToken(String openId, Long id,String mobile) throws JOSEException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                new JWTClaimsSet.Builder()
                        .issuer(openId)
                        .jwtID(String.valueOf(id))
                        .claim("mobile", mobile)
                        .expirationTime(cal.getTime())
                        .issueTime(new Date())
                        .build()
        );
        signedJWT.sign(new MACSigner(secretKey));
        return signedJWT.serialize();
    }

    public static void validateToken(String token) throws Exception {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey);
            //校验是否有效
            if (!jwt.verify(verifier)) {
                throw new Exception("Token 无效");
            }

            //校验超时
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            if (new Date().after(expirationTime)) {
                throw new Exception("Token 已过期");
            }

            //获取载体中的数据
            Object openId = jwt.getJWTClaimsSet().getClaim("openId");
            System.out.println("openId为" + openId);
            //是否有openUid
            if (ObjectUtil.isNull(openId)){
                throw new Exception("token已过期");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getIdByToken(String token) throws Exception {
        SignedJWT jwt = SignedJWT.parse(token);
        Object id = jwt.getJWTClaimsSet().getClaim("id");
        return ObjectUtil.isNull(id)? null: Integer.valueOf(id.toString());
    }

    public static void main(String[] args) throws Exception {
        validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb2kiLCJvcGVuSWQiOiJvUm52cjRsc1pLUmMyUmJuS3lyVnpGMTBCRmVZIiwiaXNzIjoiaHR0cDpcL1wvd3d3LmRvaWR1b3lpLmNvbSIsImlkIjo4LCJleHAiOjE2MDc4NjA2MDF9.LlEkKk-m3m9rYbxSV2rrPJpgSE6iISOwIUMV6nLTRBI");
    }

    private final static byte[] secretKey = Base64.getDecoder().decode("c8L3/dA0Z4AwEK4U+aXhUkAGbQfjCeO+P5CiYCC9V2o=");

    public static byte[] getSecretKey() {
        return secretKey;
    }
}
