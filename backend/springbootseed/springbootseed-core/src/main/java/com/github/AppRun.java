//package com.github;
//
//import com.github.modules.config.GlobalConfig;
//import com.github.modules.rsa.annotation.EnableSecurity;
//import com.github.utils.SpringContextHolder;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
//
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.env.Environment;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//
///**
// * @author oldhand
// * @date 2019-12-16
//*/
//@EnableAsync
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableEncryptableProperties
//@EnableTransactionManagement
//@EnableSecurity
//public class AppRun {
//    private static Logger logger = LoggerFactory.getLogger(AppRun.class);
//
//
//    public static void main(String[] args) throws UnknownHostException {
//        ConfigurableApplicationContext application = SpringApplication.run(AppRun.class, args);
//
//        Environment env = application.getEnvironment();
//        if (GlobalConfig.isDev()) {
//            logger.info("\n----------------------------------------------------------\n\t" +
//                            "Application is running! Access URLs:\n\t" +
//                            "Local: \t\thttp://localhost:{}\n\t" +
//                            "External: \thttp://{}:{}\n\t" +
//                            "Doc: \thttp://{}:{}/doc.html\n" +
//                            "----------------------------------------------------------",
//                    env.getProperty("server.port"),
//                    InetAddress.getLocalHost().getHostAddress(),
//                    env.getProperty("server.port"),
//                    InetAddress.getLocalHost().getHostAddress(),
//                    env.getProperty("server.port"));
//        }
//        else {
//            logger.info("\n----------------------------------------------------------\n\t" +
//                            "Application is running! Access URLs:\n\t" +
//                            "Local: \t\thttp://localhost:{}\n\t" +
//                            "External: \thttp://{}:{}\n\t" +
//                            "----------------------------------------------------------",
//                    env.getProperty("server.port"),
//                    InetAddress.getLocalHost().getHostAddress(),
//                    env.getProperty("server.port"));
//        }
//
//    }
//
//    @Bean
//    public SpringContextHolder springContextHolder() {
//        return new SpringContextHolder();
//    }
//}
