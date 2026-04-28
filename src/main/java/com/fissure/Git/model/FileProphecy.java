package com.fissure.Git.model;

import lombok.Data;

import java.util.List;

@Data
public class FileProphecy {
    private String filename;
    private double conflictProbability;
    private String conflictType;
    private String reason;
    private List<String> conflictingRegions;
    private String suggestion;
    private boolean safeToMerge;

    public FileProphecy(String filename,
         double conflictProbability,
         String conflictType,
         String reason,
         List<String> conflictingRegions,
         String suggestion,
         boolean safeToMerge)
    {
        this.filename=filename;
        this.conflictProbability=conflictProbability;
        this.conflictType=conflictType;
        this.reason=reason;
        this.conflictingRegions=conflictingRegions;
        this.suggestion=suggestion;
        this.safeToMerge=safeToMerge;
    }
}
