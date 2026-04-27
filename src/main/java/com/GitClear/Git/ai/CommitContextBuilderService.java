package com.GitClear.Git.ai;

import com.GitClear.Git.dag.CommitDAG;
import com.GitClear.Git.gitEnum.DiffType;
import com.GitClear.Git.model.Commit;
import com.GitClear.Git.model.DiffLine;
import com.GitClear.Git.model.Tree;
import com.GitClear.Git.service.DiffService;
import com.GitClear.Git.store.ObjectStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Slf4j
public class CommitContextBuilderService {
    @Autowired ObjectStore objectStore;
    @Autowired DiffService diffService;
    @Autowired CommitDAG commitDAG;
    public String buildContext(List<String> commits)
    {
        StringBuilder context = new StringBuilder();
        for(String commitSha:commits)
        {
            Commit commit=(Commit) objectStore.getGitObject(commitSha);
            List<String> parentShaList=commit.getParentCommitSha();


            //commit ki tree->
            Tree tree=(Tree) objectStore.getGitObject(commit.getTreeSha());
            List<String>allFiles=new ArrayList<>(tree.getEntries().keySet());
            Map<String,List<DiffLine>>fileToDiffMap=new HashMap<>();

            //get parents commit
            //parentShaList.addAll(lastThreeCommits);

            for(String parent:parentShaList)
            {
                //parent ki tree->
                Commit parentCommit=(Commit)objectStore.getGitObject(parent);
                Tree parentTree=(Tree) objectStore.getGitObject(parentCommit.getTreeSha());

                for(String file:allFiles)
                {
                    //if file content is same then continue;
                    if(parentTree.getBlobSha(file).equals(tree.getBlobSha(file)))
                        continue;
                    List<DiffLine>diffs=diffService.diffCommits(parent,commitSha,file);
                    List<DiffLine> filtered = diffs.stream()
                            .filter(diffLine -> diffLine.getType() != DiffType.UNCHANGED)
                            .toList();
                    fileToDiffMap
                            .computeIfAbsent(file, k -> new ArrayList<>())
                            .addAll(filtered);
                }
            }

            //get last 3 commit from the same author for the context
            List<String>commitHistory=new ArrayList<>(commitDAG.getHistory(commitSha));
            List<String>lastThreeCommits= commitHistory.stream()
                    .filter(str->((Commit) objectStore.getGitObject(str)).getAuthor().equals(commit.getAuthor()))
                    .limit(3)
                    .toList();


            //Metadata
            context.append("\n====================\n");
            context.append("Commit SHA: ").append(commitSha).append("\n");
            context.append("Commit Message: ").append(commit.getMessage()).append("\n");
            context.append("Author: ").append(commit.getAuthor()).append("\n");
            context.append("Timestamp: ").append(commit.getTimestamp()).append("\n\n");

            //context Section
            context.append("Last 3 commits by same author:\n");
            for (String sha : lastThreeCommits) {
                Commit c = (Commit) objectStore.getGitObject(sha);
                context.append("- ").append(c.getMessage()).append("\n");
            }
            //diff Section
            context.append("\nChanged Files and Diffs:\n");
            for (Map.Entry<String, List<DiffLine>> entry : fileToDiffMap.entrySet()) {
                context.append("\nFile: ").append(entry.getKey()).append("\n");
                for (DiffLine diff : entry.getValue()) {
                    context.append(diff.getType())
                            .append(": ")
                            .append(diff.getDiffContent())
                            .append("\n");
                }
            }
        }
        // Limit size
        if (context.length() > 2000) {
            context.setLength(2000);
        }
        return context.toString();
    }
}

