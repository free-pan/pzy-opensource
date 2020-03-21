# pzy-security

## 关于普通请求跨域以及ajax跨域请求的相关文章

[Apache Shiro框架在CORS跨域访问中遇到的问题及解决](https://www.jianshu.com/p/e56362315581)

[shiro拦截ajax返回json](http://heeexy.com/2017/10/22/build-springboot-shiro-vue/)

[解决跨域问题时遇到的坑](https://blog.csdn.net/madonghyu/article/details/80027387)

[SpringBoot2.0 + Shiro 跨域问题](https://blog.csdn.net/wangchsh2008/article/details/90324631)

## 本组件如何解决跨域问题

网上的回答五花八门, 对于这个问题的描述也千奇百怪, 那么到底要如何看待跨域问题, 遇到了跨域问题如何解决? 请往下看!

### 什么是跨域? session与cookie之间的关系?

请参考我的这边文章: [跨域相关概念归档](https://www.jianshu.com/p/d00c5c6cc702)

了解了基本概念之后(这些概念很重要,请认真阅读,理解并记住),请继续往下看.

### 项目中如何实际解决跨域问题?

前端: ajax组件或前端代理服务器要启用跨域支持

后端(重点): 
* springboot开启跨域支持(详见:`pzy-springboot-web-starter`模块的`org.pzy.opensource.springboot.factory.CorsConfigurationFactory`)
* shiro过滤器对所有`OPTIONS`请求放行,以及设置跨域支持响应头(即在跳转之前设置支持跨域的请求头,请求头设置详见:`pzy-spring-web`模块的`org.pzy.opensource.web.util.HttpResponseUtl#allowHttpCross`, 以及本人定义的任意一个支持跨域的过滤器.如:`org.pzy.opensource.security.shiro.filter.CrossFormAuthenticationFilter`)
* 为什么springboot设置了一次跨域过滤器,shiro中还要设置呢? 因为shiro的过滤器先于springboot的跨域过滤器执行,所以,在shiro过滤器进行redirect跳转时,springboot的过滤器尚未执行, 所以只有在shiro过滤器中再单独设置一次
* 关于`Access-Control-Allow-Origin`最好使用实际域名,不要使用`*`, 否则依然可能不起作用.(另外:springboot中使用`*`号貌似没问题,shiro中自己设置就不行,可能是springboot对其进行了处理吧)
 
因此本组件提供了如下支持跨域请求的过滤器:
* crossForceLogout: `org.pzy.opensource.security.shiro.filter.CrossForceLogoutFilter`
* crossAuthc: `org.pzy.opensource.security.shiro.filter.CrossFormAuthenticationFilter`
* crossKickout: `org.pzy.opensource.security.shiro.filter.CrossKickoutSessionControlFilter`
* crossUriAuthc: `org.pzy.opensource.security.shiro.filter.CrossUriPermissionFilter`
* crossUriUserAuthc:`org.pzy.opensource.security.shiro.filter.CrossUriUserPermissionFilter`
* crossUser: `org.pzy.opensource.security.shiro.filter.CrossUserFilter`