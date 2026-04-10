import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import TaskForm from "./TaskForm";
import { fetchTasks, deleteTask, updateTask } from "../api";

function DashboardPage() {
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const user = useMemo(() => {
    const raw = localStorage.getItem("tasknestUser");
    return raw ? JSON.parse(raw) : null;
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
    const updated = await updateTask(task.id, {
      ...task,
      status: task.status === "pending" ? "completed" : "pending",
    });
    setTasks((prev) => prev.map((t) => (t.id === updated.id ? updated : t)));
  }

  function logout() {
    localStorage.removeItem("tasknestUser");
    navigate("/login");
  }

  if (!user) return null;

  return (
    <div className="dashboard-shell">
      <nav className="navbar">
        <h1>TaskNest</h1>
        <button onClick={logout}>Logout</button>
      </nav>

      <div className="container">
        <h2>Welcome, {user.username}!</h2>

        {/* Your Tasks header with Create button */}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginTop: "2rem" }}>
          <h3 style={{ margin: 0 }}>Your Tasks</h3>
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
          {tasks.map((task) => (
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
                background: task.status === "completed" ? "#d4edda" : "#fff3cd",
                color: task.status === "completed" ? "#155724" : "#856404",
                padding: "2px 8px",
                borderRadius: "12px"
              }}>
                {task.status}
              </span>
              <br />
              <small style={{ color: "#666" }}>{task.description}</small>
              <br />
              <small style={{ color: "#999" }}>Due: {task.deadline}</small>
              <br />
              <div style={{ marginTop: "0.5rem", display: "flex", gap: "8px" }}>
                <button
                  onClick={() => handleToggleStatus(task)}
                  style={{
                    backgroundColor: task.status === "pending" ? "#28a745" : "#6c757d",
                    color: "white",
                    border: "none",
                    borderRadius: "6px",
                    padding: "4px 12px",
                    cursor: "pointer",
                    fontSize: "13px"
                  }}
                >
                  {task.status === "pending" ? "Mark Complete" : "Mark Pending"}
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