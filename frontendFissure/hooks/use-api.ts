"use client";

import { useCallback, useEffect, useState } from "react";
import { toast } from "sonner";

export function useApiQuery<T>(queryFn: () => Promise<T>, enabled = true) {
  const [data, setData] = useState<T | null>(null);
  const [loading, setLoading] = useState(enabled);
  const [error, setError] = useState<string | null>(null);

  const refetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await queryFn();
      setData(response);
      return response;
    } catch (e) {
      const message = e instanceof Error ? e.message : "Request failed";
      setError(message);
      toast.error(message);
      return null;
    } finally {
      setLoading(false);
    }
  }, [queryFn]);

  useEffect(() => {
    if (enabled) {
      void refetch();
    }
  }, [enabled, refetch]);

  return { data, loading, error, refetch };
}

export function useApiMutation<TPayload, TResult>(
  mutationFn: (payload: TPayload) => Promise<TResult>,
  successMessage: string,
) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const mutate = useCallback(
    async (payload: TPayload) => {
      setLoading(true);
      setError(null);
      try {
        const result = await mutationFn(payload);
        toast.success(successMessage);
        return result;
      } catch (e) {
        const message = e instanceof Error ? e.message : "Action failed";
        setError(message);
        toast.error(message);
        return null;
      } finally {
        setLoading(false);
      }
    },
    [mutationFn, successMessage],
  );

  return { mutate, loading, error };
}
