package org.pzy.opensource.codegenerator.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据库连接信息
 *
 * @author pan
 * @date 2020/3/29
 */
@Data
@AllArgsConstructor
public class DbConnectionInfo implements Serializable {

    /**
     * 数据库驱动类.
     */
    private String driverName;
    /**
     * 数据库连接用户名. 如:root
     */
    private String username;
    /**
     * 数据库连接密码. 如:root
     */
    private String password;
    /**
     * 数据库连接url.
     */
    private String url;
}
