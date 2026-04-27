package com.GitClear.Git.ai;

import com.GitClear.Git.dto.CommitIntentResult;
import com.GitClear.Git.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class IntentPredictorService {
    @Autowired private CommitContextBuilderService commitContextBuilderService;
    @Autowired private AiService aiService;
    @Autowired private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    public CommitIntentResult predictIntent(String commitSha)
    {
        String context= commitContextBuilderService.buildContext(List.of(commitSha));
        String prompt="You are an expert software engineer analyzing a Git commit.\n" +
                "Based on the diff, files changed, commit message, author history \n" +
                "and timestamp — determine the true intent of this commit.\n" +
                "\n" +
                "Return only valid JSON with these fields:\n" +
                "- \"intent\": one of BUGFIX / FEATURE / REFACTOR / PERFORMANCE / SECURITY / CLEANUP / HOTFIX\n" +
                "- \"confidence\": float between 0 and 1\n" +
                "- \"reasoning\": one sentence explaining why\n" +
                "- \"riskLevel\": LOW / MEDIUM / HIGH\n" +
                "- \"suggestedMessage\": a better commit message if current one is vague\n" +
                "- \"relatedFiles\": list of files that are logically connected to this change\n" +
                "\n" +
                "Context:\n" +
                "{%s}".formatted(context);
        String aiResponse =aiService.generation(prompt);
        aiResponse = aiResponse.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        try {
            return objectMapper.readValue(aiResponse, CommitIntentResult.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + aiResponse, e);
        }
    }
    public CommitIntentResult predictIntentBatch(List<String> commitSha)
    {
        String context= commitContextBuilderService.buildContext(commitSha);
        String prompt = """
            You are an expert software engineer analyzing MULTIPLE Git commits.

            For EACH commit, determine its intent.

            Return STRICT JSON ARRAY only (no markdown):

            [
              {
                "intent": "...",
                "confidence": 0.0,
                "reasoning": "...",
                "riskLevel": "...",
                "suggestedMessage": "...",
                "relatedFiles": ["..."]
              }
            ]

            Context:
            %s
            """.formatted(context);
        String aiResponse =aiService.generation(prompt);
        aiResponse = aiResponse.replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        try {
            return objectMapper.readValue(aiResponse, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, CommitIntentResult.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to batch parse AI response: " + aiResponse, e);
        }
    }
}
