import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import TaskForm from "./TaskForm";
import Notifications from "../components/Notifications";
import { fetchTasks, deleteTask, updateTask, logout } from "../api";

function DashboardPage() {
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [sortBy, setSortBy] = useState("deadline"); // "deadline" or "status"

  const user = useMemo(() => {
    const raw = localStorage.getItem("tasknestUser");
    if (!raw) return null;
    
    const parsed = JSON.parse(raw);
    console.log("User data from localStorage:", parsed);
    console.log("User ID type:", typeof parsed.userId);
    console.log("User ID value:", parsed.userId);
    
    // Validate that userId exists and is a valid number
    if (!parsed.userId || parsed.userId === "3") {
      console.warn("Invalid or hardcoded userId detected, clearing localStorage");
      localStorage.removeItem("tasknestUser");
      return null;
    }
    
    return parsed;
  }, []);

  useEffect(() => {
    if (!user) {
      navigate("/login", { replace: true });
      return;
    }
    fetchTasks(user.userId).then(setTasks).catch(console.error);
  }, [user, navigate]);

  function handleTaskCreated(newTask) {
    setTasks((prev) => [...prev, newTask]);
    setShowModal(false);
  }

  async function handleDelete(taskId) {
    await deleteTask(taskId);
    setTasks((prev) => prev.filter((t) => t.id !== taskId));
  }

  async function handleToggleStatus(task) {
    let newStatus;
    if (task.status === "pending") {
      newStatus = "completed";
    } else if (task.status === "completed") {
      newStatus = "pending";
    } else if (task.status === "overdue") {
      newStatus = "completed";
    } else {
      newStatus = "pending";
    }
    
    const updated = await updateTask(task.id, {
      ...task,
      status: newStatus,
    });
    setTasks((prev) => prev.map((t) => (t.id === updated.id ? updated : t)));
  }

  async function handleLogout() {
    try {
      await logout(); // Call the API logout function
      localStorage.removeItem("tasknestUser");
      navigate("/login");
    } catch (error) {
      console.error("Logout failed:", error);
      // Still clear local storage and navigate even if API call fails
      localStorage.removeItem("tasknestUser");
      navigate("/login");
    }
  }

  if (!user) return null;

  // Sort tasks based on selected criteria
  const sortedTasks = useMemo(() => {
    const sorted = [...tasks];
    if (sortBy === "deadline") {
      sorted.sort((a, b) => {
        if (!a.deadline) return 1;
        if (!b.deadline) return -1;
        return new Date(a.deadline) - new Date(b.deadline);
      });
    } else if (sortBy === "status") {
      const statusOrder = { overdue: 0, pending: 1, completed: 2 };
      sorted.sort((a, b) => statusOrder[a.status] - statusOrder[b.status]);
    }
    return sorted;
  }, [tasks, sortBy]);

  // Get deadline urgency text and color
  function getDeadlineInfo(deadline) {
    if (!deadline) return { text: "No deadline", color: "#666" };
    
    const now = new Date();
    const deadlineDate = new Date(deadline);
    const diffTime = deadlineDate - now;
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
    
    if (diffDays < 0) {
      return { text: `Overdue by ${Math.abs(diffDays)} day${Math.abs(diffDays) !== 1 ? 's' : ''}`, color: "#dc3545" };
    } else if (diffDays === 0) {
      return { text: "Due today", color: "#fd7e14" };
    } else if (diffDays === 1) {
      return { text: "Due tomorrow", color: "#fd7e14" };
    } else if (diffDays <= 3) {
      return { text: `Due in ${diffDays} days`, color: "#ffc107" };
    } else {
      return { text: `Due in ${diffDays} days`, color: "#28a745" };
    }
  }

  return (
    <div className="dashboard-shell">
      <nav className="navbar">
        <h1>TaskNest</h1>
        <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
          <Notifications />
          <button
            onClick={() => navigate("/profile")}
            style={{
              background: "#6c757d",
              color: "white",
              border: "none",
              borderRadius: "4px",
              padding: "6px 12px",
              cursor: "pointer",
              fontSize: "14px"
            }}
          >
            Profile
          </button>
          <button onClick={handleLogout}>Logout</button>
        </div>
      </nav>

      <div className="container">
        <h2>Welcome, {user.username}!</h2>

        {/* Your Tasks header with Create button */}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginTop: "2rem" }}>
          <div>
            <h3 style={{ margin: 0 }}>Your Tasks</h3>
            <div style={{ marginTop: "8px" }}>
              <label style={{ marginRight: "8px", fontSize: "14px" }}>Sort by:</label>
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value)}
                style={{
                  padding: "4px 8px",
                  borderRadius: "4px",
                  border: "1px solid #ddd",
                  fontSize: "14px"
                }}
              >
                <option value="deadline">Deadline</option>
                <option value="status">Status</option>
              </select>
            </div>
          </div>
          <button
            onClick={() => setShowModal(true)}
            style={{
              backgroundColor: "#6c63ff",
              color: "white",
              border: "none",
              borderRadius: "8px",
              padding: "8px 16px",
              cursor: "pointer",
              fontWeight: "bold",
              fontSize: "14px"
            }}
          >
            + Create Task
          </button>
        </div>

        {/* Task list */}
        {tasks.length === 0 && <p>No tasks yet. Click "Create Task" to add one!</p>}
        <ul style={{ listStyle: "none", padding: 0, marginTop: "1rem" }}>
          {sortedTasks.map((task) => (
            <li key={task.id} style={{
              background: "white",
              borderRadius: "8px",
              padding: "1rem",
              marginBottom: "0.75rem",
              boxShadow: "0 1px 4px rgba(0,0,0,0.1)"
            }}>
              <strong>{task.title}</strong>
              <span style={{
                marginLeft: "10px",
                fontSize: "12px",
                background: task.status === "completed" ? "#d4edda" : 
                           task.status === "overdue" ? "#f8d7da" : "#fff3cd",
                color: task.status === "completed" ? "#155724" : 
                      task.status === "overdue" ? "#721c24" : "#856404",
                padding: "2px 8px",
                borderRadius: "12px",
                fontWeight: task.status === "overdue" ? "bold" : "normal"
              }}>
                {task.status.toUpperCase()}
              </span>
              <br />
              <small style={{ color: "#666" }}>{task.description}</small>
              <br />
              <small style={{ color: getDeadlineInfo(task.deadline).color }}>
                {getDeadlineInfo(task.deadline).text}
              </small>
              <br />
              <div style={{ marginTop: "0.5rem", display: "flex", gap: "8px" }}>
                <button
                  onClick={() => handleToggleStatus(task)}
                  style={{
                    backgroundColor: task.status === "pending" ? "#28a745" : 
                                     task.status === "completed" ? "#6c757d" : "#dc3545",
                    color: "white",
                    border: "none",
                    borderRadius: "6px",
                    padding: "4px 12px",
                    cursor: "pointer",
                    fontSize: "13px"
                  }}
                >
                  {task.status === "pending" ? "Mark Complete" : 
                   task.status === "completed" ? "Mark Pending" : "Mark Complete"}
                </button>
                <button
                  onClick={() => handleDelete(task.id)}
                  style={{
                    backgroundColor: "#dc3545",
                    color: "white",
                    border: "none",
                    borderRadius: "6px",
                    padding: "4px 12px",
                    cursor: "pointer",
                    fontSize: "13px"
                  }}
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>

      {/* Modal */}
      {showModal && (
        <div style={{
          position: "fixed",
          top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: "rgba(0,0,0,0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000
        }}>
          <div style={{
            background: "white",
            borderRadius: "12px",
            padding: "2rem",
            width: "400px",
            boxShadow: "0 4px 20px rgba(0,0,0,0.2)"
          }}>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "1rem" }}>
              <h3 style={{ margin: 0 }}>Create New Task</h3>
              <button
                onClick={() => setShowModal(false)}
                style={{ background: "none", border: "none", fontSize: "20px", cursor: "pointer", color: "#666" }}
              >
                ✕
              </button>
            </div>
            <TaskForm userId={user.userId} onTaskCreated={handleTaskCreated} />
          </div>
        </div>
      )}
    </div>
  );
}

export default DashboardPage;