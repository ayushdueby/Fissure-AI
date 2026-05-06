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
        String prompt =
                """
                You are a code review AI.
                
                Return ONLY valid JSON.
                
                Do NOT explain anything.
                Do NOT use markdown.
                Do NOT wrap in ```json.
                
                Expected JSON format:
                
                {
                  "summary": "...",
                  "type": "BUGFIX",
                  "semanticGroups": [
                    {
                      "explanation": "...",
                      "lines": ["..."]
                    }
                  ],
                  "suggestions": ["..."],
                  "riskLevel": "LOW"
                }
                
                Diff:
                %s
                """.formatted(sb.toString());
        //feed this prompt to geminai API:
        String aiResult=aiService.generation(prompt);
        return parseResponse(aiResult);
    }
    public SemanticDiffResult parseResponse(String aiResponse) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            System.out.println("RAW AI RESPONSE:");
            System.out.println(aiResponse);

            String clean = jsonCleaner.cleanJson(aiResponse);

            clean = clean
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            int start = clean.indexOf("{");
            int end = clean.lastIndexOf("}");

            if (start != -1 && end != -1) {
                clean = clean.substring(start, end + 1);
            }

            if (clean.contains("\"error\"")) {
                return fallbackResult(clean);
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
