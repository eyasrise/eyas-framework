## 架构2.0升级内容
####1.统一版本号
项目所需基本的maven版本统一维护到eyas-framework-dependencies中,
新项目架构升级的时候只需要修改eyas-framework-dependencies的版本号
####2.租户统一管理
增强mysql的脚本，默认再执行sql的时候加入TENANT_CODE = xxx，
开发不需要关心租户code
####3.jwt2.0升级
jwt解析token获取用户信息从单一的从redis获取，现在改成由各项目自己
实现获取方式，默认的使用redis，重写userProvide可以自己实现获取用户
信息功能
####4.2022-08-26增加mybatis_plus
架构增加mybatis_plus插件