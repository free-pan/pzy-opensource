/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.comm.util;


import cn.hutool.core.codec.Base64;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.pzy.opensource.comm.domain.bo.JwtStandardInfo;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 潘志勇
 * @date 2019-02-08
 */
public class JwtUtil {

    private JwtUtil() {
    }

    private static SecretKey generalKey(String secret) {
        byte[] encodedKey = secret.getBytes();
        // 根据给定的字节数组使用AES加密算法构造一个密钥，使用 encodedKey中的始于且包含 0 到前 leng 个字节这是当然是所有
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    /**
     * 创建jwt
     *
     * @param secret          秘钥[必须是经过base64加密之后的字符串]
     * @param jwtStandardInfo jwt标准信息
     * @param privateParamMap jwt私有信息
     * @return
     */
    public static String createJwt(String secret, JwtStandardInfo jwtStandardInfo, Map<String, Object> privateParamMap) {
        SecretKey key = generalKey(secret);
        String tmpIssuer = jwtStandardInfo.getIssuer();
        tmpIssuer = Base64.encode(tmpIssuer.getBytes());
        String tmpAudience = null;
        if (!StringUtils.isEmpty(jwtStandardInfo.getAudience())) {
            tmpAudience = jwtStandardInfo.getAudience();
            tmpAudience = Base64.encode(tmpAudience.getBytes());
        }

        JwtBuilder jwtBuilder = Jwts.builder();
        if (null != privateParamMap && !privateParamMap.isEmpty()) {
            jwtBuilder
                    // 设置私有数据
                    .setClaims(privateParamMap);
        }
        // 签发时间
        Long createTimeLong = System.currentTimeMillis();
        Date crateTime = new Date(createTimeLong);
        // 过期时间
        Date expireDate = new Date(createTimeLong + TimeUnit.SECONDS.toMillis(jwtStandardInfo.getExpiresAt()));
        String uuid = UUID.randomUUID().toString();
        jwtBuilder
                // 签发时间
                .setIssuedAt(crateTime)
                // 过期时间
                .setExpiration(expireDate)
                // 签发人(使用BCryptPasswordEncoder对签发人加密,使得每次加密后的签发人字符串都不一样)
                .setIssuer(tmpIssuer);
        if (!StringUtils.isEmpty(tmpAudience)) {
            // 接收jwt的一方
            jwtBuilder.setAudience(tmpAudience);
        }
        // 设置jwt唯一标识,可用于防止重放攻击
        jwtBuilder.setId(uuid);
        jwtBuilder.signWith(SignatureAlgorithm.HS256, key);
        return jwtBuilder.compact();
    }

    /**
     * 验证(是否过期,jwt中的签发者与issuer参数是否相同)并解析jwt获取到jwt中的载荷数据
     *
     * @param secret 创建jwt时设置的秘钥
     * @param issuer 创建jwt时设置的签发者
     * @param jwt    jwt
     * @return
     * @throws ExpiredJwtException     当jwt过期时,将抛出该异常
     * @throws IncorrectClaimException jwt中的issuer与当前issuer不匹配时,抛出此异常
     */
    public static Claims verify(String secret, String issuer, String jwt) throws ExpiredJwtException, IncorrectClaimException {
        String tmpIssuer = Base64.encode(issuer.getBytes());
        SecretKey key = generalKey(secret);
        Claims claims = Jwts.parser()
                //设置签名的秘钥
                .setSigningKey(key)
                .requireIssuer(tmpIssuer)
                //设置需要解析的jwt
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}
