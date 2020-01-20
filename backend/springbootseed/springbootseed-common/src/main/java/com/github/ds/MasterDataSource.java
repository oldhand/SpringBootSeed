 package com.github.ds;

 import com.alibaba.druid.pool.DruidDataSource;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.context.annotation.Configuration;


 /**
  * 获得主数据源
  *
  */

 @Configuration
 public class MasterDataSource {

     @Value("${spring.datasource.druid.driver-class-name}")
     private String driverClassName;

     @Value("${spring.datasource.druid.master.url}")
     private String url;

     @Value("${spring.datasource.druid.master.username}")
     private String username;

     @Value("${spring.datasource.druid.master.password}")
     private String password;

     public DruidDataSource make() {
         DruidDataSource result = new DruidDataSource();
         result.setDriverClassName(driverClassName);
         result.setUrl(url);
         result.setUsername(username);
         result.setPassword(password);
         return result;
     }
 }