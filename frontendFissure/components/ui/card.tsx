import { cn } from "@/lib/utils";
import { HTMLAttributes } from "react";

export function Card({ className, ...props }: HTMLAttributes<HTMLDivElement>) {
  return (
    <div
      className={cn(
        "rounded-xl border border-slate-800/80 bg-slate-950/50 backdrop-blur-md shadow-glass",
        className,
      )}
      {...props}
    />
  );
}
