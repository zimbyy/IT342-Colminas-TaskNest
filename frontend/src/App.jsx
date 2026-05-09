import { Navigate, Route, Routes } from "react-router-dom";
import RegisterPage from "./features/authentication/pages/RegisterPage";
import LoginPage from "./features/authentication/pages/LoginPage";
import DashboardPage from "./features/dashboard/pages/DashboardPage";
import ProfilePage from "./features/authentication/pages/ProfilePage";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
}

export default App;
