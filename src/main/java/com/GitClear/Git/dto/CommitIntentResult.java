package com.GitClear.Git.dto;

import lombok.Data;

import java.util.List;

@Data
public class CommitIntentResult {
    private String intent;
    private double confidence;
    private String reasoning;
    private String riskLevel;
    private String suggestedMessage;
    private List<String> relatedFiles;
}
