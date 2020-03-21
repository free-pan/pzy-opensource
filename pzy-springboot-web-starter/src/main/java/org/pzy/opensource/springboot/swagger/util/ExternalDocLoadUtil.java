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

package org.pzy.opensource.springboot.swagger.util;

import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.spring.util.ResourceFileLoadUtil;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 用于判断是否需要加载外部文档以及加载外部文档
 *
 * @author 潘志勇
 * @date 2019-02-01
 */
public class ExternalDocLoadUtil {

    private ExternalDocLoadUtil() {
    }

    public static boolean needLoadExternalDoc(String text) {
        if (StringUtils.isEmpty(text)) {
            return false;
        } else {
            String tmp = text.trim();
            return tmp.matches("^\\{[ ]*classpath:.+\\}$");
        }
    }

    /**
     * 加载外部文档
     *
     * @param doc 外部文档路径, 格式: classpath:demo.txt或classpath:demo.md
     * @return
     */
    public static String loadExternalDoc(String doc) {
        String docContent = null;
        try {
            docContent = ResourceFileLoadUtil.loadAsString(doc.trim(), GlobalConstant.DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException("外部文档:" + doc + "读取异常!", e);
        }
        return docContent;
    }
}
