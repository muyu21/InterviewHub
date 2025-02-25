package com.muyu.interview.aop;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.muyu.interview.annotation.HotKeyCache;
import com.muyu.interview.common.ErrorCode;
import com.muyu.interview.exception.BusinessException;
import com.muyu.interview.model.dto.questionbank.QuestionBankQueryRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class HotKeyInterceptor {

    @Around("@annotation(hotKeyCache)")  // 拦截所有使用@HotKeyCacheable的注解方法
    public Object handleHotKeyCache(ProceedingJoinPoint joinPoint, HotKeyCache hotKeyCache) throws Throwable {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        String key = null;

        // 获取指定字段名
        String fieldName = hotKeyCache.field();

        // 如果指定了字段名，则使用反射获取该字段
        for (Object arg : args) {
            Field field = arg.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(arg);
            if (value != null) {
                key = (hotKeyCache.prefix() + value).trim();  // 生成缓存键
                break;
            }
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }



        // 如果是热key，则尝试从缓存中获取
        if (JdHotKeyStore.isHotKey(key)) {
            Object cacheResult = JdHotKeyStore.get(key);
            if (cacheResult != null) {
                return cacheResult;  // 缓存命中，直接返回
            }
        }

        // 如果缓存没有命中，则执行目标方法
        Object result = joinPoint.proceed();

        // 方法执行完成后，将结果存入缓存
        if (result != null) {
            JdHotKeyStore.smartSet(key, result);  // 将结果存入缓存
        }

        return result;
    }
}
