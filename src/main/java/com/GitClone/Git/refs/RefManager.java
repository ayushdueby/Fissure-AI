package com.GitClone.Git.refs;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RefManager {
    private String headBranch;
    private Map<String,String>latestCommitByBranch; // branch->latest Committed sha
    public RefManager()
    {
        this.latestCommitByBranch=new HashMap<>();
    }
    public String checkout(String checkOutBranch)
    {
        if(!latestCommitByBranch.containsKey(checkOutBranch))
        {
            throw new RuntimeException("Branch does not exists");
        }
        headBranch=checkOutBranch;
        return headBranch;
    }
    public void createBranch(String branchToAdd,String sha)
    {
        if(latestCommitByBranch.containsKey(branchToAdd))
        {
            throw new RuntimeException("Branch does already exists");
        }
        latestCommitByBranch.put(branchToAdd,sha);
    }
    public String getHeadSha()
    {
        return latestCommitByBranch.get(headBranch);
    }
    //uska latest sha return hoga and we will update that into our key branch's value
    public void updateCurrentBranchAfterCommit(String newSha)
    {
        latestCommitByBranch.put(headBranch,newSha);
    }
    //curr branch delete nai kar sakte
    public void deleteBranch(String branchName)
    {
        if (!latestCommitByBranch.containsKey(branchName)) {
            throw new RuntimeException("Branch does not exist");
        }
        if (branchName.equals(headBranch)) {
            throw new RuntimeException("Cannot delete current branch");
        }
        latestCommitByBranch.remove(branchName);
    }
    public List<String>listBranches()
    {
        return new ArrayList<>(latestCommitByBranch.keySet());
    }
    public String getBranchSha(String branchName)
    {
        return latestCommitByBranch.getOrDefault(branchName,null);
    }
}
