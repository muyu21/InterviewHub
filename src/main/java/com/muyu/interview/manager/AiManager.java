package com.muyu.interview.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.muyu.interview.common.ErrorCode;
import com.muyu.interview.exception.BusinessException;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class AiManager {
    @Resource
    private ArkService aiService;

    private final String DEFAULT_MODEL = "deepseek-v3-241226";
    public String doChat(String userPrompt) {
        return doChat("",userPrompt,DEFAULT_MODEL);
    }
    public String doChat(String systemPrompt,String userPrompt) {
        return doChat(systemPrompt,userPrompt,DEFAULT_MODEL);
    }
    public String doChat(String systemPrompt,String userPrompt,String model) {
        // 构造消息列表
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();
        messages.add(systemMessage);
        messages.add(userMessage);
        // 构造请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                .model(model)
                .messages(messages)
                .build();
        // 调用接口发送请求
        List<ChatCompletionChoice> choices = aiService.createChatCompletion(chatCompletionRequest).getChoices();
        if (CollUtil.isNotEmpty(choices)) {
            return (String) choices.get(0).getMessage().getContent();
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR,"AI 调用失败，没有返回结果");
//        aiService.shutdownExecutor();
    }
}
