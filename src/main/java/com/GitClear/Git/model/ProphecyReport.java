package com.GitClear.Git.model;

import lombok.Data;

import java.util.List;

@Data
public class ProphecyReport {
    private String branch1;
    private String branch2;
    private String lcaSha;
    private int divergedCommits;
    private double overallConflictProbability;
    private List<FileProphecy> fileProphecies;
    private String recommendation;

    public ProphecyReport(
            String branch1,
            String branch2,
            String lcaSha,
            int divergedCommits,
            double overallConflictProbability,
            List<FileProphecy>fileProphecies,
            String recommendation)
    {
        this.branch1=branch1;
        this.branch2=branch2;
        this.lcaSha=lcaSha;
        this.divergedCommits=divergedCommits;
        this.overallConflictProbability=overallConflictProbability;
        this.fileProphecies=fileProphecies;
        this.recommendation=recommendation;
    }
}
