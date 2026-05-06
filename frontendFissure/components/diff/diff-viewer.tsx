import type { DiffLine } from "@/types/git";
import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/common/states";

export function DiffViewer({ lines, title }: { lines: DiffLine[] | null; title: string }) {
  if (!lines?.length) return <EmptyState label="No diff lines available." />;

  return (
    <Card className="overflow-hidden">
      <div className="border-b border-slate-800 px-4 py-3 text-sm text-slate-300">{title}</div>
      <div className="max-h-80 overflow-auto font-mono text-xs">
        {lines.map((line, index) => (
          <div
            key={`${line.type}-${index}`}
            className={`px-4 py-1 ${
              line.type === "ADDED"
                ? "bg-emerald-950/30 text-emerald-300"
                : line.type === "DELETED"
                  ? "bg-rose-950/30 text-rose-300"
                  : "text-slate-400"
            }`}
          >
            {line.diffContent}
          </div>
        ))}
      </div>
    </Card>
  );
}
