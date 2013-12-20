package com.odong.portal.redis;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by flamen on 13-12-19下午10:43.
 */
@Aspect
public class RedisInterceptor {
    @Around("@annotation(com.odong.portal.redis.RedisPool)")
    public void around(ProceedingJoinPoint point) throws Throwable {

        logger.debug("调用开始");
        point.proceed();
        logger.debug("调用结束");
    }

    private final static Logger logger = LoggerFactory.getLogger(RedisInterceptor.class);
}
