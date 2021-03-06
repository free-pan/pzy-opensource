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

package org.pzy.opensource.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.pzy.opensource.domain.GlobalConstant;

import java.io.Serializable;

/**
 * 分页参数
 *
 * @author pan
 * @date 2019-12-11
 */
@Slf4j
@ToString
public class PageVO implements Serializable {

    private static final long serialVersionUID = -3151144130513721711L;

    /**
     * 分页大小. 默认: 10
     */
    @ApiModelProperty(value = "分页大小", example = "10")
    private Long size;
    /**
     * 页号. 默认: 1
     */
    @ApiModelProperty(value = "页号", example = "1")
    private Long page;

    public PageVO() {
        this.page = GlobalConstant.PAGE_NUM;
        this.size = GlobalConstant.PAGE_SIZE;
    }

    /**
     * 将分页参数规范化. 当分页大小,页号不规范时, 则重置为默认值
     */
    public void format() {
        if (size < GlobalConstant.ZERO || size > GlobalConstant.MAX_PAGE_SIZE) {
            this.size = GlobalConstant.PAGE_SIZE;
        }
        if (page < GlobalConstant.ZERO || page > GlobalConstant.MAX_PAGE_NUM) {
            this.page = GlobalConstant.PAGE_NUM;
        }
    }

    public Long getSize() {
        if (this.size == null || this.size <= 0) {
            if (log.isWarnEnabled()) {
                log.warn("实际的分页大小为[{}],不符合系统规范,将返回默认的分页大小[{}]", this.size, GlobalConstant.PAGE_SIZE);
            }
            return GlobalConstant.PAGE_SIZE;
        }
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getPage() {
        if (this.page == null || this.page <= 0) {
            if (log.isWarnEnabled()) {
                log.warn("实际的页号为[{}],不符合系统规范,将返回默认的分页大小[{}]", this.page, GlobalConstant.PAGE_NUM);
            }
            return GlobalConstant.PAGE_NUM;
        }
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }
}
