package com.muyu.interview.job.cycle;

import cn.hutool.core.collection.CollUtil;
import com.muyu.interview.esdao.PostEsDao;
import com.muyu.interview.esdao.QuestionEsDao;
import com.muyu.interview.mapper.PostMapper;
import com.muyu.interview.mapper.QuestionMapper;
import com.muyu.interview.model.dto.post.PostEsDTO;
import com.muyu.interview.model.dto.question.QuestionEsDTO;
import com.muyu.interview.model.entity.Post;
import com.muyu.interview.model.entity.Question;
import com.muyu.interview.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 增量同步帖子到 es
 *
 *   
 *  
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class IncSyncQuestionToEs {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionEsDao questionEsDao;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        List<Question> questionList = questionMapper.listQuestionWithDelete(fiveMinutesAgoDate);
        if (CollUtil.isEmpty(questionList)) {
            log.info("no inc post");
            return;
        }
        List<QuestionEsDTO> qustionEsDTOList = questionList.stream()
                .map(QuestionEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;
        int total = qustionEsDTOList.size();
        log.info("IncSyncPostToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            questionEsDao.saveAll(qustionEsDTOList.subList(i, end));
        }
        log.info("IncSyncPostToEs end, total {}", total);
    }
}
