import type { AiSemanticDiff } from "@/types/git";
import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/common/states";

export function SemanticCard({ data }: { data: AiSemanticDiff | null }) {
  if (!data) return <EmptyState label="No semantic analysis available." />;
  const semanticGroups = Array.isArray(data.semanticGroups) ? data.semanticGroups : [];
  const suggestions = Array.isArray(data.suggestions) ? data.suggestions : [];

  return (
    <Card className="p-5">
      <div className="mb-4 flex items-center justify-between">
        <h3 className="text-sm font-semibold text-slate-100">AI Semantic Analysis</h3>
        <span className="rounded-full border border-slate-700 px-2 py-1 text-xs text-slate-300">
          Risk: {data.riskLevel}
        </span>
      </div>
      <p className="mb-4 text-sm text-slate-300">{data.summary || "No summary available."}</p>
      <div className="grid gap-4 md:grid-cols-2">
        <div>
          <p className="mb-2 text-xs uppercase tracking-wide text-slate-500">Semantic Groups</p>
          <ul className="space-y-1 text-sm text-slate-300">
            {semanticGroups.map((group, index) => (
              <li key={`group-${index}`}>
                - {group.explanation ?? "Semantic grouping"}
              </li>
            ))}
            {!semanticGroups.length && <li>- No semantic groups returned.</li>}
          </ul>
        </div>
        <div>
          <p className="mb-2 text-xs uppercase tracking-wide text-slate-500">Suggestions</p>
          <ul className="space-y-1 text-sm text-slate-300">
            {suggestions.map((suggestion) => (
              <li key={suggestion}>- {suggestion}</li>
            ))}
            {!suggestions.length && <li>- No suggestions available.</li>}
          </ul>
        </div>
      </div>
      <p className="mt-4 text-xs text-slate-500">Change Type: {data.type || "unknown"}</p>
    </Card>
  );
}
