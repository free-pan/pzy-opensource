package org.pzy.opensource.mybatisplus.basemapper;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * WinterSqlInjector
 *
 * @author pan
 * @date 2020/4/6 10:20
 */
public class WinterSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> list = super.getMethodList(mapperClass);
        list.add(new WinterLogicDeleteMethod());
        list.add(new WinterSelectListMethod());
        list.add(new WinterSelectOneMethod());
        list.add(new WinterSelectPageMethod());
        list.add(new WinterSelectByIdMethod());
        list.add(new WinterSelectByIdsMethod());
        list.add(new WinterSelectCountMethod());
        list.add(new WinterSelectByMapMethod());
        list.add(new WinterUpdateByIdMethod());
        return list;
    }
}
