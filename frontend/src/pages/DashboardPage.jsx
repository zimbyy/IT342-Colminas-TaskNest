import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getUserTasks,
  createTask,
  updateTask,
  completeTask,
  incompleteTask,
  deleteTask,
} from "../api";

function DashboardPage() {
  const navigate = useNavigate();
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    deadline: "",
  });

  const user = useMemo(() => {
    const raw = localStorage.getItem("tasknestUser");
    return raw ? JSON.parse(raw) : null;
  }, []);

  useEffect(() => {
    if (!user) {
      navigate("/login", { replace: true });
    } else {
      fetchTasks();
    }
  }, [navigate, user]);

  async function fetchTasks() {
    try {
      setLoading(true);
      setError("");
      const data = await getUserTasks(user.userId);
      setTasks(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  function resetForm() {
    setFormData({ title: "", description: "", deadline: "" });
    setEditingId(null);
    setShowForm(false);
  }

  function handleEditClick(task) {
    setEditingId(task.id);
    setFormData({
      title: task.title,
      description: task.description || "",
      deadline: task.deadline.substring(0, 16),
    });
    setShowForm(true);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    
    if (!formData.title.trim()) {
      setError("Task title is required");
      return;
    }

    if (!formData.deadline) {
      setError("Deadline is required");
      return;
    }

    try {
      setLoading(true);
      setError("");

      const payload = {
        title: formData.title,
        description: formData.description,
        deadline: formData.deadline + ":00",
      };

      if (editingId) {
        await updateTask(editingId, payload);
        setSuccessMessage("Task updated successfully!");
      } else {
        await createTask(user.userId, payload);
        setSuccessMessage("Task created successfully!");
      }

      resetForm();
      await fetchTasks();
      setTimeout(() => setSuccessMessage(""), 3000);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function handleComplete(taskId, isCompleted) {
    try {
      setError("");
      if (isCompleted) {
        await incompleteTask(taskId);
        setSuccessMessage("Task marked as pending");
      } else {
        await completeTask(taskId);
        setSuccessMessage("Task completed!");
      }
      await fetchTasks();
      setTimeout(() => setSuccessMessage(""), 3000);
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDelete(taskId) {
    if (confirm("Are you sure you want to delete this task?")) {
      try {
        setError("");
        await deleteTask(taskId);
        setSuccessMessage("Task deleted successfully!");
        await fetchTasks();
        setTimeout(() => setSuccessMessage(""), 3000);
      } catch (err) {
        setError(err.message);
      }
    }
  }

  function logout() {
    localStorage.removeItem("tasknestUser");
    navigate("/login");
  }

  if (!user) {
    return null;
  }

  function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + " " + date.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    });
  }

  function isDeadlineSoon(deadline) {
    const now = new Date();
    const deadlineDate = new Date(deadline);
    const hoursRemaining = (deadlineDate - now) / (1000 * 60 * 60);
    return hoursRemaining > 0 && hoursRemaining <= 24;
  }

  function isDeadlinePassed(deadline) {
    const now = new Date();
    const deadlineDate = new Date(deadline);
    return deadlineDate < now;
  }

  return (
    <div className="dashboard-shell">
      <nav className="navbar">
        <div className="navbar-content">
          <div>
            <h1>TaskNest Dashboard</h1>
            <p className="welcom-user">Welcome, {user.firstName}!</p>
          </div>
          <button onClick={logout} className="logout-btn">Logout</button>
        </div>
      </nav>

      <div className="container dashboard-container">
        {successMessage && <div className="success-message">{successMessage}</div>}
        {error && <div className="error-message">{error}</div>}

        <div className="dashboard-header">
          <h2>Your Tasks</h2>
          {!showForm && (
            <button onClick={() => setShowForm(true)} className="btn btn-primary">
              + New Task
            </button>
          )}
        </div>

        {showForm && (
          <div className="task-form-container">
            <h3>{editingId ? "Edit Task" : "Create New Task"}</h3>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label htmlFor="title">Task Title *</label>
                <input
                  id="title"
                  type="text"
                  placeholder="Enter task title..."
                  value={formData.title}
                  onChange={(e) =>
                    setFormData({ ...formData, title: e.target.value })
                  }
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="description">Description</label>
                <textarea
                  id="description"
                  placeholder="Enter task description..."
                  value={formData.description}
                  onChange={(e) =>
                    setFormData({ ...formData, description: e.target.value })
                  }
                  rows="4"
                ></textarea>
              </div>

              <div className="form-group">
                <label htmlFor="deadline">Deadline *</label>
                <input
                  id="deadline"
                  type="datetime-local"
                  value={formData.deadline}
                  onChange={(e) =>
                    setFormData({ ...formData, deadline: e.target.value })
                  }
                  required
                />
              </div>

              <div className="form-buttons">
                <button type="submit" className="btn btn-primary" disabled={loading}>
                  {loading ? "Saving..." : editingId ? "Update Task" : "Create Task"}
                </button>
                <button
                  type="button"
                  onClick={resetForm}
                  className="btn btn-secondary"
                  disabled={loading}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        )}

        {loading && !showForm ? (
          <div className="loading">Loading tasks...</div>
        ) : tasks.length === 0 ? (
          <div className="empty-state">
            <p>No tasks yet. Create your first task to get started!</p>
          </div>
        ) : (
          <div className="tasks-list">
            {tasks.map((task) => (
              <div
                key={task.id}
                className={`task-card ${task.isCompleted ? "completed" : ""} ${
                  isDeadlineSoon(task.deadline) && !task.isCompleted ? "deadline-soon" : ""
                } ${isDeadlinePassed(task.deadline) && !task.isCompleted ? "deadline-passed" : ""}`}
              >
                <div className="task-header">
                  <div className="task-title-section">
                    <input
                      type="checkbox"
                      checked={task.isCompleted}
                      onChange={() =>
                        handleComplete(task.id, task.isCompleted)
                      }
                      className="task-checkbox"
                    />
                    <h3 className={task.isCompleted ? "strike-through" : ""}>
                      {task.title}
                    </h3>
                  </div>
                  <div className="task-actions">
                    <button
                      onClick={() => handleEditClick(task)}
                      className="btn-icon edit"
                      title="Edit"
                    >
                      ✎
                    </button>
                    <button
                      onClick={() => handleDelete(task.id)}
                      className="btn-icon delete"
                      title="Delete"
                    >
                      ✕
                    </button>
                  </div>
                </div>

                {task.description && (
                  <p className="task-description">{task.description}</p>
                )}

                <div className="task-meta">
                  <span className="deadline">
                    📅 {formatDate(task.deadline)}
                  </span>
                  {isDeadlineSoon(task.deadline) && !task.isCompleted && (
                    <span className="deadline-warning">⚠️ Due soon!</span>
                  )}
                  {isDeadlinePassed(task.deadline) && !task.isCompleted && (
                    <span className="deadline-error">❌ Overdue!</span>
                  )}
                  {task.isCompleted && (
                    <span className="completed-badge">✓ Completed</span>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default DashboardPage;
