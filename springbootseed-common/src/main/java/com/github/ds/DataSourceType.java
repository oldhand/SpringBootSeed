 package com.github.ds;


 /**
  * 数据源
  *
  */
 public enum DataSourceType {
     /**
      * 主库
      */
     MASTER,

     /**
      * 从库(存放年度，月度等业务类型数据)
      */
     SLAVE
 }