package com.dj.djbackendjudgeservice.judge;

import com.dj.djbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.dj.djbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.dj.djbackendjudgeservice.judge.strategy.JudgeContext;
import com.dj.djbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.dj.ojbackendmodel.model.codesandbox.JudgeInfo;
import com.dj.ojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
