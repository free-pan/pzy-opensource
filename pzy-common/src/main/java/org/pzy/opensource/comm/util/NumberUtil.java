package org.pzy.opensource.comm.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数字 常用操作方法
 *
 * @author pan
 * @date 4/12/20
 */
public class NumberUtil {

    private NumberUtil() {
    }

    /**
     * 四舍五入, 保留newScale位小数
     *
     * @param num      待被操作的数据
     * @param newScale 保留的小数位数
     * @return 结果
     */
    public static BigDecimal halfUp(BigDecimal num, int newScale) {
        return num.setScale(newScale, RoundingMode.HALF_UP);
    }

    /**
     * 保留newScale位小数(纯粹的保留newScale位小数, 不进行四舍五入)
     *
     * @param num      待被操作的数据
     * @param newScale 保留的小数位数
     * @return 结果
     */
    public static BigDecimal down(BigDecimal num, int newScale) {
        return num.setScale(newScale, RoundingMode.DOWN);
    }

    /**
     * 将数值进行格式化输出
     *
     * @param num     待格式化数值
     * @param pattern 格式化模板
     * @return 格式化之后的数字字符串
     */
    public static String numberFormatToStr(BigDecimal num, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(num);
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(BigDecimal one, BigDecimal two) {
        return one.add(two);
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(Float one, Float two) {
        return add(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(Double one, Double two) {
        return add(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(Double one, Float two) {
        return add(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(BigDecimal one, Float two) {
        return add(one, new BigDecimal(two));
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(Float one, BigDecimal two) {
        return add(new BigDecimal(one), two);
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(BigDecimal one, Double two) {
        return add(one, new BigDecimal(two));
    }

    /**
     * (加法)两值相加. `one + two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相加后的结果
     */
    public static BigDecimal add(Double one, BigDecimal two) {
        return add(new BigDecimal(one), two);
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(BigDecimal one, BigDecimal two) {
        return one.subtract(two);
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(Float one, Float two) {
        return subtract(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(Double one, Double two) {
        return subtract(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(Double one, Float two) {
        return subtract(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(Float one, Double two) {
        return subtract(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(BigDecimal one, Double two) {
        return subtract(one, new BigDecimal(two));
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(Double one, BigDecimal two) {
        return subtract(new BigDecimal(one), two);
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(BigDecimal one, Float two) {
        return subtract(one, new BigDecimal(two));
    }

    /**
     * (减法)两值相减. `one - two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相减后的结果
     */
    public static BigDecimal subtract(Float one, BigDecimal two) {
        return subtract(new BigDecimal(one), two);
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(BigDecimal one, BigDecimal two) {
        return one.multiply(two);
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(Float one, Float two) {
        return multiply(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(Double one, Double two) {
        return multiply(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(Double one, Float two) {
        return multiply(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(Float one, Double two) {
        return multiply(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(Float one, BigDecimal two) {
        return multiply(new BigDecimal(one), two);
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(BigDecimal one, Float two) {
        return multiply(one, new BigDecimal(two));
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(Double one, BigDecimal two) {
        return multiply(new BigDecimal(one), two);
    }

    /**
     * (乘法)两值相乘. `one * two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相乘后的结果
     */
    public static BigDecimal multiply(BigDecimal one, Double two) {
        return multiply(one, new BigDecimal(two));
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(BigDecimal one, BigDecimal two) {
        return one.divide(two);
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(Float one, Float two) {
        return divide(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(Double one, Double two) {
        return divide(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(Double one, Float two) {
        return divide(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(Float one, Double two) {
        return divide(new BigDecimal(one), new BigDecimal(two));
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(Float one, BigDecimal two) {
        return divide(new BigDecimal(one), two);
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(BigDecimal one, Float two) {
        return divide(one, new BigDecimal(two));
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(Double one, BigDecimal two) {
        return divide(new BigDecimal(one), two);
    }

    /**
     * (除法)两值相除. `one / two`
     *
     * @param one 值一
     * @param two 值二
     * @return 相除后的结果
     */
    public static BigDecimal divide(BigDecimal one, Double two) {
        return divide(one, new BigDecimal(two));
    }

    public static void main(String[] args) {
        System.out.println(halfUp(new BigDecimal(0.523), 2));
        System.out.println(halfUp(new BigDecimal(0.543), 2));
        System.out.println(halfUp(new BigDecimal(0.545), 2));
        System.out.println(halfUp(new BigDecimal(0.546), 2));
        System.out.println(halfUp(new BigDecimal(0.5), 2));
        System.out.println(halfUp(new BigDecimal(0.4), 2));
        System.out.println(halfUp(new BigDecimal(0.4), 3));
        System.out.println(halfUp(new BigDecimal(0.1), 4));

        System.out.println("------------");
        System.out.println(down(new BigDecimal(0.520), 2));
        System.out.println(down(new BigDecimal(0.521), 2));
        System.out.println(down(new BigDecimal(0.524), 2));
        System.out.println(down(new BigDecimal(0.525), 2));
        System.out.println(down(new BigDecimal(0.526), 2));
        System.out.println(down(new BigDecimal(0.529), 2));

        System.out.println("------------");
        System.out.println(numberFormatToStr(new BigDecimal(0.529), "0.00"));
        System.out.println(numberFormatToStr(new BigDecimal(987.529), "0.00"));
        System.out.println(numberFormatToStr(new BigDecimal(987.52), "0.00"));
        System.out.println(numberFormatToStr(new BigDecimal(987.5), "0.00元"));
        System.out.println(numberFormatToStr(new BigDecimal(87.5), "$0.00"));

        System.out.println("------------");
        System.out.println(add(new BigDecimal(1.3), new BigDecimal(0.02)));
    }
}
