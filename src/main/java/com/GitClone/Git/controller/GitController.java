package com.GitClone.Git.controller;

import com.GitClone.Git.model.DiffLine;
import com.GitClone.Git.service.DiffService;
import com.GitClone.Git.service.GitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/git")
public class GitController {

    @Autowired public GitService gitService;
    @Autowired public DiffService diffService;

    @PostMapping("/init")
    public void gitInit()
    {
        gitService.gitInit();
    }
    @PostMapping("/add")
    public void gitAdd(@RequestParam String path, @RequestBody String body) throws DigestException, NoSuchAlgorithmException {
        gitService.gitAdd(path,body);
    }
    @PostMapping("/branch")
    public void gitCreateBranch(@RequestParam String name)
    {
        gitService.gitCreateBranch(name);
    }
    @PostMapping("/checkout")
    public void gitCheckoutBranch(@RequestParam String target)
    {
        gitService.gitCheckout(target);
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
    public List<DiffLine> getDiff(@RequestParam String sha1,@RequestParam String sha2,@RequestParam String fileName)
    {
        return diffService.diffCommits(sha1,sha2,fileName);
    }
    @GetMapping("/diff/working")
    public List<DiffLine> getDiff(@RequestParam String file,@RequestParam String newContent)
    {
        return diffService.diffWorkingVsHead(file,newContent);
    }
}
