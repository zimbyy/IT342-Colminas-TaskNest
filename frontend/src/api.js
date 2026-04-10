const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

// Auth endpoints
export async function registerUser(payload) {
  const response = await fetch(`${API_BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.error || "Registration failed");
  }

  return data;
}

export async function loginUser(payload) {
  const response = await fetch(`${API_BASE_URL}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });

  const data = await response.json();
  if (!response.ok) {
    throw new Error(data.error || "Login failed");
  }

  return data;
}

// Task endpoints
export async function getUserTasks(userId) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/user/${userId}`);
    
    if (!response.ok) {
      console.error("Failed to fetch tasks, status:", response.status);
      return [];
    }
    
    const text = await response.text();
    if (!text) {
      return [];
    }
    
    const data = JSON.parse(text);
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Error fetching tasks:", error);
    return [];
  }
}

export async function getPendingTasks(userId) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/user/${userId}/pending`);
    
    if (!response.ok) {
      return [];
    }
    
    const text = await response.text();
    if (!text) {
      return [];
    }
    
    const data = JSON.parse(text);
    return Array.isArray(data) ? data : [];
  } catch (error) {
    console.error("Error fetching pending tasks:", error);
    return [];
  }
}

export async function getTaskById(taskId) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/${taskId}`);
    
    if (!response.ok) {
      throw new Error("Failed to fetch task");
    }
    
    const text = await response.text();
    if (!text) return null;
    return JSON.parse(text);
  } catch (error) {
    console.error("Error fetching task:", error);
    throw error;
  }
}

export async function createTask(userId, payload) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/user/${userId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    const text = await response.text();
    console.log("Response status:", response.status);
    console.log("Response text:", text);
    console.log("Payload sent:", payload);

    if (!response.ok) {
      let errorMsg = "Failed to create task";
      try {
        const errorData = JSON.parse(text);
        errorMsg = errorData.error || errorMsg;
      } catch (e) {
        // If not JSON, use text as error
        errorMsg = text || errorMsg;
      }
      throw new Error(errorMsg);
    }

    return JSON.parse(text);
  } catch (error) {
    console.error("Error creating task:", error);
    throw error;
  }
}

export async function updateTask(taskId, payload) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/${taskId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!response.ok) {
      throw new Error("Failed to update task");
    }

    const text = await response.text();
    return JSON.parse(text);
  } catch (error) {
    console.error("Error updating task:", error);
    throw error;
  }
}

export async function completeTask(taskId) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/${taskId}/complete`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" }
    });

    if (!response.ok) {
      throw new Error("Failed to complete task");
    }

    const text = await response.text();
    return JSON.parse(text);
  } catch (error) {
    console.error("Error completing task:", error);
    throw error;
  }
}

export async function incompleteTask(taskId) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/${taskId}/incomplete`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" }
    });

    if (!response.ok) {
      throw new Error("Failed to mark task incomplete");
    }

    const text = await response.text();
    return JSON.parse(text);
  } catch (error) {
    console.error("Error marking task incomplete:", error);
    throw error;
  }
}

export async function deleteTask(taskId) {
  try {
    const response = await fetch(`${API_BASE_URL}/tasks/${taskId}`, {
      method: "DELETE"
    });

    if (!response.ok) {
      throw new Error("Failed to delete task");
    }

    const text = await response.text();
    if (!text) return { message: "Task deleted successfully" };
    return JSON.parse(text);
  } catch (error) {
    console.error("Error deleting task:", error);
    throw error;
  }
}
