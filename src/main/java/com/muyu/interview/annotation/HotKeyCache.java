package com.muyu.interview.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HotKeyCache {
    // 热 key 的前缀
    String prefix() default "";

    // 用于生成缓存键的字段名
    String field() default "id";  // 默认为空，表示使用id字段
}
