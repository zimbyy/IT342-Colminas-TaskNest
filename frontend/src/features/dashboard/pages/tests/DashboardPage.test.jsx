import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render, screen } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import DashboardPage from "../DashboardPage.jsx";

vi.mock("../../../../shared/api.js", () => ({
  fetchTasks: vi.fn(() => Promise.resolve([])),
  deleteTask: vi.fn(() => Promise.resolve()),
  updateTask: vi.fn(() => Promise.resolve()),
  fetchNotifications: vi.fn(() => Promise.resolve([])),
}));

vi.mock("react-router-dom", async (importOriginal) => {
  const actual = await importOriginal();
  return { ...actual, useNavigate: () => vi.fn() };
});

const mockUser = { id: 1, userId: 1, username: "testuser" };

beforeEach(() => {
  localStorage.setItem("tasknestUser", JSON.stringify(mockUser));
  localStorage.setItem("tasknestToken", "fake-token");
});

afterEach(() => {
  localStorage.clear();
});

describe("DashboardPage", () => {
  it("renders dashboard heading", () => {
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);
    expect(screen.getByText(/TaskNest/i)).toBeTruthy();
  });

  it("renders the logout button", () => {
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);
    expect(screen.getByText(/Logout/i)).toBeTruthy();
  });

  it("renders the create task button", () => {
    render(<MemoryRouter><DashboardPage /></MemoryRouter>);
    expect(screen.getByRole('button', { name: /Create Task/i })).toBeTruthy();
  });
});
