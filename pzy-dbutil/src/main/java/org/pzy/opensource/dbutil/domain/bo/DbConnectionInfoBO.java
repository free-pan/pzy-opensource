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

package org.pzy.opensource.dbutil.domain.bo;

import lombok.*;

/**
 * 数据库连接信息
 *
 * @author 潘志勇
 * @date 2019-01-13
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DbConnectionInfoBO {

    /**
     * 连接驱动. 如: com.mysql.jdbc.Driver
     */
    private String driver;
    /**
     * 连接url. 如: jdbc:mysql://localhost:3306/free-db?useUnicode=true&characterEncoding=UTF8&allowMultiQueries=true&useSSL=false
     */
    private String url;
    /**
     * 连接用户. 如: root
     */
    private String user;
    /**
     * 连接密码. 如: root
     */
    private String password;
}
