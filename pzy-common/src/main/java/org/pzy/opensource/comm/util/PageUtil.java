package org.pzy.opensource.comm.util;

/**
 * 分页计算帮助类
 *
 * @author pan
 * @date 4/11/20
 */
public class PageUtil {

    private PageUtil() {
    }

    /**
     * 根据总记录数和分页大小,计算总页数
     *
     * @param total 总记录数
     * @param size  分页大小
     * @return 总页数
     */
    public static long calcTotalPage(long total, int size) {
        if (size <= 0 || total <= 0) {
            return 0;
        }
        return (total + size - 1) / size;
    }
}
