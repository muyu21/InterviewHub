package com.muyu.interview.esdao;

import com.muyu.interview.model.dto.question.QuestionEsDTO;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionEsDaoTest {
    @Resource
    QuestionEsDao questionEsDao;
    @Test
    void findByUserId() {
        questionEsDao.findByUserId(1L);
    }
}