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

package org.pzy.opensource.comm.mapstruct;

import org.mapstruct.InheritConfiguration;

/**
 * 基础转换类，提供基本的几个方法，直接继承就可以，如果有需要写Mappings的写在方法上
 * 并且接口类上一定要加上 {@link org.mapstruct.Mapper} 注解
 * 默认注解，需要单独定义 如 CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class); 以此进行实例创建和调用
 * 或者如下
 *
 * @author pan
 * @date 2019-12-06
 */
public interface ComplexMapStruct<SearchDTO, AddSource, EditSource, Entity, DTO> extends BaseMapStruct<AddSource, EditSource, Entity, DTO> {

    /**
     * SearchDTO转SearchDTO
     *
     * @param searchDTO
     * @return
     */
    @InheritConfiguration
    SearchDTO searchDtoToSearchDTO(SearchDTO searchDTO);
}
