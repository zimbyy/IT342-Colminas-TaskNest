import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getProfile, updateProfile, changePassword } from "../api";

function ProfilePage() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // Form states
  const [profileForm, setProfileForm] = useState({
    firstName: "",
    lastName: ""
  });
  const [passwordForm, setPasswordForm] = useState({
    currentPassword: "",
    newPassword: "",
    confirmPassword: ""
  });

  useEffect(() => {
    fetchProfile();
  }, []);

  async function fetchProfile() {
    try {
      const data = await getProfile();
      setProfile(data);
      setProfileForm({
        firstName: data.firstName,
        lastName: data.lastName
      });
    } catch (err) {
      setError("Failed to fetch profile");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }

  function handleProfileChange(e) {
    setProfileForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  function handlePasswordChange(e) {
    setPasswordForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  }

  async function handleProfileSubmit(e) {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      await updateProfile(profileForm);
      setMessage("Profile updated successfully!");
      fetchProfile(); // Refresh profile data
    } catch (err) {
      setError(err.message || "Failed to update profile");
    }
  }

  async function handlePasswordSubmit(e) {
    e.preventDefault();
    setMessage("");
    setError("");

    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      setError("New password and confirm password do not match");
      return;
    }

    if (passwordForm.newPassword.length < 6) {
      setError("New password must be at least 6 characters");
      return;
    }

    try {
      await changePassword(passwordForm);
      setMessage("Password changed successfully!");
      setPasswordForm({
        currentPassword: "",
        newPassword: "",
        confirmPassword: ""
      });
    } catch (err) {
      setError(err.message || "Failed to change password");
    }
  }

  if (loading) {
    return <div style={{ padding: "20px", textAlign: "center" }}>Loading...</div>;
  }

  if (!profile) {
    return <div style={{ padding: "20px", textAlign: "center" }}>Profile not found</div>;
  }

  return (
    <div style={{ padding: "20px", maxWidth: "600px", margin: "0 auto" }}>
      <h1>Profile Settings</h1>

      {message && (
        <div style={{
          background: "#d4edda",
          color: "#155724",
          padding: "12px",
          borderRadius: "6px",
          marginBottom: "20px"
        }}>
          {message}
        </div>
      )}

      {error && (
        <div style={{
          background: "#f8d7da",
          color: "#721c24",
          padding: "12px",
          borderRadius: "6px",
          marginBottom: "20px"
        }}>
          {error}
        </div>
      )}

      {/* Profile Information */}
      <div style={{
        background: "white",
        padding: "20px",
        borderRadius: "8px",
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
        marginBottom: "20px"
      }}>
        <h2>Profile Information</h2>
        <div style={{ marginBottom: "16px" }}>
          <strong>Username:</strong> {profile.username}
        </div>
        <div style={{ marginBottom: "16px" }}>
          <strong>Member Since:</strong> {new Date(profile.createdAt).toLocaleDateString()}
        </div>
      </div>

      {/* Edit Profile Form */}
      <div style={{
        background: "white",
        padding: "20px",
        borderRadius: "8px",
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
        marginBottom: "20px"
      }}>
        <h2>Edit Profile</h2>
        <form onSubmit={handleProfileSubmit}>
          <div style={{ marginBottom: "16px" }}>
            <label style={{ display: "block", marginBottom: "4px", fontWeight: "bold" }}>
              First Name
            </label>
            <input
              type="text"
              name="firstName"
              value={profileForm.firstName}
              onChange={handleProfileChange}
              required
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ddd",
                borderRadius: "4px",
                fontSize: "14px"
              }}
            />
          </div>
          <div style={{ marginBottom: "16px" }}>
            <label style={{ display: "block", marginBottom: "4px", fontWeight: "bold" }}>
              Last Name
            </label>
            <input
              type="text"
              name="lastName"
              value={profileForm.lastName}
              onChange={handleProfileChange}
              required
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ddd",
                borderRadius: "4px",
                fontSize: "14px"
              }}
            />
          </div>
          <button
            type="submit"
            style={{
              background: "#007bff",
              color: "white",
              border: "none",
              borderRadius: "4px",
              padding: "10px 20px",
              cursor: "pointer",
              fontSize: "14px"
            }}
          >
            Update Profile
          </button>
        </form>
      </div>

      {/* Change Password Form */}
      <div style={{
        background: "white",
        padding: "20px",
        borderRadius: "8px",
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
      }}>
        <h2>Change Password</h2>
        <form onSubmit={handlePasswordSubmit}>
          <div style={{ marginBottom: "16px" }}>
            <label style={{ display: "block", marginBottom: "4px", fontWeight: "bold" }}>
              Current Password
            </label>
            <input
              type="password"
              name="currentPassword"
              value={passwordForm.currentPassword}
              onChange={handlePasswordChange}
              required
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ddd",
                borderRadius: "4px",
                fontSize: "14px"
              }}
            />
          </div>
          <div style={{ marginBottom: "16px" }}>
            <label style={{ display: "block", marginBottom: "4px", fontWeight: "bold" }}>
              New Password
            </label>
            <input
              type="password"
              name="newPassword"
              value={passwordForm.newPassword}
              onChange={handlePasswordChange}
              required
              minLength="6"
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ddd",
                borderRadius: "4px",
                fontSize: "14px"
              }}
            />
          </div>
          <div style={{ marginBottom: "16px" }}>
            <label style={{ display: "block", marginBottom: "4px", fontWeight: "bold" }}>
              Confirm New Password
            </label>
            <input
              type="password"
              name="confirmPassword"
              value={passwordForm.confirmPassword}
              onChange={handlePasswordChange}
              required
              minLength="6"
              style={{
                width: "100%",
                padding: "8px",
                border: "1px solid #ddd",
                borderRadius: "4px",
                fontSize: "14px"
              }}
            />
          </div>
          <button
            type="submit"
            style={{
              background: "#28a745",
              color: "white",
              border: "none",
              borderRadius: "4px",
              padding: "10px 20px",
              cursor: "pointer",
              fontSize: "14px"
            }}
          >
            Change Password
          </button>
        </form>
      </div>

      <div style={{ marginTop: "20px" }}>
        <button
          onClick={() => navigate("/dashboard")}
          style={{
            background: "#6c757d",
            color: "white",
            border: "none",
            borderRadius: "4px",
            padding: "10px 20px",
            cursor: "pointer",
            fontSize: "14px"
          }}
        >
          Back to Dashboard
        </button>
      </div>
    </div>
  );
}

export default ProfilePage;
