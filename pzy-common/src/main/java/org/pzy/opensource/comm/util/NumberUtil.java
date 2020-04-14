package org.pzy.opensource.comm.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * <p>数字 常用操作方法
 * <p><b>注意</b>: 通常我们在开发的时候需要的是精确的值. 因此要避免直接使用float和double.
 * <p>因为float和double类型主要是为了科学计算和工程计算而设计，他们执行二进制浮点运算，这是为了在广泛的数值范围上提供较为精确的快速近和计算而精心设计的，然而，他们并没有提供完全精确的结果，所以不应该被用于精确的结果的场合
 * <p>所以该帮助类并未提供对float和double加减乘除的直接支持
 * <p>更详细的文章说明详见: <a href='https://blog.csdn.net/gege87417376/article/details/79550749' target='_blank'>解释BigDecimal精度的坑</a> <a href='https://blog.csdn.net/weixin_40459875/article/details/80031570?depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-5&utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-5' target='_blank'>BigDecimal解决浮点精度丢失</a>
 * <p><a href='https://blog.csdn.net/xingbaozhen1210/article/details/80777644?depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1' target='_blank'>BigDecimal的8种精度取舍方式</a>
 * <p><a href='https://blog.csdn.net/qq_33300026/article/details/80610091' target='_blank'>Mysql精度损失--隐式类型转换的坑</a>
 * <p><a href='https://blog.csdn.net/lx12345_/article/details/82048851' target='_blank'>Mybatis提取BigDecimal字段值显示丢失末尾0精度的问题解决</a>
 * <p><a href='https://blog.csdn.net/lihua5419/article/details/81316914' target='_blank'>mysql中float、double、decimal的区别</a>
 * <p><a href='https://blog.csdn.net/MuZiYu2015/article/details/87972031' target='_blank'>MySQL double类型的字段保存不了小数问题解决</a>
 * <p><a href='https://my.oschina.net/OutOfMemory/blog/2250803' target='_blank'>关于Jackson默认丢失Bigdecimal精度问题分析</a>
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
     * 判断 num1 > num2
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return num1 > num2 的boolean结果
     */
    public static boolean gt(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == 1;
    }

    /**
     * 判断 num1 < num2
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return num1 < num2 的boolean结果
     */
    public static boolean lt(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2) == -1;
    }

    /**
     * 判断 num1 >= num2
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return num1 >= num2 的boolean结果
     */
    public static boolean gte(BigDecimal num1, BigDecimal num2) {
        int result = num1.compareTo(num2);
        return (result == 1 || result == 0);
    }

    /**
     * 判断 num1 <= num2
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return num1 <= num2 的boolean结果
     */
    public static boolean lte(BigDecimal num1, BigDecimal num2) {
        int result = num1.compareTo(num2);
        return (result == -1 || result == 0);
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
    public static BigDecimal add(String one, String two) {
        return add(new BigDecimal(one), new BigDecimal(two));
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
    public static BigDecimal subtract(String one, String two) {
        return subtract(new BigDecimal(one), new BigDecimal(two));
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
    public static BigDecimal multiply(String one, String two) {
        return multiply(new BigDecimal(one), new BigDecimal(two));
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
    public static BigDecimal divide(String one, String two) {
        return divide(new BigDecimal(one), new BigDecimal(two));
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

        System.out.println("------------");
        System.out.println(new BigDecimal(0));
        System.out.println(new BigDecimal(1));
        System.out.println(new BigDecimal(1.3));
        System.out.println(new BigDecimal(1.03));
        System.out.println(new BigDecimal(0.1));
        System.out.println(new BigDecimal("0.01"));
        System.out.println(new BigDecimal("0.001"));
        System.out.println(new BigDecimal("0.9"));
        System.out.println(new BigDecimal("0.09"));
        System.out.println(new BigDecimal(0.009));

        System.out.println("----------------");
        System.out.println(0.5 * 3);
        // 这里, 按照我们常规的理解, 得到的应该是0.3, 可实际结果却是0.30000000000000004, 因此不要在需要精确结果的场景使用float和double
        System.out.println(0.1 * 3);
        // 这一条是针对上一个0.1*3的 `错误解决方式`, 你可能想当然的认为使用BigDecimal就能解决精度问题, 但实际上得到的结果依然不是0.3, 因为导致精度问题的是float和double数据类型, 而非BigDecimal
        System.out.println(new BigDecimal(0.1).multiply(new BigDecimal(3)));
        System.out.println("这里才是0.1*0.3得到精确结果的正确解决方式:");
        System.out.println(multiply("0.1", "3"));

        String str = JsonUtil.toJsonString(new Person("张三", new BigDecimal("0.1")));
        System.out.println(str);
        Person p = JsonUtil.toJavaBean(str, Person.class);
        System.out.println(p);

        System.out.println(new BigDecimal("1").compareTo(new BigDecimal("2")));
        System.out.println(new BigDecimal("1").compareTo(new BigDecimal("1")));
        System.out.println(new BigDecimal("2").compareTo(new BigDecimal("1")));
    }


}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Person {
    private String name;
    private BigDecimal salary;
}
