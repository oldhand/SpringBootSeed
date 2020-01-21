package com.github.modules.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;



/**
 * 配置
 *
 * @author Oldhand
 * @date 2019/12/12
 */
@Component
public class GlobalConfig {

  @Getter
  public static String active;

  @Value("${spring.profiles.active}")
  public void setActive(String value) {
    active = value;
  }


  /**
   * 是否是线上环境
   */
  public static boolean isDev() {
    if (active.equals("dev")) {
      return true;
    }
    return false;
  }

}
