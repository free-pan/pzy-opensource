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

package org.pzy.opensource.mybatisplus.objecthandler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.pzy.opensource.currentuser.CurrentUserInfo;
import org.pzy.opensource.mybatisplus.model.entity.BaseEntity;
import org.pzy.opensource.mybatisplus.model.entity.SimpleBaseEntity;

import java.util.Date;

/**
 * 填充创建人/编辑人信息
 *
 * @author pan
 * @date 2019-12-11
 */
@Slf4j
public class CreatorEditorMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("自动填充创建人相关信息...");
        this.setFieldValByName(BaseEntity.CREATOR_ID, CurrentUserInfo.getUserId(), metaObject);
        this.setFieldValByName(BaseEntity.CREATOR_NAME, CurrentUserInfo.getRealName(), metaObject);
        this.setFieldValByName(BaseEntity.CREATE_TIME, new Date(), metaObject);
        Object id = this.getFieldValByName(SimpleBaseEntity.ID, metaObject);
        if (null == id) {
            // TODO 自动填充id
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("自动填充编辑人相关信息...");
        this.setFieldValByName(BaseEntity.EDITOR_ID, CurrentUserInfo.getUserId(), metaObject);
        this.setFieldValByName(BaseEntity.EDITOR_NAME, CurrentUserInfo.getRealName(), metaObject);
        this.setFieldValByName(BaseEntity.EDIT_TIME, new Date(), metaObject);
    }
}
