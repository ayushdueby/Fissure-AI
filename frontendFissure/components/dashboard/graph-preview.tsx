import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/common/states";
import type { GraphResponse } from "@/types/git";

export function GraphPreview({ graph }: { graph: GraphResponse | null }) {
  if (!graph?.nodes?.length) return <EmptyState label="Graph data unavailable." />;

  return (
    <Card className="p-5">
      <h2 className="mb-4 text-sm font-semibold text-slate-100">Commit Graph</h2>
      <div className="space-y-2">
        {graph.nodes.slice(0, 14).map((node) => (
          <div key={node.sha} className="grid grid-cols-[18px_1fr_auto] items-start gap-2 rounded-md border border-slate-800/80 bg-slate-900/30 p-2">
            <div className="mt-1 h-2.5 w-2.5 rounded-full bg-slate-400" />
            <div>
              <p className="text-xs text-slate-100">{node.message}</p>
              <p className="font-mono text-[11px] text-slate-500">{node.sha.slice(0, 10)} by {node.author}</p>
            </div>
            <p className="text-[10px] uppercase text-slate-500">{node.parents?.length ?? 0} parents</p>
          </div>
        ))}
      </div>
      <div className="mt-4 rounded-md border border-slate-800 bg-slate-900/20 p-2 text-xs text-slate-400">
        Branch heads: {Object.keys(graph.branches || {}).join(", ") || "none"}
      </div>
    </Card>
  );
}
