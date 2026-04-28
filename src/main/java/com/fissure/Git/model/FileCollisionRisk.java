package com.fissure.Git.model;

import lombok.Data;

import java.util.List;

@Data
public class FileCollisionRisk {
    private String fileName;
    private int touchCountBranch1;
    private int touchCountBranch2;
    private List<String>branch1Commits;
    private List<String> branch2Commits;
}
