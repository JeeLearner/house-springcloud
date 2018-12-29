package cn.jeelearn.house.user.utils;

import com.google.common.base.Throwables;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.Instant;

/**
 * @Description: hash加密工具类
 * @Auther: lyd
 * @Date: 2018/12/12
 * @Version:v1.0
 */
public class HashUtils {

    private static final HashFunction FUNCTION = Hashing.md5();
    private static final HashFunction MURMUR_FUNC = Hashing.murmur3_128();

    private static final String SALT = "mooc.com";

    public static String encryPassword(String password){
        HashCode hashCode = FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
        return hashCode.toString();
    }

    public static String hashString(String input){
        HashCode code = null;
        try {
            code = MURMUR_FUNC.hashBytes(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            Throwables.propagate(e);
        }
        return code.toString();
    }

    public static void main(String[] args) {
        System.out.println(encryPassword("111111"));
        System.out.println(hashString("111111"));
    }

}

