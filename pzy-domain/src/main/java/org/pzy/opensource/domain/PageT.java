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

package org.pzy.opensource.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.pzy.opensource.domain.vo.PageVO;

import java.util.Collection;

/**
 * @author pan
 * @date 2019-12-11
 */
@Data
public class PageT<T> extends PageVO {

    private static final long serialVersionUID = -3963224962952214829L;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private Long total;
    /**
     * 分页结果集
     */
    @ApiModelProperty("分页结果集")
    private Collection<T> list;

    public PageT() {
        super();
        this.total = GlobalConstant.ZERO;
    }


    public static <T>PageT<T> EMPTY(){
        return new PageT<>();
    }

}
