# Vertical Slice Architecture Refactoring Script
# Copies files to new feature slice locations without deleting originals

$ErrorActionPreference = "Stop"

Write-Host "Starting Vertical Slice Refactoring..." -ForegroundColor Green

# Get base paths
$frontendBase = "frontend\src"
$backendBase = "backend\tasknest\src\main\java\edu\cit\colminas\tasknest"

# FRONTEND MAPPINGS
Write-Host "Copying Frontend files..." -ForegroundColor Yellow

# Create frontend directories
New-Item -ItemType Directory -Force -Path "$frontendBase\shared" | Out-Null
New-Item -ItemType Directory -Force -Path "$frontendBase\features\notifications\components" | Out-Null
New-Item -ItemType Directory -Force -Path "$frontendBase\features\authentication\pages" | Out-Null
New-Item -ItemType Directory -Force -Path "$frontendBase\features\dashboard\pages" | Out-Null
New-Item -ItemType Directory -Force -Path "$frontendBase\features\task-management\components" | Out-Null

# Copy frontend files
Copy-Item "$frontendBase\api.js" "$frontendBase\shared\api.js" -Force
Copy-Item "$frontendBase\styles.css" "$frontendBase\shared\styles.css" -Force
Copy-Item "$frontendBase\components\Notifications.jsx" "$frontendBase\features\notifications\components\Notifications.jsx" -Force
Copy-Item "$frontendBase\pages\LoginPage.jsx" "$frontendBase\features\authentication\pages\LoginPage.jsx" -Force
Copy-Item "$frontendBase\pages\RegisterPage.jsx" "$frontendBase\features\authentication\pages\RegisterPage.jsx" -Force
Copy-Item "$frontendBase\pages\ProfilePage.jsx" "$frontendBase\features\authentication\pages\ProfilePage.jsx" -Force
Copy-Item "$frontendBase\pages\DashboardPage.jsx" "$frontendBase\features\dashboard\pages\DashboardPage.jsx" -Force
Copy-Item "$frontendBase\pages\TaskForm.jsx" "$frontendBase\features\task-management\components\TaskForm.jsx" -Force

# BACKEND MAPPINGS
Write-Host "Copying Backend files..." -ForegroundColor Yellow

# Create backend directories
New-Item -ItemType Directory -Force -Path "$backendBase\shared\config" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\shared\security" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\shared\dto" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\authentication\controller" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\authentication\service" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\authentication\dto" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\authentication\model" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\authentication\repository" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\task-management\controller" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\task-management\service" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\task-management\model" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\task-management\repository" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\task-management\factory" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\task-management\observer" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\notifications\controller" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\notifications\service" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\notifications\model" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\notifications\repository" | Out-Null
New-Item -ItemType Directory -Force -Path "$backendBase\features\notifications\dto" | Out-Null

# Copy shared backend files
Copy-Item "$backendBase\config\SecurityConfig.java" "$backendBase\shared\config\SecurityConfig.java" -Force
Copy-Item "$backendBase\config\WebConfig.java" "$backendBase\shared\config\WebConfig.java" -Force
Copy-Item "$backendBase\security\JwtAuthenticationFilter.java" "$backendBase\shared\security\JwtAuthenticationFilter.java" -Force
Copy-Item "$backendBase\dto\ApiResponse.java" "$backendBase\shared\dto\ApiResponse.java" -Force

# Copy authentication feature files
Copy-Item "$backendBase\controller\AuthController.java" "$backendBase\features\authentication\controller\AuthController.java" -Force
Copy-Item "$backendBase\service\AuthService.java" "$backendBase\features\authentication\service\AuthService.java" -Force
Copy-Item "$backendBase\service\SupabaseAuthService.java" "$backendBase\features\authentication\service\SupabaseAuthService.java" -Force
Copy-Item "$backendBase\service\ProfileService.java" "$backendBase\features\authentication\service\ProfileService.java" -Force
Copy-Item "$backendBase\dto\AuthResponse.java" "$backendBase\features\authentication\dto\AuthResponse.java" -Force
Copy-Item "$backendBase\dto\LoginRequest.java" "$backendBase\features\authentication\dto\LoginRequest.java" -Force
Copy-Item "$backendBase\dto\RegisterRequest.java" "$backendBase\features\authentication\dto\RegisterRequest.java" -Force
Copy-Item "$backendBase\dto\PasswordChangeRequest.java" "$backendBase\features\authentication\dto\PasswordChangeRequest.java" -Force
Copy-Item "$backendBase\dto\ProfileUpdateRequest.java" "$backendBase\features\authentication\dto\ProfileUpdateRequest.java" -Force
Copy-Item "$backendBase\model\User.java" "$backendBase\features\authentication\model\User.java" -Force
Copy-Item "$backendBase\repository\UserRepository.java" "$backendBase\features\authentication\repository\UserRepository.java" -Force
Copy-Item "$backendBase\controller\ProfileController.java" "$backendBase\features\authentication\controller\ProfileController.java" -Force

# Copy task-management feature files
Copy-Item "$backendBase\controller\TaskController.java" "$backendBase\features\task-management\controller\TaskController.java" -Force
Copy-Item "$backendBase\service\TaskService.java" "$backendBase\features\task-management\service\TaskService.java" -Force
Copy-Item "$backendBase\model\Task.java" "$backendBase\features\task-management\model\Task.java" -Force
Copy-Item "$backendBase\repository\TaskRepository.java" "$backendBase\features\task-management\repository\TaskRepository.java" -Force
Copy-Item "$backendBase\factory\TaskFactory.java" "$backendBase\features\task-management\factory\TaskFactory.java" -Force
Copy-Item "$backendBase\observer\DeadlineNotifier.java" "$backendBase\features\task-management\observer\DeadlineNotifier.java" -Force
Copy-Item "$backendBase\observer\Observer.java" "$backendBase\features\task-management\observer\Observer.java" -Force
Copy-Item "$backendBase\observer\Subject.java" "$backendBase\features\task-management\observer\Subject.java" -Force

# Copy notifications feature files
Copy-Item "$backendBase\controller\NotificationController.java" "$backendBase\features\notifications\controller\NotificationController.java" -Force
Copy-Item "$backendBase\service\NotificationService.java" "$backendBase\features\notifications\service\NotificationService.java" -Force
Copy-Item "$backendBase\model\Notification.java" "$backendBase\features\notifications\model\Notification.java" -Force
Copy-Item "$backendBase\repository\NotificationRepository.java" "$backendBase\features\notifications\repository\NotificationRepository.java" -Force
Copy-Item "$backendBase\dto\NotificationRequest.java" "$backendBase\features\notifications\dto\NotificationRequest.java" -Force

Write-Host "Vertical Slice Refactoring completed successfully!" -ForegroundColor Green
Write-Host "Files copied to new locations. Original files preserved." -ForegroundColor Cyan
Write-Host "Next step: Update package declarations and imports." -ForegroundColor Cyan
