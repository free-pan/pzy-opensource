# pzy-opensource

## 作用

完成工作常用框架的整合, 通过简单的配置就能启用常用框架特性, 适用于项目的快速开发与迭代.

## 已整合的技术

springboot, shiro, spring-session, spring-data-redis, redisson, mybatis plus 

## 构建工具

maven

## 如何使用

* 先在机器上安装好`maven`
* 执行`mvn install`,将代码打包,安装到本地库 或 `mvn deploy`发布到自己的私库中
* 在实际项目中再引入即可

## 主要组件介绍

* `pzy-common`: 封装了一些常用的帮助类, 定义了自定义异常
* `pzy-current-user`: 对登录用户信息进行了封装
* `pzy-dbutil`: 可以方便的获取数据库表/字段信息
* `pzy-pdf`: 封装pdf常用操作
* `pzy-domain`: 封装全局常量, 分页参数, 响应数据结构
* `pzy-mybatis-plus-starter`: 整合`mybatis plus`
* `pzy-okhttp3`: 整合`okhttp3`
* `pzy-redis-starter`: 整合`spring-data-redis`和`redission`,对分布式锁的实现进行封装,对`spring cache`进行封装
* `pzy-security`:整合`shiro`权限框架
* `pzy-session-starter`: 整合`spring-session-data-redis`和`redisson`
* `pzy-springboot-web-starter`:整合`springboot`,提供以配置的方式启用跨域,静态目录映射,swagger,全局异常处理
* `pzy-springboot-web-security-starter`: 将`shiro`和`springboot`进行整合
* `pzy-swagger-ui`:提供一套全新的`swagger-ui`,使得rest api的查阅与测试更方便,快速.

## 组件使用示例

[pzy-opensource-demo](https://gitee.com/free_pan/pzy-opensource-demo)

## 开源协议

木兰宽松许可证 http://license.coscl.org.cn/MulanPSL

## 更多更便捷的项目开发骨架