package org.pyz.opensource.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.pyz.opensource.demo.domain.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * UserInfoDao
 *
 * @author pan
 * @date 6/5/20
 */
@Repository
public interface UserInfoDao extends BaseMapper<UserInfo> {

}
