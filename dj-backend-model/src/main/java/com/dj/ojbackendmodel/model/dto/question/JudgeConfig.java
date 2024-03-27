package com.dj.ojbackendmodel.model.dto.question;

import lombok.Data;

@Data
public class JudgeConfig {
    private Long stackLimit;
    private Long timeLimit;
    private Long memoryLimit;
}
