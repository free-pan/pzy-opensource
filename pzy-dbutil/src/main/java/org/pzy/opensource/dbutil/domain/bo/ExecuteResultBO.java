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

package org.pzy.opensource.dbutil.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.pzy.opensource.dbutil.domain.enums.SqlTypeEnum;

import java.io.PrintStream;
import java.util.List;

/**
 * SQL语句执行结果
 *
 * @author 潘志勇
 * @date 2019-01-12
 */
@Setter
@Getter
@ToString
public class ExecuteResultBO {

    /**
     * 执行的sql语句
     */
    private String sql;
    /**
     * sql语句类型
     */
    private SqlTypeEnum sqlType;
    /**
     * 列名
     */
    private List<String> columnNameList;

    /**
     * 执行结果
     */
    private List<List<Object>> dataResultList;
    /**
     * 执行用时(单位:秒)
     */
    private Float useTime;
    /**
     * 影响记录数
     */
    private Integer effectRows;

    /**
     * 将格式化的结果,输出到指定打印流中. 如: System.out
     *
     * @param printStream 打印流
     */
    public void print(PrintStream printStream) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SQL语句:").append("\r\n");
        stringBuilder.append(this.sql).append("\r\n");
        stringBuilder.append("执行用时: ").append(useTime).append(" 秒.").append("\r\n");
        if (sqlType == SqlTypeEnum.SELECT) {
            if (null != dataResultList && !dataResultList.isEmpty()) {
                stringBuilder.append("|");
                for (String columnName : this.columnNameList) {
                    stringBuilder.append(" ").append(columnName).append("\t").append("|");
                }
                stringBuilder.append("\r\n");
                for (List<Object> row : dataResultList) {
                    stringBuilder.append("|");
                    int columnIndex = 0;
                    for (Object val : row) {
                        String columnName = this.getColumnNameList().get(columnIndex);
                        if (null != val) {
                            String valStr = val.toString();
                            int len = columnName.length() - valStr.length();
                            String blank = buildBlankStr(len);
                            stringBuilder.append(" ").append(valStr).append(blank);
                        } else {
                            String blank = buildBlankStr(columnName.length() - 4);
                            stringBuilder.append(" ").append("NULL").append(blank);
                            ;
                        }
                        stringBuilder.append("\t").append("|");
                        columnIndex++;
                    }
                    stringBuilder.append("\r\n");
                }
                stringBuilder.append("返回行数: ").append(dataResultList.size());
            } else {
                stringBuilder.append("返回行数: 0");
            }
        } else {
            stringBuilder.append("影响行数: ").append(this.effectRows);
        }
        printStream.println(stringBuilder.toString());
        printStream.flush();
    }

    private String buildBlankStr(int len) {
        StringBuilder stringBuilder = new StringBuilder();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 如果是count查询,可以通过该方法快速获取到count的结果
     *
     * @return count结果
     */
    public Long getCountResult() {
        return Long.parseLong(this.getDataResultList().get(0).get(0).toString());
    }
}
