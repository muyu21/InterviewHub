package com.muyu.interview.aop;

import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.muyu.interview.annotation.HotKeyCache;
import com.muyu.interview.model.dto.questionbank.QuestionBankQueryRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HotKeyInterceptor {

    @Around("@annotation(hotKeyCache)")  // 拦截所有使用@HotKeyCacheable的注解方法
    public Object handleHotKeyCache(ProceedingJoinPoint joinPoint, HotKeyCache hotKeyCache) throws Throwable {
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        // 从方法参数中获取 `questionBankQueryRequest` 或者是传递的其他参数
        QuestionBankQueryRequest questionBankQueryRequest = null;
        for (Object arg : args) {
            if (arg instanceof QuestionBankQueryRequest) {
                questionBankQueryRequest = (QuestionBankQueryRequest) arg;
                break;
            }
        }

        if (questionBankQueryRequest == null) {
            throw new IllegalArgumentException("Missing required parameter: QuestionBankQueryRequest");
        }

        // 生成缓存 key，使用注解的prefix作为前缀（如果有的话），否则默认使用id
        Long id = questionBankQueryRequest.getId();
        String key = (hotKeyCache.prefix() + id).trim();

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
