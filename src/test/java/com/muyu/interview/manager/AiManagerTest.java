package com.muyu.interview.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;
    @Test
    void doChat() {
        String s = aiManager.doChat("你好");
        System.out.println(s);
    }

    @Test
    void testDoChat() {
        aiManager.doChat("你是一名Java后端程序员", "你好", "deepseek-v3-241226");
    }
}