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
        this.setFieldValByName("creatorId", CurrentUserInfo.getUserId().orElse(0L), metaObject);
        this.setFieldValByName("creatorName", CurrentUserInfo.getRealName().orElse(GlobalConstant.EMPTY_STRING), metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.debug("自动填充编辑人相关信息...");
        this.setFieldValByName("editorId", CurrentUserInfo.getUserId().orElse(0L), metaObject);
        this.setFieldValByName("editorName", CurrentUserInfo.getRealName().orElse(GlobalConstant.EMPTY_STRING), metaObject);
        this.setFieldValByName("editTime", new Date(), metaObject);
    }
}
