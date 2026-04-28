package com.fissure.Git.dto;

import com.fissure.Git.model.Trajectory;
import lombok.Data;

import java.util.List;

@Data
public class TrajectoryResponse {
    private List<Trajectory> trajectories;
    private String Trend;
}
