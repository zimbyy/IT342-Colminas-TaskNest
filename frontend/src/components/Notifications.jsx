import { useState, useEffect } from "react";
import { getNotifications, markNotificationAsRead, markAllNotificationsAsRead } from "../api";

function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    fetchNotifications();
    // Poll for new notifications every 30 seconds
    const interval = setInterval(fetchNotifications, 30000);
    return () => clearInterval(interval);
  }, []);

  async function fetchNotifications() {
    try {
      const data = await getNotifications();
      setNotifications(data.notifications || []);
      setUnreadCount(data.unreadCount || 0);
    } catch (error) {
      // Failed to fetch notifications
    }
  }

  async function handleMarkAsRead(notificationId) {
    try {
      await markNotificationAsRead(notificationId);
      fetchNotifications();
    } catch (error) {
      // Failed to mark notification as read
    }
  }

  async function handleMarkAllAsRead() {
    try {
      await markAllNotificationsAsRead();
      fetchNotifications();
    } catch (error) {
      // Failed to mark all notifications as read
    }
  }

  function getNotificationColor(type) {
    switch (type) {
      case "DEADLINE_REMINDER":
        return "#fff3cd";
      case "TASK_OVERDUE":
        return "#f8d7da";
      default:
        return "#d1ecf1";
    }
  }

  function getNotificationIcon(type) {
    switch (type) {
      case "DEADLINE_REMINDER":
        return "⏰";
      case "TASK_OVERDUE":
        return "⚠️";
      default:
        return "ℹ️";
    }
  }

  return (
    <div style={{ position: "relative", display: "inline-block" }}>
      <button
        onClick={() => setShowNotifications(!showNotifications)}
        style={{
          background: "none",
          border: "none",
          fontSize: "20px",
          cursor: "pointer",
          position: "relative",
          padding: "8px"
        }}
      >
        🔔
        {unreadCount > 0 && (
          <span
            style={{
              position: "absolute",
              top: "4px",
              right: "4px",
              background: "#dc3545",
              color: "white",
              borderRadius: "50%",
              width: "18px",
              height: "18px",
              fontSize: "12px",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              fontWeight: "bold"
            }}
          >
            {unreadCount > 9 ? "9+" : unreadCount}
          </span>
        )}
      </button>

      {showNotifications && (
        <div
          style={{
            position: "absolute",
            top: "40px",
            right: "0",
            background: "white",
            border: "1px solid #ddd",
            borderRadius: "8px",
            boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
            width: "350px",
            maxHeight: "400px",
            overflowY: "auto",
            zIndex: 1000
          }}
        >
          <div
            style={{
              padding: "12px",
              borderBottom: "1px solid #eee",
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center"
            }}
          >
            <h4 style={{ margin: 0, fontSize: "16px" }}>Notifications</h4>
            {unreadCount > 0 && (
              <button
                onClick={handleMarkAllAsRead}
                style={{
                  background: "#007bff",
                  color: "white",
                  border: "none",
                  borderRadius: "4px",
                  padding: "4px 8px",
                  fontSize: "12px",
                  cursor: "pointer"
                }}
              >
                Mark all read
              </button>
            )}
          </div>

          {notifications.length === 0 ? (
            <div style={{ padding: "20px", textAlign: "center", color: "#666" }}>
              No notifications
            </div>
          ) : (
            notifications.map((notification) => (
              <div
                key={notification.id}
                style={{
                  padding: "12px",
                  borderBottom: "1px solid #eee",
                  background: notification.read ? "white" : getNotificationColor(notification.type),
                  cursor: notification.read ? "default" : "pointer"
                }}
                onClick={() => !notification.read && handleMarkAsRead(notification.id)}
              >
                <div style={{ display: "flex", alignItems: "flex-start" }}>
                  <span style={{ marginRight: "8px", fontSize: "16px" }}>
                    {getNotificationIcon(notification.type)}
                  </span>
                  <div style={{ flex: 1 }}>
                    <div style={{ fontSize: "14px", marginBottom: "4px" }}>
                      {notification.message}
                    </div>
                    <div style={{ fontSize: "12px", color: "#666" }}>
                      {new Date(notification.createdAt).toLocaleString()}
                    </div>
                  </div>
                  {!notification.read && (
                    <div
                      style={{
                        width: "8px",
                        height: "8px",
                        background: "#007bff",
                        borderRadius: "50%",
                        marginTop: "4px"
                      }}
                    />
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}

export default Notifications;
