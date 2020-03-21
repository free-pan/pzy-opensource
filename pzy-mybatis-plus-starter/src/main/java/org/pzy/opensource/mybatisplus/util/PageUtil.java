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

package org.pzy.opensource.mybatisplus.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.pzy.opensource.domain.PageT;
import org.pzy.opensource.domain.vo.PageVO;

import java.util.List;

/**
 * @author pan
 * @date 2019-12-11
 */
public class PageUtil {

    private PageUtil() {
    }

    /**
     * 系统的分页对象转换为mybatis-plus的分页对象
     *
     * @param pageVO 系统的分页对象
     * @param <T>
     * @return mybatis plus的分页对象
     */
    public static <T> IPage<T> pageVO2MybatisPlusPage(PageVO pageVO) {
        return new Page<>(pageVO.getPage(), pageVO.getSize());
    }

    /**
     * mybatis plus的分页结果转换为系统的分页结果
     *
     * @param mybatisPage mybatis plus的分页结果
     * @param <T>
     * @return 系统的分页结果
     */
    public static <T> PageT<T> mybatisPlusPage2PageT(IPage<T> mybatisPage) {
        PageT<T> page = new PageT<>();
        page.setPage(mybatisPage.getCurrent());
        page.setSize(mybatisPage.getSize());
        page.setTotal(mybatisPage.getTotal());
        page.setList(mybatisPage.getRecords());
        return page;
    }

    /**
     * mybatis plus的分页结果转换为系统的分页结果
     *
     * @param mybatisPage mybatis plus的分页结果
     * @param dataList    实际的数据列表
     * @param <T>
     * @return 系统的分页结果
     */
    public static <T> PageT<T> mybatisPlusPage2PageT(IPage mybatisPage, List<T> dataList) {
        PageT<T> page = new PageT<>();
        page.setPage(mybatisPage.getCurrent());
        page.setSize(mybatisPage.getSize());
        page.setTotal(mybatisPage.getTotal());
        page.setList(dataList);
        return page;
    }
}
