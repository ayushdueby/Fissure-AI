import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/common/states";

export function LogTimeline({ commits }: { commits: string[] | null }) {
  if (!commits?.length) {
    return <EmptyState label="No commits available for timeline." />;
  }

  return (
    <Card className="p-5">
      <h2 className="mb-4 text-sm font-semibold text-slate-100">Git Log Timeline</h2>
      <div className="space-y-3">
        {commits.map((commit, index) => (
          <div key={`${commit}-${index}`} className="relative rounded-lg border border-slate-800/90 bg-slate-900/30 p-3 pl-6">
            <span className="absolute left-2 top-4 h-2 w-2 rounded-full bg-slate-400" />
            <p className="font-mono text-xs text-slate-200">{commit.slice(0, 12)}</p>
            <p className="mt-1 text-xs text-slate-400">{commit}</p>
          </div>
        ))}
      </div>
    </Card>
  );
}
