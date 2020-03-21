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

package org.pzy.opensource.springboot.swagger.enums;

/**
 * @author tony
 */
public enum ApiStatusEnums {

    /**
     * 接口已定义(前端可按照接口定义进行初步开发)
     */
    DEFINE("[接口定义]-"),
    /**
     * 开发中(后端正在实现/完善业务逻辑)
     */
    DEV("[开发中]-"),
    /**
     * 可联调(前端可正式开始联调[确认功能是否能够走通,数据是否正确])
     */
    DEBUG("[可联调]-"),
    /**
     * 联调完待确认(前端联调完毕,待后端进行测试确认)
     */
    CONFIRM("[联调完待确认]-"),
    /**
     * 可测试(前后端双方已确认联调成功, 可交由测试人员, 进行详细测试)
     */
    TEST("[可测试]-"),
    /**
     * 完结(测试人员已完成测试,且已不存在BUG)
     */
    OVER("[完结]-");

    private String msg;

    private ApiStatusEnums(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
