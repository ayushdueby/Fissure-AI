package com.fissure.Git.ai;

import com.fissure.Git.dto.SemanticDiffResult;
import com.fissure.Git.gitEnum.DiffType;
import com.fissure.Git.helper.JsonCleaner;
import com.fissure.Git.model.DiffLine;
import com.fissure.Git.service.AiService;
import com.fissure.Git.service.DiffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SemanticDiffService {
    @Autowired private DiffService diffService;
    @Autowired private JsonCleaner jsonCleaner;
    @Autowired private AiService aiService;

    public SemanticDiffResult semanticDiff(String sha1, String sha2, String filePath)
    {
        List<DiffLine> diffLineList=diffService.diffCommits(sha1,sha2,filePath);
        StringBuilder sb=new StringBuilder();
        for(DiffLine diffLine:diffLineList)
        {
            if(diffLine.getType()== DiffType.UNCHANGED)
                continue;
            sb.append(diffLine.getType()).append(": ").append(diffLine.getDiffContent()).append("\n");
        }
        String prompt="You are a code review expert. Analyze this diff and return JSON with:\n" +
                "1. \"summary\" - one line what changed\n" +
                "2. \"type\" - REFACTOR / BUGFIX / FEATURE / RENAME / CLEANUP\n" +
                "3. \"semanticGroups\" - group related changes together with explanation\n" +
                "4. \"suggestions\" - list of improvement suggestions\n" +
                "5. \"riskLevel\" - LOW / MEDIUM / HIGH with reason\n" +
                "\n" +
                "Diff:\n```diff\n" + sb + "\n```\n" +
                "Return only valid JSON, nothing else.";
        //feed this prompt to geminai API:
        String aiResult=aiService.generation(prompt);
        return parseResponse(aiResult);
    }
    public SemanticDiffResult parseResponse(String aiResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String clean = jsonCleaner.cleanJson(aiResponse);

            // Extract Gemini "text"
            if (clean.contains("\"text\":")) {
                int start = clean.indexOf("\"text\":") + 8;
                int end = clean.lastIndexOf("\"");
                clean = clean.substring(start, end);
            }

            return mapper.readValue(clean, SemanticDiffResult.class);

        } catch (Exception e) {
            e.printStackTrace();
            return fallbackResult(aiResponse);
        }
    }
    private SemanticDiffResult fallbackResult(String raw) {
        SemanticDiffResult res = new SemanticDiffResult();

        res.setSummary("Failed to parse AI response");
        res.setType("UNKNOWN");
        res.setSuggestions(List.of("Check raw response"));

        return res;
    }
}
