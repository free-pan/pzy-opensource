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
import org.pzy.opensource.domain.GlobalConstant;

import java.time.LocalDateTime;

/**
 * 填充创建人/编辑人信息
 *
 * @author pan
 * @date 2019-12-11
 */
@Slf4j
public class CreatorEditorMetaObjectHandler implements MetaObjectHandler {


    private static final String CREATOR_ID = "creatorId";
    private static final String CREATOR_NAME = "creatorName";
    private static final String CREATE_TIME = "createTime";
    private static final String EDITOR_ID = "editorId";
    private static final String EDITOR_NAME = "editorName";
    private static final String EDIT_TIME = "editTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("自动填充创建人相关信息...");
        Object obj = null;
        fitEditorInfo(metaObject, CREATOR_ID, CREATOR_NAME, CREATE_TIME);
        fitEditorInfo(metaObject, EDITOR_ID, EDITOR_NAME, EDIT_TIME);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("自动填充编辑人相关信息...");
        Object obj = null;
        fitEditorInfo(metaObject, EDITOR_ID, EDITOR_NAME, EDIT_TIME);
    }

    private void fitEditorInfo(MetaObject metaObject, String editorId, String editorName, String editTime) {
        Object obj;
        if (metaObject.hasSetter(editorId)) {
            obj = getFieldValByName(editorId, metaObject);
            if (null == obj) {
                this.setFieldValByName(editorId, CurrentUserInfo.getUserId().orElse(0L), metaObject);
            }
        }
        if (metaObject.hasSetter(editorName)) {
            obj = getFieldValByName(editorName, metaObject);
            if (null == obj) {
                this.setFieldValByName(editorName, CurrentUserInfo.getRealName().orElse(GlobalConstant.EMPTY_STRING), metaObject);
            }
        }
        if (metaObject.hasSetter(editTime)) {
            obj = getFieldValByName(editTime, metaObject);
            if (null == obj) {
                this.setFieldValByName(editTime, LocalDateTime.now(), metaObject);
            }
        }
    }
}
