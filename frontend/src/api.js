const API_BASE_URL = "/api/auth";
const TASK_URL = "/api/tasks";

// Helper to get auth headers
function authHeaders() {
  const raw = localStorage.getItem("tasknestUser");
  const user = raw ? JSON.parse(raw) : null;
  const token = user?.token;
  return {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  };
}

export async function registerUser(payload) {
  const response = await fetch(`${API_BASE_URL}/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  const data = await response.json();
  if (!response.ok) throw new Error(data.error || "Registration failed");
  return data;
}

export async function loginUser(payload) {
  const response = await fetch(`${API_BASE_URL}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });
  const text = await response.text();
  let data;
  try {
    data = JSON.parse(text);
  } catch (e) {
    throw new Error("Invalid JSON response from server");
  }
  if (!response.ok) throw new Error(data.error || "Login failed");
  return data;
}

export async function fetchTasks(userId) {
  const response = await fetch(`${TASK_URL}/user/${userId}`, {
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to fetch tasks");
  return response.json();
}

export async function createTask(userId, task) {
  const response = await fetch(`${TASK_URL}/user/${userId}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(task),
  });
  if (!response.ok) throw new Error("Failed to create task");
  return response.json();
}

export async function deleteTask(taskId) {
  const response = await fetch(`${TASK_URL}/${taskId}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to delete task");
}

export async function updateTask(taskId, task) {
  const response = await fetch(`${TASK_URL}/${taskId}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(task),
  });
  if (!response.ok) throw new Error("Failed to update task");
  return response.json();
}