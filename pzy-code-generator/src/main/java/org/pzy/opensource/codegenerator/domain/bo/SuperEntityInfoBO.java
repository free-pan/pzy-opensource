package org.pzy.opensource.codegenerator.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 自定义父类实体
 *
 * @author pan
 * @date 2020/3/29
 */
@Data
@AllArgsConstructor
public class SuperEntityInfoBO implements Serializable {

    /**
     * 父类实体名(包名.类名)
     */
    private String entityClassName;
    /**
     * 父类实体的公共字段
     */
    private String[] entityColumns;
}
