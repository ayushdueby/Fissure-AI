import { Card } from "@/components/ui/card";
import { EmptyState } from "@/components/common/states";
import type { ProphecyReport, TrajectoryResponse } from "@/types/git";

export function ProphecyCard({
  report,
  trajectory,
  quickScore,
}: {
  report: ProphecyReport | null;
  trajectory: TrajectoryResponse | null;
  quickScore: number | null;
}) {
  if (!report) return <EmptyState label="Run conflict prophecy to get risk score." />;
  const fileProphecies = Array.isArray(report.fileProphecies) ? report.fileProphecies : [];

  return (
    <Card className="p-5">
      <h3 className="text-sm font-semibold text-slate-100">Conflict Prophecy</h3>
      <div className="mt-3 grid gap-2 md:grid-cols-3 text-sm">
        <p className="text-slate-300">Risk Score: {(quickScore ?? report.overallConflictProbability ?? 0).toFixed(2)}</p>
        <p className="text-slate-300">Conflict Likely: {(quickScore ?? report.overallConflictProbability ?? 0) >= 0.5 ? "Yes" : "No"}</p>
        <p className="text-slate-300">Diverged Commits: {report.divergedCommits ?? 0}</p>
      </div>
      <p className="mt-3 text-xs text-slate-400">{report.recommendation || "No recommendation provided."}</p>

      <div className="mt-4 space-y-2">
        {fileProphecies.slice(0, 6).map((file) => (
          <div key={file.filename} className="rounded-md border border-slate-800 bg-slate-900/30 p-2">
            <p className="text-xs text-slate-200">{file.filename}</p>
            <p className="text-[11px] text-slate-400">
              probability {(file.conflictProbability ?? 0).toFixed(2)} | safeToMerge {String(file.safeToMerge)}
            </p>
          </div>
        ))}
      </div>

      {!!trajectory?.trajectories?.length && (
        <div className="mt-4 rounded-md border border-slate-800 bg-slate-900/20 p-2">
          <p className="mb-2 text-xs uppercase text-slate-500">Trajectory Trend: {trajectory.Trend || "unknown"}</p>
          {trajectory.trajectories.slice(0, 5).map((point) => (
            <p key={point.afterCommit} className="text-[11px] text-slate-400">
              {point.afterCommit.slice(0, 12)}: {point.probability.toFixed(2)}
            </p>
          ))}
        </div>
      )}
    </Card>
  );
}
