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

package org.pzy.opensource.comm.util;

import org.pzy.opensource.domain.GlobalConstant;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * @author pan
 * @date 2019-12-14
 */
public class XmlUtil {

    private XmlUtil() {
    }

    /**
     * java对象转xml字符串
     *
     * @param obj
     * @return
     * @throws JAXBException
     */
    public static final String toXmlString(Object obj) throws JAXBException {
        //创建输出流
        StringWriter sw = new StringWriter();

        //利用jdk中自带的转换类实现
        JAXBContext context = JAXBContext.newInstance(obj.getClass());

        Marshaller marshaller = context.createMarshaller();
        //格式化xml输出的格式
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //去掉生成xml的默认报文头
        //marshaller.setProperty(Marshaller.JAXB_FRAGMENT,Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, GlobalConstant.DEFAULT_CHARSET);
        //将对象转换成输出流形式的xml
        marshaller.marshal(obj, sw);

        return sw.toString();
    }

    /**
     * xml字符串转java对象
     *
     * @param xmlStr
     * @param cls
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static final <T> T toJavaBean(String xmlStr, Class<T> cls) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(cls);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader sr = new StringReader(xmlStr);
        return (T) unmarshaller.unmarshal(sr);
    }
}
