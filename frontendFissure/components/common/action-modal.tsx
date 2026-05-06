"use client";

import * as Dialog from "@radix-ui/react-dialog";
import { ReactNode } from "react";
import { Button } from "@/components/ui/button";

export function ActionModal({
  trigger,
  title,
  description,
  onConfirm,
}: {
  trigger: ReactNode;
  title: string;
  description: string;
  onConfirm: () => void;
}) {
  return (
    <Dialog.Root>
      <Dialog.Trigger asChild>{trigger}</Dialog.Trigger>
      <Dialog.Portal>
        <Dialog.Overlay className="fixed inset-0 bg-black/60" />
        <Dialog.Content className="fixed left-1/2 top-1/2 w-[92vw] max-w-md -translate-x-1/2 -translate-y-1/2 rounded-xl border border-slate-800 bg-slate-950 p-5">
          <Dialog.Title className="text-sm font-semibold text-slate-100">{title}</Dialog.Title>
          <Dialog.Description className="mt-2 text-sm text-slate-300">{description}</Dialog.Description>
          <div className="mt-4 flex justify-end gap-2">
            <Dialog.Close asChild>
              <Button variant="secondary">Cancel</Button>
            </Dialog.Close>
            <Dialog.Close asChild>
              <Button onClick={onConfirm}>Confirm</Button>
            </Dialog.Close>
          </div>
        </Dialog.Content>
      </Dialog.Portal>
    </Dialog.Root>
  );
}
