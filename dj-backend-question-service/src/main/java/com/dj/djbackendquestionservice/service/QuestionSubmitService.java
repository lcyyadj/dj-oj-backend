package com.dj.djbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.dj.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.dj.ojbackendmodel.model.entity.QuestionSubmit;
import com.dj.ojbackendmodel.model.entity.User;
import com.dj.ojbackendmodel.model.vo.QuestionSubmitVO;

/**
* @author len'len
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2023-12-22 13:41:06
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);



    /**
     * 获取帖子封装
     *
     * @param questionSubmit
     * @param user
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User user);

    /**
     * 分页获取帖子封装
     *
     * @param questionSubmitPage
     * @param user
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User user);


}
