import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/common/states";
import type { QueryResult } from "@/types/git";

export function CodebaseChatCard({ data }: { data: QueryResult | null }) {
  if (!data) return <EmptyState label="Ask CodebaseMemory a question." />;

  return (
    <Card className="p-5">
      <h3 className="text-sm font-semibold text-slate-100">Codebase Memory Response</h3>
      <p className="mt-3 text-sm text-slate-300">{data.answer || "No answer returned."}</p>
      <div className="mt-4 grid gap-2 md:grid-cols-2 text-xs">
        <p className="text-slate-400">Intent: {data.intent || "unknown"}</p>
        <p className="text-slate-400">Confidence: {Math.round((data.confidence || 0) * 100)}%</p>
      </div>
      <div className="mt-4 grid gap-3 md:grid-cols-2">
        <div>
          <p className="mb-1 text-xs uppercase text-slate-500">Supporting Commits</p>
          {(data.supportingCommits || []).slice(0, 5).map((item) => (
            <p key={item} className="font-mono text-[11px] text-slate-400">{item.slice(0, 12)}</p>
          ))}
        </div>
        <div>
          <p className="mb-1 text-xs uppercase text-slate-500">Supporting Files</p>
          {(data.supportingFiles || []).slice(0, 5).map((item) => (
            <p key={item} className="text-[11px] text-slate-400">{item}</p>
          ))}
        </div>
      </div>
    </Card>
  );
}
