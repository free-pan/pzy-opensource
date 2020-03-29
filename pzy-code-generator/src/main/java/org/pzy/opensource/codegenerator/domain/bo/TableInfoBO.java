package org.pzy.opensource.codegenerator.domain.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pan
 * @date 2020/3/29
 */
@Data
@AllArgsConstructor
public class TableInfoBO implements Serializable {
    /**
     * 要生成代码的表
     */
    private String[] tableArr;
    /**
     * 需要去除的表前缀[可以为null]
     */
    private String tablePrefix;
    /**
     * 需要去除的表字段前缀[可以为null]
     */
    private String columnPrefix;
    /**
     * id类型
     */
    private IdType idType;

    public TableInfoBO(String[] tableArr) {
        this.tableArr = tableArr;
    }

    public TableInfoBO(String[] tableArr, String tablePrefix, String columnPrefix) {
        this.tableArr = tableArr;
        this.tablePrefix = tablePrefix;
        this.columnPrefix = columnPrefix;
    }
}
