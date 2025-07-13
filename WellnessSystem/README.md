# Wellness System - Staff Management

A comprehensive desktop application for managing wellness services with role-based staff management, built using Java Swing and JavaDB.

## 🏗️ Architecture Overview

This project demonstrates **Object-Oriented Programming** principles and **MVC Architecture**:

- **Model**: Staff classes, data access objects, and business logic
- **View**: Java Swing GUI components
- **Controller**: Event handlers and application flow

## 🎯 Core Features

### 1. Staff Management System
- **Base Staff Class**: Abstract class with common fields (ID, name, password, role)
- **Three Staff Types**:
  - **Admin**: Full system access and permissions
  - **Receptionist**: Limited to appointment booking and schedule viewing
  - **Counselor**: Can view personal schedule and feedback

### 2. Role-Based Permissions
Each staff type has specific permissions:

| Permission | Admin | Receptionist | Counselor |
|------------|-------|--------------|-----------|
| Manage Appointments | ✓ | ✗ | ✗ |
| Manage Counselors | ✓ | ✗ | ✗ |
| Manage Feedback | ✓ | ✗ | ✗ |
| View All Data | ✓ | ✗ | ✗ |
| Book Appointments | ✓ | ✓ | ✗ |
| View Schedule | ✓ | ✓ | ✓ |

### 3. Database Integration
- **JavaDB (Apache Derby)** for data persistence
- **CRUD Operations** for all staff members
- **Authentication system** with password validation
- **Role-based queries** and filtering

## 🏛️ OOP Principles Demonstrated

### 1. Inheritance
```java
public abstract class Staff {
    // Base class with common fields and methods
}

public class Admin extends Staff {
    // Admin-specific implementation
}

public class Receptionist extends Staff {
    // Receptionist-specific implementation
}

public class CounselorStaff extends Staff {
    // Counselor-specific implementation with specialization
}
```

### 2. Polymorphism
- Staff objects can be treated uniformly through the base class
- Different staff types respond differently to permission checks
- Collections can store different staff types together

### 3. Encapsulation
- Private fields with public getters/setters
- Data validation in constructors and methods
- Protected access for inheritance

### 4. Abstraction
- Abstract methods for role-based permissions
- Interface-like behavior through abstract base class
- Separation of concerns between staff types

## 📁 Project Structure

```
src/
├── wellnesssystem/
│   ├── model/
│   │   ├── Staff.java              # Abstract base class
│   │   ├── Admin.java              # Admin subclass
│   │   ├── Receptionist.java       # Receptionist subclass
│   │   ├── CounselorStaff.java     # Counselor subclass
│   │   ├── StaffManager.java       # Staff management logic
│   │   └── StaffDAO.java           # Database operations
│   ├── view/
│   │   ├── LoginForm.java          # Staff authentication GUI
│   │   └── RoleBasedDashboard.java # Role-based dashboard
│   └── WellnessSystem.java         # Main application entry
├── db/
│   ├── DBConnection.java           # Enhanced database connection
│   ├── DatabaseManager.java        # Connection management
│   ├── DatabaseCleanup.java        # Derby restart fix utility
│   ├── DatabaseSetup.java          # Complete database setup
│   ├── CreateTables.java           # Database schema
│   └── InsertDummyData.java        # Sample data insertion
└── lib/
    ├── derby.jar                   # Apache Derby core
    ├── derbyshared.jar             # Derby shared components
    └── derbytools.jar              # Derby tools
```

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Apache Derby (included in lib/ directory)
- NetBeans IDE (recommended)

### Installation
1. Clone the repository
2. **Option A: Using the Launcher Scripts (Recommended)**
   - **Windows**: Double-click `run_wellness_system.bat`
   - **PowerShell**: Right-click `run_wellness_system.ps1` → "Run with PowerShell"
   
3. **Option B: Manual Command Line**
   ```bash
   # Compile and run
   javac -cp "src/lib/*;src" src/wellnesssystem/WellnessSystem.java
   java -cp "src/lib/*;src" wellnesssystem.WellnessSystem
   ```
   
4. **Option C: NetBeans IDE**
   - Open the project in NetBeans
   - **Add Derby libraries to classpath:**
     - Right-click project → Properties → Libraries
     - Add JAR/Folder → Select `src/lib/derby.jar`
     - Repeat for `derbyshared.jar` and `derbytools.jar`
   - Run `WellnessSystem.java`

5. **Database Setup**: The application will automatically set up the database on first run
6. Login with sample credentials to test the system

