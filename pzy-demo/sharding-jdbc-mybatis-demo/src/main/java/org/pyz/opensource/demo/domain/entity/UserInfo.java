package org.pyz.opensource.demo.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * UserInfo
 *
 * @author pan
 * @date 6/5/20
 */
@Data
public class UserInfo implements Serializable {

    private Long userId;
    private String userName;
    private String account;
    private String password;
}
