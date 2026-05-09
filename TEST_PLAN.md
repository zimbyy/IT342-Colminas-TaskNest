# TaskNest Test Plan

## 1. Project Information

**Project Name:** TaskNest  
**Repository:** https://github.com/zimbyy/IT342-Colminas-TaskNest  
**Branch:** refactor/vertical-slice-architecture  
**Date:** May 9, 2026  

## 2. Functional Requirements Coverage

### Implemented Features:

- **User Registration** - New user account creation with validation
- **User Login / Logout** - Authentication with JWT tokens
- **JWT Authentication** - Token-based authentication system
- **Task Creation** - Create new tasks with title, description, and deadline
- **Task Status Update** - Update task status (pending/completed/overdue)
- **Task Deletion** - Remove tasks from the system
- **Task Sorting** - Sort tasks by deadline or status
- **Deadline Notifications** - Automatic notifications for approaching deadlines
- **Profile View and Update** - View and edit user profile information
- **Password Change** - Change user password securely

## 3. Test Cases Table

| Test Case ID | Feature | Description | Preconditions | Input | Expected Output | Pass/Fail Criteria |
|---------------|----------|-------------|----------------|--------|----------------|-------------------|
| TC-001 | User Registration | Create new user account | User not logged in, registration page accessible | Valid username, email, password, firstName, lastName | User account created, redirected to login | Success message shown, user exists in database |
| TC-002 | User Login | Authenticate existing user | User account exists, login page accessible | Valid username/password | JWT token received, redirected to dashboard | Dashboard loads with user data |
| TC-003 | User Logout | End user session | User logged in | Click logout button | JWT token cleared, redirected to login | Login page shows, no authenticated access |
| TC-004 | JWT Authentication | Verify token protection | User has valid JWT token | Access protected endpoint | Request authorized | 200 OK response with data |
| TC-005 | Task Creation | Add new task | User logged in, dashboard accessible | Task title, description, deadline | Task created and displayed | Task appears in task list |
| TC-006 | Task Status Update | Change task status | Task exists, user logged in | New status (pending/completed) | Task status updated | Task shows new status in UI |
| TC-007 | Task Deletion | Remove task | Task exists, user logged in | Delete task action | Task removed from system | Task no longer appears in list |
| TC-008 | Task Sorting by Deadline | Sort tasks by deadline | Multiple tasks exist | Sort by deadline option | Tasks ordered by deadline date | Earliest deadline first |
| TC-009 | Task Sorting by Status | Sort tasks by status | Multiple tasks exist | Sort by status option | Tasks grouped by status | Pending tasks first, then completed |
| TC-010 | Deadline Notifications | Receive deadline alerts | Task with approaching deadline | System checks deadlines | Notification generated | User receives deadline notification |
| TC-011 | Profile View | Display user profile | User logged in | Navigate to profile | Profile data displayed | User information shown correctly |
| TC-012 | Profile Update | Modify user information | User logged in | Updated profile data | Profile updated | New information saved and displayed |
| TC-013 | Password Change | Update user password | User logged in | Current password, new password | Password changed | New password works for login |

## 4. Test Steps

### TC-001: User Registration
1. Navigate to registration page (`/register`)
2. Enter valid username, email, first name, last name, password
3. Click "Register" button
4. Verify success message appears
5. Verify redirect to login page
6. Attempt login with new credentials

### TC-002: User Login
1. Navigate to login page (`/login`)
2. Enter valid username and password
3. Click "Login" button
4. Verify redirect to dashboard
5. Verify user data displayed in dashboard
6. Check JWT token stored in localStorage

### TC-003: User Logout
1. Ensure user is logged in
2. Click logout button
3. Verify redirect to login page
4. Verify localStorage cleared
5. Attempt to access protected route
6. Verify redirect to login

### TC-004: JWT Authentication
1. Login to obtain JWT token
2. Copy token from localStorage
3. Make API request with Authorization header
4. Verify 200 OK response
5. Try request without token
6. Verify 401 Unauthorized response

### TC-005: Task Creation
1. Login to application
2. Navigate to dashboard
3. Click "Add Task" button
4. Enter task title, description, and deadline
5. Submit form
6. Verify task appears in task list
7. Verify task data in database

### TC-006: Task Status Update
1. Login and navigate to dashboard
2. Locate existing task
3. Change task status (pending → completed)
4. Save changes
5. Verify status updated in UI
6. Verify status updated in database

### TC-007: Task Deletion
1. Login and navigate to dashboard
2. Locate existing task
3. Click delete button
4. Confirm deletion
5. Verify task removed from UI
6. Verify task removed from database

### TC-008: Task Sorting by Deadline
1. Login with multiple tasks
2. Navigate to dashboard
3. Select "Sort by Deadline" option
4. Verify tasks ordered by deadline date
5. Verify earliest deadline appears first

### TC-009: Task Sorting by Status
1. Login with multiple tasks
2. Navigate to dashboard
3. Select "Sort by Status" option
4. Verify tasks grouped by status
5. Verify pending tasks appear before completed

### TC-010: Deadline Notifications
1. Create task with deadline within notification window
2. Wait for notification trigger
3. Verify notification generated
4. Verify notification content includes task details
5. Verify notification marked as read when viewed

### TC-011: Profile View
1. Login to application
2. Navigate to profile page (`/profile`)
3. Verify user information displayed
4. Verify all profile fields populated correctly

### TC-012: Profile Update
1. Login and navigate to profile page
2. Modify profile information (email, name)
3. Submit changes
4. Verify success message
5. Verify updated information displayed
6. Verify changes persisted in database

### TC-013: Password Change
1. Login and navigate to profile page
2. Enter current password
3. Enter new password and confirmation
4. Submit password change
5. Verify success message
6. Logout and test new password
7. Verify old password no longer works

## 5. Automated Tests

### Backend Tests
- **Location:** `backend/tasknest/src/test/java/`
- **Framework:** JUnit 5 + Spring Boot Test
- **Coverage:** AuthController, TaskController, NotificationController

### Frontend Tests
- **Location:** `frontend/src/features/*/tests/`
- **Framework:** Vitest + React Testing Library
- **Coverage:** LoginPage, RegisterPage, DashboardPage
