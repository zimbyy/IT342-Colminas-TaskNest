# Supabase PostgreSQL Configuration Guide

## Complete Setup for TaskNest Backend

### Step 1: Update application.properties

Replace your `src/main/resources/application.properties` with:

```properties
# Supabase PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://db.admtwxejbgacvvhje.supabase.co:5432/postgres?sslmode=require
spring.datasource.username=postgres
spring.datasource.password=Simbran-2005
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Configuration for PostgreSQL
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Connection Pool
spring.datasource.hikari.maximum-pool-size=5
spring.datasource.hikari.minimum-idle=1
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.data-source-properties.sslmode=require
```

### Step 2: Verify User Entity

Your User entity in `src/main/java/edu/cit/colminas/tasknest/model/User.java` should be:

```java
package edu.cit.colminas.tasknest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User(String username, String firstName, String lastName, String password) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
```

### Step 3: Verify Task Entity

Your Task entity in `src/main/java/edu/cit/colminas/tasknest/model/Task.java` should be:

```java
package edu.cit.colminas.tasknest.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime deadline;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Task(User user, String title, String description, LocalDateTime deadline) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
```

### Step 4: Verify AuthService

Your AuthService in `src/main/java/edu/cit/colminas/tasknest/service/AuthService.java`:

```java
package edu.cit.colminas.tasknest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import edu.cit.colminas.tasknest.dto.AuthResponse;
import edu.cit.colminas.tasknest.dto.LoginRequest;
import edu.cit.colminas.tasknest.dto.RegisterRequest;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        // Check if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create and save new user
        User user = new User(
            request.getUsername(),
            request.getFirstName(),
            request.getLastName(),
            passwordEncoder.encode(request.getPassword())
        );
        
        userRepository.save(user);

        return new AuthResponse(
            "Registration successful",
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName()
        );
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new AuthResponse(
            "Login successful",
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}
```

### Step 5: Run Backend

```bash
cd backend/tasknest
mvn clean package -DskipTests
java -jar target/tasknest-0.0.1-SNAPSHOT.jar
```

### Expected Behavior

1. **Registration**: User data is saved to Supabase PostgreSQL `users` table
2. **Login**: Queries check Supabase database for existing users
3. **Task Creation**: Tasks are saved to Supabase PostgreSQL `tasks` table
4. **Persistence**: Data persists after restart (no longer in-memory)

### Troubleshooting

If you get connection errors:
1. Verify your Supabase password is correct
2. Check that SSL verification is enabled (`sslmode=require`)
3. Ensure your network allows outbound connections to `db.admtwxejbgacvvhje.supabase.co:5432`
4. Check Supabase dashboard > Project Settings > Database for the correct credentials

### Connection String Breakdown

```
jdbc:postgresql://db.admtwxejbgacvvhje.supabase.co:5432/postgres?sslmode=require
                  └─ Host                          └─ Port └─ DB   └─ SSL required
```

- **Host**: Your Supabase project reference
- **Username**: `postgres` (default)
- **Password**: `Simbran-2005`
- **Database**: `postgres` (default Supabase database)
- **SSL Mode**: `require` (enforces encrypted connection)
