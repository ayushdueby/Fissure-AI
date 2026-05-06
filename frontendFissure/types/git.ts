export type ApiEnvelope<T> = {
  success?: boolean;
  message?: string;
  data?: T;
};

export type Branch = {
  name: string;
  current?: boolean;
};

export type Commit = string;

export type DiffLine = {
  type: "ADDED" | "DELETED" | "UNCHANGED";
  diffContent: string;
};

export type MergePreview = {
  cleanMerge: boolean;
  detail?: string;
};

export type AiSemanticDiff = {
  summary: string;
  riskLevel: string;
  semanticGroups: Array<{ explanation?: string; lines?: string[] }>;
  suggestions: string[];
  type: string;
};

export type GraphResponse = {
  nodes: Array<{
    sha: string;
    message: string;
    author: string;
    timestamp: number;
    parents: string[];
  }>;
  branches: Record<string, string | null>;
};

export type CommitIntentResult = {
  intent: string;
  confidence: number;
  reasoning: string;
  riskLevel: string;
  suggestedMessage: string;
  relatedFiles: string[];
};

export type FileProphecy = {
  filename: string;
  conflictProbability: number;
  conflictType: string;
  reason: string;
  conflictingRegions: string[];
  suggestion: string;
  safeToMerge: boolean;
};

export type ProphecyReport = {
  branch1: string;
  branch2: string;
  lcaSha: string;
  divergedCommits: number;
  overallConflictProbability: number;
  fileProphecies: FileProphecy[];
  recommendation: string;
};

export type TrajectoryPoint = {
  afterCommit: string;
  probability: number;
};

export type TrajectoryResponse = {
  trajectories: TrajectoryPoint[];
  Trend: string;
};

export type QueryResult = {
  answer: string;
  intent: string;
  supportingCommits: string[];
  supportingFiles: string[];
  confidence: number;
};
