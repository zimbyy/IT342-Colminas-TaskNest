import { useState } from "react";
import { createTask } from "../api";

function TaskForm({ userId, onTaskCreated }) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [deadline, setDeadline] = useState("");
  const [error, setError] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    const taskPayload = {
      title,
      description,
      deadline: deadline ? new Date(deadline).toISOString().slice(0, 19) : null,
      status: "pending",
    };
        try {
      const newTask = await createTask(userId, taskPayload);
      onTaskCreated(newTask);
      setTitle("");
      setDescription("");
      setDeadline("");
      setError("");
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <input
        type="text"
        placeholder="Task title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <textarea
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
      />
      <input
        type="datetime-local"
        value={deadline}
        onChange={(e) => setDeadline(e.target.value)}
        required
      />
      <button type="submit">Add Task</button>
    </form>
  );
}

export default TaskForm;