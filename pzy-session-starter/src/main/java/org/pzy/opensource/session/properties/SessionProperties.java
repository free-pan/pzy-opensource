package org.pzy.opensource.session.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author pan
 * @date 2020-01-29
 */
@Setter
@Getter
@ToString
@ConfigurationProperties(prefix = "component.session", ignoreInvalidFields = true)
public class SessionProperties implements Serializable {

    /**
     * sessionid对应的cookie名称
     */
    private String cookieName = "jessionId";

}
