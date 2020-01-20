package com.github.multids.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.sql.DataSource;

import com.github.ds.DataSourceProperties;
import com.github.ds.DataSourceType;
import com.github.ds.MasterDataSource;
import com.github.ds.SlaveDataSource;
import com.github.multids.DynamicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;

/**
 * @ConditionalOnProperty控制Configuration是否生效
 * value 数组，获取对应property名称的值，与name不可同时使用
 * prefix property名称的前缀，可有可无
 * name 数组，property完整名称或部分名称（可与prefix组合使用，组成完整的property名称），与value不可同时使用
 * havingValue 可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
 * matchIfMissing 缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错
 * relaxedNames 是否可以松散匹配，至今不知道怎么使用的
 */
@Configuration
public class DruidConfig {
    @Autowired
    private DataSourceProperties druidproperties;

    @Autowired
    private MasterDataSource masterdatasource;

    @Autowired
    private SlaveDataSource slavedatasource;

    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource() {
        DataSource masterDataSource = druidproperties.dataSource(masterdatasource.make());
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
        if (slavedatasource.isEnabled()) {
            targetDataSources.put(DataSourceType.SLAVE.name(), druidproperties.dataSource(slavedatasource.make()));
        }
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","123456");
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * 去除监控页面底部广告
     * @param properties
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties) {
        //获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        //提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        final String filePath = "support/http/resources/js/common.js";
        //创建filter进行过滤
        Filter filter = new Filter() {

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                chain.doFilter(request, response);
                //重置缓冲区，响应头不会被重置
                response.resetBuffer();
                //获取common.js
                String text = Utils.readFromResource(filePath);
                //正则替换banner, 除去底部广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }

            @Override
            public void destroy() {

            }
        };

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}