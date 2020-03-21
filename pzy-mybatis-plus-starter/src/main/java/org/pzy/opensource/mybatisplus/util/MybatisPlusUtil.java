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

package org.pzy.opensource.mybatisplus.util;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.reflection.ExceptionUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author pan
 * @date 2019-12-21
 */
public class MybatisPlusUtil {

    private static final String STR = "%";

    private MybatisPlusUtil() {
    }

    /**
     * 计算hash表的实际大小
     *
     * @param elementCount hash表中实际要存放的元素
     * @param loadFactor   加载因子[可不指定].默认:0.75
     * @return
     */
    public static int computeHashSize(int elementCount, Float loadFactor) {
        if (elementCount == 0) {
            return 0;
        }
        if (null == loadFactor) {
            loadFactor = 0.75F;
        }
        return (int) Math.ceil(elementCount * loadFactor);
    }

    /**
     * 执行批量操作
     *
     * @param fun fun
     * @since 3.3.0
     */
    public static void executeBatch(Consumer<SqlSession> fun, Class<?> entityClass) {
        SqlHelper.clearCache(entityClass);
        SqlSessionFactory sqlSessionFactory = SqlHelper.sqlSessionFactory(entityClass);
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            fun.accept(sqlSession);
            sqlSession.commit();
        } catch (Throwable t) {
            sqlSession.rollback();
            Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
            if (unwrapped instanceof RuntimeException) {
                MyBatisExceptionTranslator myBatisExceptionTranslator
                        = new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true);
                throw Objects.requireNonNull(myBatisExceptionTranslator.translateExceptionIfPossible((RuntimeException) unwrapped));
            }
            throw ExceptionUtils.mpe(unwrapped);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    public static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    public static <Entity> Consumer<SqlSession> buildConsumer(String sqlStatement, List<Entity> entityList, int batchSize) {
        int size = entityList.size();
        return sqlSession -> {
            int i = 1;
            for (Entity entity : entityList) {
                sqlSession.insert(sqlStatement, entity);
                if ((i % batchSize == 0) || i == size) {
                    sqlSession.flushStatements();
                }
                i++;
            }
        };
    }

    /**
     * 获取 SqlStatement
     *
     * @param entityClass 实体类的类对象
     * @param sqlMethod   ignore
     * @return ignore
     */
    public static <Entity> String sqlStatement(Class<Entity> entityClass, SqlMethod sqlMethod) {
        return SqlHelper.table(entityClass).getSqlStatement(sqlMethod.getMethod());
    }
}
