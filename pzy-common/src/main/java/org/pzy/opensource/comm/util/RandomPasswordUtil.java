package org.pzy.opensource.comm.util;

import java.util.Random;

/**
 * 生成随机密码
 *
 * @author pan
 * @date 2020/3/26
 */
public class RandomPasswordUtil {

    /**
     * 数字
     */
    private static final String NUM = "0123456789";
    /**
     * 小写字母
     */
    private static final String ENGLISH = "abcdefghijklmnopqrstuvwxyz";
    /**
     * 大写字母
     */
    private static final String ENGLISH_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /**
     * 特殊字符
     */
    private static final String SYMBOL = "!@#$%^&*_+-{}<>*";

    public RandomPasswordUtil() {
    }

    /**
     * 根据source内容,生成length个长度的随机数字
     *
     * @param length 需要生成的随机数字长度
     * @param source 所有可选的随机字符串
     * @return 生成的随机数字
     */
    public static final String randomLengthStr(int length, String source) {
        Random random = new Random();
        int numSize = source.length();
        int randomPos = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomPos = random.nextInt(numSize);
            stringBuilder.append(source.charAt(randomPos));
        }
        return stringBuilder.toString();
    }

    /**
     * 生成随机6位密码
     *
     * @return 随机验证码
     */
    public final static String generateSixRandomPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(randomLengthStr(1, NUM));
        stringBuilder.append(randomLengthStr(2, ENGLISH));
        stringBuilder.append(randomLengthStr(1, SYMBOL));
        stringBuilder.append(randomLengthStr(2, ENGLISH_UPPER));
        return stringBuilder.toString();
    }

    /**
     * 生成6位随机的数字+小写英文字母的验证码
     *
     * @return 随机验证码
     */
    public final static String generateSixRandomNumberWordVerifyCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(randomLengthStr(1, ENGLISH));
        stringBuilder.append(randomLengthStr(2, NUM));
        stringBuilder.append(randomLengthStr(1, ENGLISH));
        stringBuilder.append(randomLengthStr(1, NUM));
        stringBuilder.append(randomLengthStr(1, ENGLISH));
        return stringBuilder.toString();
    }

    /**
     * 生成8位随机密码
     *
     * @return 随机密码
     */
    public final static String generateEightRandomPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(randomLengthStr(2, NUM));
        stringBuilder.append(randomLengthStr(2, ENGLISH));
        stringBuilder.append(randomLengthStr(2, SYMBOL));
        stringBuilder.append(randomLengthStr(2, ENGLISH_UPPER));
        return stringBuilder.toString();
    }

}
