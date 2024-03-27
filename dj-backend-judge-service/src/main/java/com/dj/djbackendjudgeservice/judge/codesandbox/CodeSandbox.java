package com.dj.djbackendjudgeservice.judge.codesandbox;


import com.dj.ojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.dj.ojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
