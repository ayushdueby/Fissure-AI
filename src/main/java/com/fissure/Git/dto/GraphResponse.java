package com.fissure.Git.dto;

import com.fissure.Git.model.GraphNode;

import java.util.List;
import java.util.Map;

public class GraphResponse {
    public List<GraphNode> nodes;
    public Map<String, String> branches;

    public GraphResponse(List<GraphNode> nodes, Map<String, String> branches) {
        this.nodes = nodes;
        this.branches = branches;
    }
}