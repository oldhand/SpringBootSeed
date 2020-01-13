package com.github.multids.aspect;

import java.lang.reflect.Method;

import com.github.multids.DynamicDataSourceContextHolder;
import com.github.multids.annotation.DataSource;
import com.github.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



/**
 * 多数据源切面处理
 *
 * @author layduo
 * @createTime 2019年11月5日 下午3:41:17
 */
@Aspect
@Order(1) // 优先级
@Component
public class DataSourceAspect {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 通过自定义注解@DataSource定义切点
     */
    @Pointcut("@annotation(com.github.multids.annotation.DataSource)" + "|| @within(com.github.multids.annotation.DataSource)")
    public void dsPointCut() {
    }

    /**
     * 切点环绕
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);
        if (StringUtils.isNotNull(dataSource)) {
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value().name());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<? extends Object> targetClass = point.getTarget().getClass();
        DataSource targetDataSource = targetClass.getAnnotation(DataSource.class);
        if (StringUtils.isNotNull(targetDataSource)) {
            return targetDataSource;
        } else {
            Method method = signature.getMethod();
            DataSource dataSource = method.getAnnotation(DataSource.class);
            return dataSource;
        }
    }
}