### Sample Staff Credentials
```
Admin: ID=100001, Password=admin123
Receptionist: ID=200001, Password=reception789
Counselor: ID=300001, Password=counselor123
```

## 🧪 Testing the System

### 1. Database Setup
1. Run `CreateTables.java` to create all database tables
2. Run `InsertSampleData.java` to populate with sample data
3. The database will be created in the project directory as `wellnessDB`

### 2. Application Testing
1. Run `WellnessSystem.java` to start the application
2. Login with sample credentials:
   - **Admin**: ID `100001`, Password `admin123`
   - **Receptionist**: ID `200001`, Password `reception789`
   - **Counselor**: ID `300001`, Password `counselor123`

### 3. Interface Testing
- **Appointments Management**: Add, edit, delete appointments
- **Staff Management**: Manage all staff members (Admin only)
- **Feedback Management**: Manage feedback with role-based access
- **Role-Based Dashboard**: Different features based on user role

### 4. Database Operations
The system includes:
- Full CRUD operations for all entities
- Role-based data access
- Real-time database connectivity
- Input validation and error handling

## 🔧 Key Classes Explained

### Staff.java (Abstract Base Class)
- Defines common fields: `id`, `name`, `password`, `role`
- Abstract methods for role-based permissions
- Validation methods for ID and password
- Demonstrates abstraction and inheritance

### StaffManager.java
- Manages collections of staff members
- Demonstrates polymorphism with `List<Staff>`
- Provides role-based filtering and queries
- Implements exception handling

### StaffDAO.java
- Handles all database operations
- Uses prepared statements for security
- Demonstrates factory pattern for staff creation
- Implements CRUD operations with JavaDB

### LoginForm.java
- Java Swing GUI for authentication
- Input validation and error handling
- Role-based access control demonstration
- User-friendly interface with sample credentials

## 🎓 Educational Value

This project demonstrates:

1. **Core Java & OOP**
   - Inheritance hierarchies
   - Polymorphism with collections
   - Encapsulation and abstraction
   - Exception handling

2. **GUI with Swing**
   - Event-driven programming
   - Layout managers
   - Input validation
   - User feedback

3. **Database Operations**
   - JDBC with JavaDB
   - Prepared statements
   - CRUD operations
   - Data validation

4. **MVC Architecture**
   - Separation of concerns
   - Model-View-Controller pattern
   - Data access objects
   - Business logic separation

## 🔒 Security Features

- **Password validation** (minimum 6 characters)
- **ID validation** (6-digit requirement)
- **SQL injection prevention** (prepared statements)
- **Role-based access control**
- **Input sanitization**

## 🔧 Database Troubleshooting

### Derby Restart Issues
If you encounter database connection errors when restarting the application, follow these steps:

#### Quick Fix
1. **Run Database Cleanup:**
   ```bash
   java -cp "src/lib/*;src" db.DatabaseCleanup
   ```

2. **If that doesn't work, delete the database folder:**
   ```bash
   # On Windows
   rmdir /s wellnessDB
   
   # On Mac/Linux
   rm -rf wellnessDB
   ```

3. **Recreate the database:**
   ```bash
   java -cp "src/lib/*;src" db.DatabaseSetup
   ```

#### Prevention
The application now includes:
- **Automatic Derby shutdown** when the application closes
- **Connection retry logic** for restart issues
- **Proper connection management** to prevent lingering connections
- **Shutdown hooks** for clean database cleanup

### Common Error Messages
- **"Database 'wellnessDB' not found"**: Run `DatabaseCleanup` or delete the `wellnessDB` folder
- **"Derby system shutdown"**: Normal message, the database is shutting down properly
- **"Connection refused"**: Database is locked, run cleanup utility
- **"NumberFormatException: Error at index 2 in: '00:00'"**: Time parsing issue (now fixed with robust time handling)
- **"Could not load any Derby driver"**: Classpath issue - use the launcher scripts instead of manual commands
- **"ClassNotFoundException: org.apache.derby.jdbc.EmbeddedDriver"**: JAR files not in classpath - use launcher scripts

### Database Management Utilities
- **`DatabaseCleanup.java`**: Fixes restart issues
- **`DatabaseSetup.java`**: Creates tables and sample data
- **`DatabaseManager.java`**: Manages connections properly

## 🚧 Future Enhancements

## 📝 License

This project is created for educational purposes to demonstrate Java OOP concepts and desktop application development.

---

**Note**: This system is designed as a learning tool and should be enhanced with proper security measures before production use. 