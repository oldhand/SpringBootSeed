# SpringBootSeed

<h4 align="center">
  SpringBoot的种子框架，一个拿来即用，稳定，可靠的java框架。<br> 
  <br><br>
 Support By <a href="https://github.com/oldhand" target="_blank">oldhand</a>.
</h4>
<p align="center">    
    <b>如果对您有帮助，您可以点右上角 "Star" 支持一下 谢谢！</b>
</p>

</div>

#### 项目简介
springbootseed基于Spring Boot 2.1.0、Jpa、Spring Security、redis等的种子框架项目，  

#### 项目集成
| 集成包名 | 版本  | 功能  
| :---: | :---:  | :---: 
| JDK | 1.8 | - 
| SpringBoot | 2.1.0.RELEASE  | 核心框架
| SpringMVC(spring-boot-starter-web) | 2.1.0.RELEASE |   核心框架
| JPA | 2.1.0.RELEASE  | 持久层API
| Spring Security | 2.1.0.RELEASE  | 认证
| Mapper(mapper-spring-boot-starter) | 2.1.0  | - 
| Mybatis(mybatis-spring-boot-starter) | 1.3.2  | 持久层框架
| Druid(druid-spring-boot-starter) | 1.1.10  | 数据库连接池
| PageHelper(pagehelper-spring-boot-starter) | 1.2.4  | 分页控件
| Log4j2/Slf4j(spring-boot-starter-log4j2) | 2.0.2.RELEASE | 日志   
| Redis(spring-boot-starter-data-redis) | 2.0.2.RELEASE  | 缓存
| jedis | 2.9.0  | 缓存
| mybatis generator | 1.3.6  | 
| lombok | 1.16.20   | 注解样板代码
| mysql | runtime   | -
| postgresql | runtime   | -
| Swagger2(swagger-bootstrap-ui) | 2.9.2   | 文档自动化
| jasypt | 2.1.0   | 配置文件加密
 

#### 功能说明
- **文档自动化**：Swagger2文档分组，需要认证与无需认证的分组展示
- **系统授权管理**：appid+secret 组合授权
- **代码生成**：根据表结构自动生成代码
- **本地存储管理**：文件上传，查询等
- **redis缓存管理**：管理redis
- **日志管理**：详细了解接口工作情况  
- **定时任务管理**：任务调度  
- **访问记录管理**：客户端访问统计 
 
#### 项目结构
项目采用按功能分模块开发方式，项目的业务部分，放在子模块即可！
- springbootseed-common 公共模块
  - annotation 为系统自定义注解
  - aspect 自定义注解的切面
  - base 提供了Entity、DTO基类和mapstruct的通用mapper
  - config 自定义权限实现、redis配置、swagger配置
  - exception 项目统一异常的处理
  - utils 系统通用工具类
- springbootseed-core 系统核心模块（系统启动入口）
  - config 配置跨域与静态资源，与数据权限
    - thread 线程池相关
  - modules 系统相关模块(登录授权、系统监控、定时任务等)
- springbootseed-logging 系统日志模块
- springbootseed-tools 系统第三方工具模块
- springbootseed-generator 系统代码生成模块
 
 
 
#### 项目图片
  ![图片说明](https://github.com/oldhand/images/raw/master/springbootseed/springbootseed.jpg)


 
#### 反馈交流
 * 您有疑问，我们解答，您有建议，我们吸取，您有idea 我们欢迎！
 - 联系人：15111122026
 - 本项目由湖南网数科技有限公司提供支持
