# pzy-springboot-web-security-starter

## 作用

将`springboot`与`shiro`进行整合,并结合`spring-session-data-reids`实现`session共享`

## 使用

### 如何使用自定义的密码加密算法

实现`org.apache.shiro.authc.credential.CredentialsMatcher`接口,并实现相关方法.然后设置到自定义的`Realm`中.

`org.pzy.opensource.security.shiro.realm.WinterRealmTemplate`默认使用的加密算法是`org.pzy.opensource.security.shiro.matcher.WinterCredentialsMatcher`

### 如何实现授权与认证

向spring注入自定义`org.pzy.opensource.security.shiro.realm.WinterRealmTemplate`的bean实例, 
并注入`org.pzy.opensource.security.service.ShiroWinterUserService`的自定义实现类即可

### 如何向shiro注入自定义过滤器

请将自定义shiro过滤器填充到`org.pzy.opensource.security.shiro.CustomShiroFilterBoxTemplate`的`winterShiroAccessControlFilters`中

*P.S.* 千万不要将自定义过滤器注入到spring容器中,这样会导致shiro的过滤器链执行出问题. 

问题说明详见:

[springboot集成Shiro，添加自定义filter后shiro的默认filter无法使用](https://blog.csdn.net/u010663758/article/details/78405339)
 
[（springboot）shiro安全框架自定义过滤器出现的几个疑难杂症解决方案](https://blog.csdn.net/qq_41737716/article/details/83187919) 

### 如何实现强制踢出

#### 两种情况

*单个登录账号超出最大session数量自动强制踢出*

* 启用`kickout`过滤器`org.pzy.opensource.security.shiro.filter.CrossKickoutSessionControlFilter`,并设置哪些url路径需要进行强制登出检查.
* 在登录成功之后, 按顺序执行如下操作
    1. 加分布式锁
    2. 判断当前账号的session数量是否超过最大限制`SpringSessionUtil.isOverMaxSessionCount`
    3. 超过则踢出当前账号的第一个session`SpringSessionUtil.forceLogoutFirst`
    4. 释放分布式锁
    5. 保存当前用户的`principalName`(使用用户id作为principalName最稳妥)`SpringSessionUtil.saveLoginUserPrincipalName`

*管理员手动强制踢出*

* 启用`forceLogout`过滤器`org.pzy.opensource.security.shiro.filter.CrossForceLogoutFilter`,并设置哪些url路径需要进行强制登出检查.
* 登录成功之后保存sessionid到数据库或其它持久化工具
* 此时管理员就可以通过保存的sessionid删除对应的session了