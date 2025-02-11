package com.muyu.interview.constant;

/**
 * Redis 常量
 */
public interface RedisConstant {
    /**
     * 用户签到记录的Redis key 前缀
     */
    String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins";

    /**
     * 获取用户签到记录的Redis key
     * @param year 年份
     * @param userId 用户id
     * @return 拼接好的 Redis Key
     */
    static String getUserSignInRedisKeyPrefix(int year, long userId){
        return String.format("%s%s%s",USER_SIGN_IN_REDIS_KEY_PREFIX,year,userId);
    }
}
