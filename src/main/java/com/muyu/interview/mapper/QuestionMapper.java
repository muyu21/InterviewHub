package com.muyu.interview.mapper;

import com.muyu.interview.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author 86150
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2024-12-18 23:05:44
* @Entity com.muyu.interview.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {
    /**
     * 查询题目列表（包括已被删除的数据）
     * @param minUpdateTime
     * @return
     */
    @Select("select * from question where updateTime >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);
}




