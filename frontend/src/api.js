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
  if (!response.ok) throw new Error(data.error?.message || "Registration failed");
  return data.data; // Extract data from ApiResponse
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
  if (!response.ok) throw new Error(data.error?.message || "Login failed");
  return data.data; // Extract data from ApiResponse
}

export async function fetchTasks(userId) {
  console.log("Fetching tasks for userId:", userId, "type:", typeof userId);
  const response = await fetch(`${TASK_URL}/user/${userId}`, {
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to fetch tasks");
  const data = await response.json();
  return data.data; // Extract data from ApiResponse
}

export async function createTask(userId, task) {
  console.log("Creating task for user:", userId, "with payload:", task);
  const response = await fetch(`${TASK_URL}/user/${userId}`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(task),
  });
  console.log("Response status:", response.status);
  const errorText = await response.text();
  console.log("Response body:", errorText);
  if (!response.ok) {
    try {
      const errorData = JSON.parse(errorText);
      throw new Error(errorData.error?.message || "Failed to create task");
    } catch (e) {
      throw new Error(`Failed to create task: ${errorText}`);
    }
  }
  const data = JSON.parse(errorText);
  return data.data; // Extract data from ApiResponse
}

export async function deleteTask(taskId) {
  const response = await fetch(`${TASK_URL}/${taskId}`, {
    method: "DELETE",
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to delete task");
  const data = await response.json();
  return data.data; // Extract data from ApiResponse
}

export async function updateTask(taskId, task) {
  const response = await fetch(`${TASK_URL}/${taskId}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(task),
  });
  if (!response.ok) throw new Error("Failed to update task");
  const data = await response.json();
  return data.data; // Extract data from ApiResponse
}

// Add new API functions for profile and notifications
export async function getProfile() {
  const response = await fetch("/api/profile", {
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to fetch profile");
  const data = await response.json();
  return data.data;
}

export async function updateProfile(profileData) {
  const response = await fetch("/api/profile", {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(profileData),
  });
  if (!response.ok) throw new Error("Failed to update profile");
  const data = await response.json();
  return data.data;
}

export async function changePassword(passwordData) {
  const response = await fetch("/api/profile/password", {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(passwordData),
  });
  if (!response.ok) throw new Error("Failed to change password");
  const data = await response.json();
  return data.data;
}

export async function getNotifications() {
  const response = await fetch("/api/notifications", {
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to fetch notifications");
  const data = await response.json();
  return data.data;
}

export async function markNotificationAsRead(notificationId) {
  const response = await fetch(`/api/notifications/read/${notificationId}`, {
    method: "PUT",
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to mark notification as read");
  const data = await response.json();
  return data.data;
}

export async function markAllNotificationsAsRead() {
  const response = await fetch("/api/notifications/read-all", {
    method: "PUT",
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to mark all notifications as read");
  const data = await response.json();
  return data.data;
}

export async function logout() {
  const response = await fetch("/api/auth/logout", {
    method: "POST",
    headers: authHeaders(),
  });
  if (!response.ok) throw new Error("Failed to logout");
  const data = await response.json();
  return data.data;
}