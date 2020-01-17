package com.github.rabbitmq.consumer;

import com.github.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 执行定时任务
 * @author oldhand
 */
@Slf4j
public class ConsumerRunnable implements Callable {

	private Object target;
	private Method method;
	private String params;

	ConsumerRunnable(String targetClassname, String methodName, String params) throws NoSuchMethodException, SecurityException,ClassNotFoundException {

		Class targetClass = Class.forName(targetClassname);
		// 使用spring content 获取类的实例
		ApplicationContext context = SpringContextHolder.getContext();
		this.target = context.getBean(targetClass);
		/*
             给实例化的类注入需要的bean (@Autowired)
             如果不注入，被@Autowired注解的变量会报空指针
        */
		context.getAutowireCapableBeanFactory().autowireBean(this.target);

		this.params = params;

		if (StringUtils.isNotBlank(params)) {
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
		} else {
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	public Object call() throws Exception {
		ReflectionUtils.makeAccessible(method);
		// 设置访问权限
		method.setAccessible(true);
		Object result;
		try {
			if (StringUtils.isNotBlank(params)) {
				result = method.invoke(target, params);
			} else {
				result = method.invoke(target);
			}
		}
		catch(InvocationTargetException e) {
			throw new Exception(e.getCause().getMessage());
		}
		catch(Exception e) {
			throw e;
		}
		return result;
	}
}
