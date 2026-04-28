package com.fissure.Git.model;

import lombok.Data;

@Data
public class Trajectory {
    private String afterCommit;
    private double probability;

    public Trajectory(String afterCommit,double probability)
    {
        this.afterCommit=afterCommit;
        this.probability=probability;
    }
}
