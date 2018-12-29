package cn.jeelearn.house.user.utils;

import cn.jeelearn.house.user.service.MailService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/29
 * @Version:v1.0
 */
public class JwtHelper {

    private static final String  SECRET = "session_secret";
    private static final String  ISSUER = "mooc_user";

    /**
     * 生成token
     * @auther: lyd
     * @date: 2018/12/29
     */
    public static String genToken(Map<String, String> claims){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTCreator.Builder builder = JWT.create()
                    .withIssuer(ISSUER)
                    .withExpiresAt(DateUtils.addDays(new Date(), 1));
            claims.forEach((k, v) -> builder.withClaim(k, v));
            return builder.sign(algorithm).toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验证token
     * @auther: lyd
     * @date: 2018/12/29
     */
    public static Map<String, String> verifyToken(String token){
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        Map<String, Claim> map = jwt.getClaims();
        HashMap<String , String> resultMap = Maps.newHashMap();
        //这里是asString，toString的话会是hash码
        map.forEach((k, v) -> resultMap.put(k, v.asString()));
        return resultMap;
    }

}

