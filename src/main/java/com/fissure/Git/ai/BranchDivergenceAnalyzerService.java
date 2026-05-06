package com.fissure.Git.ai;

import com.fissure.Git.dag.CommitDAG;
import com.fissure.Git.diff.DiffEngine;
import com.fissure.Git.model.Commit;
import com.fissure.Git.model.DivergenceReport;
import com.fissure.Git.model.FileCollisionRisk;
import com.fissure.Git.model.Tree;
import com.fissure.Git.refs.RefManager;
import com.fissure.Git.store.ObjectStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Objects;

@Component
public class BranchDivergenceAnalyzerService {
    @Autowired private RefManager refManager;
    @Autowired private CommitDAG commitDAG;
    @Autowired private DiffEngine diffEngine;
    @Autowired private ObjectStore objectStore;
    public DivergenceReport analyzeDivergence(String branch1, String branch2){
        String sha1=refManager.getBranchSha(branch1);
        String sha2=refManager.getBranchSha(branch2);

        String lcaCommitSha=commitDAG.findLCA(sha1,sha2);
        Commit lcaCommit = lcaCommitSha == null ? null : (Commit)objectStore.getGitObject(lcaCommitSha);
        Tree lcaTree = (lcaCommit == null || lcaCommit.getTreeSha() == null)
                ? null
                : (Tree)objectStore.getGitObject(lcaCommit.getTreeSha());

        List<String>historySha1=commitDAG.getHistoryTillGivenCommit(sha1,lcaCommitSha);
        List<String>historySha2=commitDAG.getHistoryTillGivenCommit(sha2,lcaCommitSha);

        Map<String, List<String>>branch1TouchedFiles=new HashMap<>();
        Map<String, List<String>> branch2TouchedFiles=new HashMap<>();

        for(String commitSha:historySha1)
        {
            Commit commit=(Commit) objectStore.getGitObject(commitSha);
            Tree tree=(Tree)objectStore.getGitObject(commit.getTreeSha());
            for(String fileName:tree.getEntries().keySet())
            {
                String currentBlob = tree.getBlobSha(fileName);
                String lcaBlob = lcaTree == null ? null : lcaTree.getBlobSha(fileName);
                if(Objects.equals(currentBlob, lcaBlob))
                    continue;
                else
                {
                    branch1TouchedFiles
                            .computeIfAbsent(fileName, k -> new ArrayList<>())
                            .add(commitSha);
                }
            }
        }
        for(String commitSha:historySha2)
        {
            Commit commit=(Commit) objectStore.getGitObject(commitSha);
            Tree tree=(Tree)objectStore.getGitObject(commit.getTreeSha());
            for(String fileName:tree.getEntries().keySet())
            {
                String currentBlob = tree.getBlobSha(fileName);
                String lcaBlob = lcaTree == null ? null : lcaTree.getBlobSha(fileName);
                if(Objects.equals(currentBlob, lcaBlob))
                    continue;
                else
                {
                    branch2TouchedFiles
                            .computeIfAbsent(fileName, k -> new ArrayList<>())
                            .add(commitSha);
                }
            }
        }
        Set<String>intersectingFiles=new HashSet<>(branch1TouchedFiles.keySet());
        intersectingFiles.retainAll(branch2TouchedFiles.keySet());

        int commitsBehind=commitDAG.findLCASize(sha1,sha2);
        //lca=lcaCommit
        Map<String, FileCollisionRisk>filRisks=new HashMap<>();

        for(String fileName:intersectingFiles)
        {
            FileCollisionRisk fileCollisionRisk=new FileCollisionRisk();
            fileCollisionRisk.setFileName(fileName);

            fileCollisionRisk.setBranch1Commits(branch1TouchedFiles.get(fileName));
            fileCollisionRisk.setBranch2Commits(branch2TouchedFiles.get(fileName));

            fileCollisionRisk.setTouchCountBranch1(branch1TouchedFiles.get(fileName).size());
            fileCollisionRisk.setTouchCountBranch2(branch2TouchedFiles.get(fileName).size());

            filRisks.putIfAbsent(fileName,fileCollisionRisk);
        }
        return new DivergenceReport(
                lcaCommitSha,
                commitsBehind,
                filRisks
        );
    }
}
