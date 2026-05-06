import { Card } from "@/components/ui/card";
import type { CommitIntentResult } from "@/types/git";
import { EmptyState } from "@/components/common/states";

export function IntentCard({ data }: { data: CommitIntentResult | null }) {
  if (!data) return <EmptyState label="Run AI intent analysis to see predictions." />;

  return (
    <Card className="p-5">
      <h3 className="text-sm font-semibold text-slate-100">AI Commit Intent</h3>
      <div className="mt-3 grid gap-3 md:grid-cols-2">
        <p className="text-sm text-slate-300">Intent: {data.intent || "unknown"}</p>
        <p className="text-sm text-slate-300">Confidence: {Math.round((data.confidence || 0) * 100)}%</p>
        <p className="text-sm text-slate-300">Risk Level: {data.riskLevel || "unknown"}</p>
        <p className="text-sm text-slate-300">Suggested Message: {data.suggestedMessage || "N/A"}</p>
      </div>
      <p className="mt-3 text-xs text-slate-400">{data.reasoning || "No reasoning returned."}</p>
    </Card>
  );
}
