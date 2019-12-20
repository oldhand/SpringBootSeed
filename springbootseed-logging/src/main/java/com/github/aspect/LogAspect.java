package com.github.aspect;

import lombok.extern.slf4j.Slf4j;
import com.github.domain.Log;
import com.github.service.LogService;
import com.github.utils.RequestHolder;
import com.github.utils.SecurityUtils;
import com.github.utils.StringUtils;
import com.github.utils.ThrowableUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author oldhand
 * @date 2019-12-16
*/
@Component
@Aspect
@Slf4j
public class LogAspect {

    private final LogService logService;

    private long currentTime = 0L;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.github.aop.log.Log)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;

        currentTime = System.currentTimeMillis();
        result = joinPoint.proceed();
        long costtime = System.currentTimeMillis() - currentTime;
        Log plog = new Log("INFO",costtime);
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        String ip = StringUtils.getIp(request);
        String browser = StringUtils.getBrowser(request);
        logService.save(getUsername(), browser, ip,joinPoint, plog);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                params.append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        String address = StringUtils.getCityInfo(ip);
        if (result.getClass().toString().indexOf("ResponseEntity") >= 0) {
            ResponseEntity response = (ResponseEntity)result;
            log.info("[{}][{}][{}ms][statcode:{}]:{}{}",address,browser,costtime,response.getStatusCodeValue(),methodName,params.toString() + "}");
        }
        else {
            log.info("[{}][{}][{}ms]:{}{}",address,browser,costtime,methodName,params.toString() + "}");
        }

        return result;
    }

    /**
     * 配置异常通知
     *
     * @param joinPoint join point for advice
     * @param e exception
     */
    @AfterThrowing(pointcut = "logPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        Log log = new Log("ERROR",System.currentTimeMillis() - currentTime);
        log.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        logService.save(getUsername(), StringUtils.getBrowser(request), StringUtils.getIp(request), (ProceedingJoinPoint)joinPoint, log);
    }

    public String getUsername() {
        try {
            return SecurityUtils.getUsername();
        }catch (Exception e){
            return "";
        }
    }
}
