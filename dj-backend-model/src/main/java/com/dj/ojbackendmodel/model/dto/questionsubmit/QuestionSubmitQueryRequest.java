package com.dj.ojbackendmodel.model.dto.questionsubmit;

import com.dj.ojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 创建请求
 *

 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {



    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目ID
     */
    private Long questionId;

    /**
     * 用户ID
     */
    private Long userId;
    private static final long serialVersionUID = 1L;
}