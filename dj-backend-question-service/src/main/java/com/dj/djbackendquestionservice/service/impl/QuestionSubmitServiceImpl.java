package com.dj.djbackendquestionservice.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.djbackendquestionservice.mapper.QuestionSubmitMapper;
import com.dj.djbackendquestionservice.rabbitmq.MyMessageProducer;
import com.dj.djbackendquestionservice.service.QuestionService;
import com.dj.djbackendquestionservice.service.QuestionSubmitService;
import com.dj.djbackendserviceclient.service.JudgeFeignClient;
import com.dj.djbackendserviceclient.service.UserFeignClient;
import com.dj.ojbackendcommon.common.ErrorCode;
import com.dj.ojbackendcommon.constant.CommonConstant;
import com.dj.ojbackendcommon.exception.BusinessException;
import com.dj.ojbackendcommon.utils.SqlUtils;
import com.dj.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.dj.ojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.dj.ojbackendmodel.model.entity.Question;
import com.dj.ojbackendmodel.model.entity.QuestionSubmit;
import com.dj.ojbackendmodel.model.entity.User;
import com.dj.ojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.dj.ojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.dj.ojbackendmodel.model.vo.QuestionSubmitVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author len'len
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2023-12-22 13:41:06
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    @Resource
    private UserFeignClient userService;
    @Resource
    @Lazy
    private JudgeFeignClient judgeService;
    @Resource
    private MyMessageProducer myMessageProducer;
    /**
     * 提交题目(单表更新)
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(enumByValue==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"校验编程语言非法");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目创建失败");
        }
        Long questionSubmitId = questionSubmit.getId();
        // 发送消息
        myMessageProducer.sendMessage("code_exchange", "my_routingKey", String.valueOf(questionSubmitId));
        // 执行判题服务
//        CompletableFuture.runAsync(() -> {
//            judgeFeignClient.doJudge(questionSubmitId);
//        });
        return questionId;
    }
    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.like(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "status", status);
        queryWrapper.like(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User user) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 1. 关联查询用户信息
        Long userId = questionSubmit.getUserId();
        if(user.getId()!=userId&&!userService.isAdmin(user)){
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User user) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage1 = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage1;
        }
        List<QuestionSubmitVO> collect = questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit, user)).collect(Collectors.toList());
        questionSubmitVOPage1.setRecords(collect);
        return questionSubmitVOPage1;
    }

}




