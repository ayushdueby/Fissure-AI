import { GitBranch, GitCommitHorizontal, LayoutDashboard, Sparkles, SplitSquareHorizontal } from "lucide-react";
import { Card } from "@/components/ui/card";

const items = [
  { href: "#dashboard", label: "Dashboard", icon: LayoutDashboard },
  { href: "#repository", label: "Repository", icon: GitBranch },
  { href: "#commits", label: "Commits", icon: GitCommitHorizontal },
  { href: "#diff", label: "Diff", icon: SplitSquareHorizontal },
  { href: "#ai", label: "AI Semantic Diff", icon: Sparkles },
];

export function Sidebar() {
  return (
    <Card className="sticky top-6 h-fit p-3">
      <div className="px-2 py-3 text-sm font-semibold text-slate-100">Fissure AI</div>
      <nav className="space-y-1">
        {items.map((item) => (
          <a
            key={item.href}
            href={item.href}
            className="flex items-center gap-2 rounded-lg px-2 py-2 text-sm text-slate-300 transition hover:bg-slate-800/70 hover:text-slate-100"
          >
            <item.icon className="h-4 w-4" />
            {item.label}
          </a>
        ))}
      </nav>
    </Card>
  );
}
