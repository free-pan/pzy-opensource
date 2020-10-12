package org.pzy.opensource.dbutil.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * java类型和js类型
 *
 * @author pan
 * @since 2020-10-12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JavaAndJsTypeBO implements Serializable {
    private String javaType;
    private String jsType;
}
