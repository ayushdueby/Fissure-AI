import { request } from "@/services/api-client";
import type {
  AiSemanticDiff,
  ApiEnvelope,
  Branch,
  Commit,
  CommitIntentResult,
  DiffLine,
  GraphResponse,
  MergePreview,
  ProphecyReport,
  QueryResult,
  TrajectoryResponse,
} from "@/types/git";

function unwrap<T>(value: T | ApiEnvelope<T>): T {
  if (value && typeof value === "object" && "data" in (value as ApiEnvelope<T>)) {
    return ((value as ApiEnvelope<T>).data ?? value) as T;
  }
  return value as T;
}

export const gitService = {
  initRepository: async () =>
    unwrap(await request<{ initialized: boolean } | ApiEnvelope<{ initialized: boolean }>>({ url: "/init", method: "POST" })),
  addFile: (path: string, content: string) =>
    request({ url: "/add", method: "POST", params: { path }, data: content }),
  commitChanges: (message: string, author: string) =>
    request({ url: "/commit", method: "POST", data: { message, author } }),
  createBranch: (name: string) => request({ url: "/branch", method: "POST", params: { name } }),
  listBranches: async () => unwrap(await request<Branch[] | ApiEnvelope<Branch[]>>({ url: "/branch", method: "GET" })),
  deleteBranch: (name: string) =>
    request({ url: "/branch", method: "DELETE", params: { name } }),
  checkoutBranch: (target: string) => request({ url: "/checkout", method: "POST", params: { target } }),
  getLog: async () => unwrap(await request<Commit[] | ApiEnvelope<Commit[]>>({ url: "/log", method: "GET" })),
  getCommitGraph: async () =>
    unwrap(await request<GraphResponse | ApiEnvelope<GraphResponse>>({ url: "/log/graph", method: "GET" })),
  getDiff: async (sha1: string, sha2: string, file: string) =>
    unwrap(await request<DiffLine[] | ApiEnvelope<DiffLine[]>>({ url: "/diff", method: "GET", params: { sha1, sha2, file } })),
  getWorkingDiff: async (file: string, newContent: string) =>
    unwrap(
      await request<DiffLine[] | ApiEnvelope<DiffLine[]>>({
        url: "/diff/working",
        method: "GET",
        params: { file, newContent },
      }),
    ),
  getMergePreview: async (branch: string) =>
    unwrap(await request<MergePreview | ApiEnvelope<MergePreview>>({ url: "/merge/preview", method: "GET", params: { branch } })),
  mergeBranch: (branch: string) =>
    request({ url: "/merge", method: "POST", params: { branch } }),
  getAiSemanticDiff: async (sha1: string, sha2: string, file: string) =>
    unwrap(await request<AiSemanticDiff | ApiEnvelope<AiSemanticDiff>>({ url: "/ai/diff", method: "GET", params: { sha1, sha2, file } })),
  getAiIntent: async (sha1: string) =>
    unwrap(
      await request<CommitIntentResult | ApiEnvelope<CommitIntentResult>>({
        url: "/ai/intent",
        method: "GET",
        params: { sha1 },
        timeout: 90000,
      }),
    ),
  getAiProphecy: async (branch1: string, branch2: string) =>
    unwrap(
      await request<ProphecyReport | ApiEnvelope<ProphecyReport>>({
        url: "/ai/prophet",
        method: "GET",
        params: { branch1, branch2 },
        timeout: 120000,
      }),
    ),
  getAiProphecyTrajectory: async (branch1: string, branch2: string) =>
    unwrap(
      await request<TrajectoryResponse | ApiEnvelope<TrajectoryResponse>>({
        url: "/ai/prophet/trajectory",
        method: "GET",
        params: { branch1, branch2 },
        timeout: 120000,
      }),
    ),
  getAiProphecyQuickScore: async (branch1: string, branch2: string) =>
    unwrap(
      await request<number | ApiEnvelope<number>>({
        url: "/ai/prophet/quick",
        method: "GET",
        params: { branch1, branch2 },
        timeout: 90000,
      }),
    ),
  queryCodebaseMemory: async (branch: string, question: string) =>
    unwrap(
      await request<QueryResult | ApiEnvelope<QueryResult>>({
        url: "/ai/query",
        method: "POST",
        params: { branch },
        data: question,
        timeout: 120000,
      }),
    ),
};
