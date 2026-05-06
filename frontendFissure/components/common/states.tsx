import { Card } from "@/components/ui/card";

export function LoadingState({ label = "Loading..." }: { label?: string }) {
  return <Card className="p-4 text-sm text-slate-300">{label}</Card>;
}

export function ErrorState({ message }: { message: string }) {
  return <Card className="border-rose-900/60 p-4 text-sm text-rose-300">{message}</Card>;
}

export function EmptyState({ label }: { label: string }) {
  return <Card className="p-4 text-sm text-slate-400">{label}</Card>;
}
