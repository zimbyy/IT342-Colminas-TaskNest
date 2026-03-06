import { useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";

function DashboardPage() {
  const navigate = useNavigate();

  const user = useMemo(() => {
    const raw = localStorage.getItem("tasknestUser");
    return raw ? JSON.parse(raw) : null;
  }, []);

  useEffect(() => {
    if (!user) {
      navigate("/login", { replace: true });
    }
  }, [navigate, user]);

  function logout() {
    localStorage.removeItem("tasknestUser");
    navigate("/login");
  }

  if (!user) {
    return null;
  }

  return (
    <div className="dashboard-shell">
      <nav className="navbar">
        <h1>TaskNest Dashboard</h1>
        <button onClick={logout}>Logout</button>
      </nav>

      <div className="container">
        <div className="welcome-card">
          <h2>Login Successful!</h2>
          <p>Welcome to TaskNest - Your task management system</p>
        </div>

        <div className="features">
          <div className="feature-card">
            <div className="icon"></div>
            <h3>Create Tasks</h3>
            <p>Organize your work efficiently</p>
          </div>
          <div className="feature-card">
            <div className="icon"></div>
            <h3>Collaborate</h3>
            <p>Work together with your team</p>
          </div>
          <div className="feature-card">
            <div className="icon"></div>
            <h3>Track Progress</h3>
            <p>Monitor your task completion</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default DashboardPage;
