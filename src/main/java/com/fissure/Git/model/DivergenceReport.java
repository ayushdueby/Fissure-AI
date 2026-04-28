package com.fissure.Git.model;

import lombok.Data;

import java.util.Map;

@Data
public class DivergenceReport {
    private String lca;
    private int commitsBehind;
    private Map<String,FileCollisionRisk> fileRisks;
    public DivergenceReport(String lca,int commitsBehind,Map<String,FileCollisionRisk> fileRisks)
    {
        this.lca=lca;
        this.commitsBehind=commitsBehind;
        this.fileRisks=fileRisks;
    }
}
