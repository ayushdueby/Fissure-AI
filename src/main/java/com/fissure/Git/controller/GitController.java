package com.fissure.Git.controller;

import com.fissure.Git.ai.CodebaseMemoryService;
import com.fissure.Git.ai.ConflictProphetService;
import com.fissure.Git.ai.IntentPredictorService;
import com.fissure.Git.ai.SemanticDiffService;
import com.fissure.Git.dag.CommitDAG;
import com.fissure.Git.dto.*;
import com.fissure.Git.model.DiffLine;
import com.fissure.Git.model.ProphecyReport;
import com.fissure.Git.service.DiffService;
import com.fissure.Git.service.GitService;
import com.fissure.Git.service.MergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/git")
public class GitController {

    @Autowired public GitService gitService;
    @Autowired public DiffService diffService;
    @Autowired public MergeService mergeService;
    @Autowired public SemanticDiffService semanticDiffService;
    @Autowired public IntentPredictorService intentPredictorService;
    @Autowired public CommitDAG commitDAG;
    @Autowired public ConflictProphetService conflictProphetService;
    @Autowired public CodebaseMemoryService codebaseMemoryService;

    @PostMapping("/init")
    public ResponseEntity<ApiResponse> gitInit()
    {
        boolean initialized = gitService.gitInit();
        if (initialized) {
            return ResponseEntity.ok(ApiResponse.ok("Repository initialized", java.util.Map.of("initialized", true)));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Repository already initialized", "REPO_ALREADY_INITIALIZED"));
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> gitAdd(@RequestParam(required = false) String path, @RequestBody String body) throws DigestException, NoSuchAlgorithmException {
        requireParam("path", path);
        gitService.gitAdd(path,body);
        return ResponseEntity.ok(ApiResponse.ok("File added"));
    }
    @PostMapping("/branch")
    public ResponseEntity<ApiResponse> gitCreateBranch(@RequestParam(required = false) String name)
    {
        requireParam("name", name);
        gitService.gitCreateBranch(name);
        return ResponseEntity.ok(ApiResponse.ok("Branch created"));
    }
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse> gitCheckoutBranch(@RequestParam(required = false) String target)
    {
        requireParam("target", target);
        gitService.gitCheckout(target);
        return ResponseEntity.ok(ApiResponse.ok("Checkout completed"));
    }
    @PostMapping("/merge")
    public ResponseEntity<ApiResponse> gitMerge(@RequestParam(required = false) String branch) throws DigestException, NoSuchAlgorithmException {
        requireParam("branch", branch);
        mergeService.canMerge(branch,true);
        return ResponseEntity.ok(ApiResponse.ok("Merge completed"));
    }
    @PostMapping("/commit")
    public ResponseEntity<String> gitCommit(
            @RequestBody CommitRequest request
    ) throws Exception {
        return ResponseEntity.ok(
                gitService.gitCommit(request.getMessage(), request.getAuthor())
        );
    }


    @GetMapping("/log")
    public List<String> gitLog()
    {
        return gitService.gitLog();
    }
    @GetMapping("/branch")
    public List<String>gitListBranch()
    {
        return gitService.gitListBranch();
    }
    @GetMapping("/diff")
    public List<DiffLine> getDiff(@RequestParam(required = false) String sha1,@RequestParam(required = false) String sha2,@RequestParam(required = false) String file)
    {
        requireParam("sha1", sha1);
        requireParam("sha2", sha2);
        requireParam("file", file);
        return diffService.diffCommits(sha1,sha2,file);
    }
    @GetMapping("/diff/working")
    public List<DiffLine> getDiffHeadVsCurr(@RequestParam(required = false) String file,@RequestParam(required = false) String newContent)
    {
        requireParam("file", file);
        requireParam("newContent", newContent);
        return diffService.diffWorkingVsHead(file,newContent);
    }
    @GetMapping("/merge/preview")
    public ResponseEntity<ApiResponse> gitCheckMergeConflicts(@RequestParam(required = false) String branch) throws DigestException, NoSuchAlgorithmException {
        requireParam("branch", branch);
        boolean canMerge = mergeService.canMerge(branch, false);
        if(!canMerge) {
            return ResponseEntity.ok(ApiResponse.ok("Conflicts detected", java.util.Map.of(
                    "cleanMerge", false,
                    "detail", mergeService.mergeConflictStore.toString()
            )));
        }
        return ResponseEntity.ok(ApiResponse.ok("No conflicts", java.util.Map.of("cleanMerge", true)));
    }

    @DeleteMapping("/branch")
    public ResponseEntity<?> deleteBranch(@RequestParam String name) {
        return ResponseEntity.ok(gitService.deleteBranch(name));
    }
    @GetMapping("/log/graph")
    public ResponseEntity<?> getGraph() {
        return ResponseEntity.ok(gitService.getGraphLog());
    }
    @GetMapping("/ai/diff")
    public ResponseEntity<SemanticDiffResult> getAiDiffSuggestions(
            @RequestParam(required = false) String sha1,
            @RequestParam(required = false) String sha2,
            @RequestParam(value = "file", required = false) String file
    ) {
        requireParam("sha1", sha1);
        requireParam("sha2", sha2);
        requireParam("file", file);
        return ResponseEntity.ok(
                semanticDiffService.semanticDiff(sha1, sha2, file)
        );
    }
    @GetMapping("/ai/intent")
    public ResponseEntity<CommitIntentResult> getAiCommitIntent(
            @RequestParam(required = false) String sha1
    ) {
        requireParam("sha1", sha1);
        return ResponseEntity.ok(
                intentPredictorService.predictIntent(sha1)
        );
    }
    @GetMapping("/ai/intent/batch")
    public ResponseEntity<CommitIntentResult> getAiCommitIntentBatch(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer limit
    ) {
        requireParam("branch", branch);
        if (limit == null || limit <= 0) {
            throw new IllegalArgumentException("Query parameter 'limit' must be a positive integer");
        }
        List<String>getLastTencommit=commitDAG.getHistory(gitService.getBranchsha(branch)).stream()
                .limit(limit)
                .toList();
        return ResponseEntity.ok(
                intentPredictorService.predictIntentBatch(getLastTencommit)
        );
    }
    @GetMapping("/ai/prophet")
    public ResponseEntity<ProphecyReport> getAiProphet(
            @RequestParam(required = false) String branch1,
            @RequestParam(required = false) String branch2
    ) {
        requireParam("branch1", branch1);
        requireParam("branch2", branch2);
        return ResponseEntity.ok(
                conflictProphetService.prophesy(branch1,branch2)
        );
    }
    @GetMapping("/ai/prophet/trajectory")
    public ResponseEntity<TrajectoryResponse> getAiProphetTrajectory(
            @RequestParam(required = false) String branch1,
            @RequestParam(required = false) String branch2
    ) {
        requireParam("branch1", branch1);
        requireParam("branch2", branch2);
        return ResponseEntity.ok(
                conflictProphetService.getCollisionTrajectory(branch1,branch2)
        );
    }
    @GetMapping("/ai/prophet/quick")
    public double getAiProphetTrajectory2(
            @RequestParam(required = false) String branch1,
            @RequestParam(required = false) String branch2
    ) {
        requireParam("branch1", branch1);
        requireParam("branch2", branch2);
        return conflictProphetService.prophesy(branch1,branch2).getOverallConflictProbability();
    }
    @PostMapping("/ai/query")
    public ResponseEntity<QueryResult> query(
            @RequestParam(required = false) String branch,
            @RequestBody String question
    ) {
        requireParam("branch", branch);
        return ResponseEntity.ok(
                codebaseMemoryService.query(question, branch)
        );
    }

    private void requireParam(String name, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required query parameter: " + name);
        }
    }

}
