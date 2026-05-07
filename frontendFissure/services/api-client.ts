import axios, { AxiosError, AxiosRequestConfig } from "axios";

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_GIT_API_BASE || "http://localhost:8080/git",
  timeout: 30000,
  headers: { "Content-Type": "application/json" },
});

const RETRY_CODES = new Set([408, 429, 500, 502, 503, 504]);

export async function withRetry<T>(
  request: () => Promise<T>,
  retries = 2,
): Promise<T> {
  let lastError: unknown;
  for (let attempt = 0; attempt <= retries; attempt += 1) {
    try {
      return await request();
    } catch (error) {
      lastError = error;
      const status = (error as AxiosError)?.response?.status;
      if (attempt === retries || (status && !RETRY_CODES.has(status))) {
        throw error;
      }
      await new Promise((resolve) => setTimeout(resolve, 400 * (attempt + 1)));
    }
  }
  throw lastError;
}

export async function request<T>(config: AxiosRequestConfig): Promise<T> {
  try {
    const response = await withRetry(() => api.request<T>(config));
    return response.data;
  } catch (error) {
    const axiosError = error as AxiosError<{ message?: string }>;
    const status = axiosError.response?.status;
    const serverMessage = axiosError.response?.data?.message;
    if (axiosError.code === "ECONNABORTED") {
      throw new Error("Request timed out while waiting for AI/backend response.");
    }
    if (serverMessage) {
      throw new Error(serverMessage);
    }
    if (status) {
      throw new Error(`Request failed with status ${status}`);
    }
    throw new Error("Unable to reach backend. Verify backend is running.");
  }
}
