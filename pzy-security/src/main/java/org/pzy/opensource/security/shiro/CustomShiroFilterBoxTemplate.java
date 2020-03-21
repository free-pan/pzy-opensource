package org.pzy.opensource.security.shiro;

import lombok.Getter;
import lombok.Setter;
import org.pzy.opensource.security.shiro.filter.WinterShiroFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放所有自定义shiro过滤器. 注意:不要将自定义过滤器注入到spring容器中,如果将自定义的shiro过滤器暴露到spring容器中,会导致shiro的过滤器链出现问题.
 * <br>
 * 实际开发中,应该实现自己的CustomShiroFilterBoxTemplate子类
 *
 * @author pan
 * @date 2020-01-28
 */
@Setter
@Getter
public class CustomShiroFilterBoxTemplate {

    private List<WinterShiroFilter> winterShiroAccessControlFilters = new ArrayList<>();

    /**
     * 添加自定义过滤器
     *
     * @param winterShiroFilter
     */
    public void addCustomShiroFilter(WinterShiroFilter winterShiroFilter) {
        this.winterShiroAccessControlFilters.add(winterShiroFilter);
    }
}
