package com.muyu.interview.model.dto.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionAIGenerateRequest implements Serializable {

    /**
     * 题目类型,比如Java
     */
    private String questionType;
    /**
     * 题目数量,比如10
     */
    private int number = 10;
    private static final long serialversionUID = 1L;
}
