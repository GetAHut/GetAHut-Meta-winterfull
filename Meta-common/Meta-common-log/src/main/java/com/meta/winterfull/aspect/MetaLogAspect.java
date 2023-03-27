package com.meta.winterfull.aspect;

import com.meta.winterfull.annotation.MetaLogging;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;

/**
 * @author : ytxu5
 * @date: 2023/3/27
 */
@Slf4j
@Aspect
@Component
public class MetaLogAspect {

    private final static ThreadLocal<Long> TIME_COST = new NamedThreadLocal<>("method cost");


    @Before(value = "@annotation(metaLogging)")
    public void before(JoinPoint joinPoint, MetaLogging metaLogging){
        TIME_COST.set(System.currentTimeMillis());
    }


    @AfterReturning(value = "@annotation(metaLogging)", returning = "jsonResult")
    public void afterReturning(JoinPoint joinPoint, MetaLogging metaLogging, Object jsonResult){
        logHandler(joinPoint, metaLogging, jsonResult, null);
    }

    @AfterThrowing(value = "@annotation(metaLogging)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, MetaLogging metaLogging, Exception e){
        logHandler(joinPoint, metaLogging, null, e);
    }

    private void logHandler(JoinPoint joinPoint, MetaLogging metaLogging, Object jsonResult, final Exception e){

        try {
            // TODO
            // 1. 日志异步处理
            // 2. 日志脱敏处理
        } catch (Exception ex){
            log.error("async log execute error : {}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            // thread local 需要手动释放引用
            TIME_COST.remove();
        }
    }
}
