# SpringBootSeed
<h1 align="center">
  <br>
  <a href="https://github.com/oldhand/tezancloud"><img src="https://github.com/oldhand/images/raw/master/logo/tezan.jpg" width="200"></a>
  <br>
  SpringBootSeed
  <br>
</h1>

<h4 align="center">
  SpringBoot的种子框架项目，一个拿来即用的快速框架。<br> 
  <br><br>
</h4>
<p align="center">    
    <b>如果对您有帮助，您可以点右上角 "Star" 支持一下 谢谢！</b>
</p>

</div>

#### 项目简介
springbootseed基于Spring Boot 2.1.0、Jpa、Spring Security、redis、的种子框架项目，  

 
#### 项目结构
项目采用按功能分模块开发方式，将通用的配置放在公共模块，
```core```模块为系统核心模块也是项目入口模块，
```logging``` 模块为系统的日志模块，
```tools``` 为第三方工具模块，
```generator``` 为系统的代码生成模块

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
 
#### 反馈交流
 
