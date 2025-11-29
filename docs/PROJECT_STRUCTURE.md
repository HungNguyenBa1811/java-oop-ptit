# Project Structure - Course Registration System (JavaFX + MySQL)

## 📁 Directory Layout

```
java-oop-ptit/
│
├── src/
│   └── main/
│       ├── java/                          # Source code
│       │   ├── Main.java                  # Application entry point
│       │   ├── config/
│       │   │   └── DatabaseConnection.java    # Database connection management
│       │   │
│       │   ├── model/                    # Entity classes (POJO) - Database tables
│       │   │   ├── User.java             # Base user entity
│       │   │   ├── Admin.java            # Admin entity (extends User)
│       │   │   ├── Student.java          # Student entity (extends User)
│       │   │   ├── Faculty.java          # Faculty entity
│       │   │   ├── Major.java            # Major/Specialization entity
│       │   │   ├── Course.java           # Course entity
│       │   │   ├── CourseOffering.java   # Course offering (lớp học phần)
│       │   │   ├── CourseOfferingSchedule.java # Course offering schedule link
│       │   │   ├── Schedule.java         # Schedule entity
│       │   │   ├── Semester.java         # Semester entity
│       │   │   ├── Registration.java     # Student course registration
│       │   │   └── Room.java             # Classroom entity
│       │   │
│       │   ├── repository/               # Data Access Layer (CRUD operations)
│       │   │   ├── UserRepository.java
│       │   │   ├── StudentRepository.java
│       │   │   ├── CourseRepository.java
│       │   │   ├── CourseOfferingRepository.java
│       │   │   ├── CourseOfferingScheduleRepository.java
│       │   │   ├── ScheduleRepository.java
│       │   │   ├── SemesterRepository.java
│       │   │   ├── RegistrationRepository.java
│       │   │   ├── RoomRepository.java
│       │   │   ├── FacultyRepository.java
│       │   │   └── MajorRepository.java
│       │   │
│       │   ├── service/                  # Business Logic Layer (Interfaces)
│       │   │   ├── UserService.java
│       │   │   ├── AdminService.java
│       │   │   ├── StudentService.java
│       │   │   ├── AuthService.java
│       │   │   ├── CourseService.java
│       │   │   ├── CourseOfferingService.java
│       │   │   ├── CourseOfferingScheduleService.java
│       │   │   ├── RegistrationService.java
│       │   │   ├── ScheduleService.java
│       │   │   └── SemesterService.java
│       │   │
│       │   ├── service/impl/             # Business Logic Implementation
│       │   │   ├── UserServiceImpl.java
│       │   │   ├── AdminServiceImpl.java
│       │   │   ├── StudentServiceImpl.java
│       │   │   ├── AuthServiceImpl.java
│       │   │   ├── CourseServiceImpl.java
│       │   │   ├── CourseOfferingServiceImpl.java  # Includes schedule conflict check
│       │   │   ├── CourseOfferingScheduleServiceImpl.java
│       │   │   ├── RegistrationServiceImpl.java     # Includes duplicate registration check
│       │   │   ├── ScheduleServiceImpl.java
│       │   │   ├── SemesterServiceImpl.java
│       │   │   ├── RoomServiceImpl.java
│       │   │   ├── FacultyServiceImpl.java
│       │   │   └── MajorServiceImpl.java
│       │   │
│       │   ├── controller/               # UI Controllers (JavaFX)
│       │   │   ├── LoginController.java           # Login screen
│       │   │   ├── DashboardController.java       # Dashboard routing
│       │   │   ├── admin/
│       │   │   │   ├── AdminController.java
│       │   │   │   ├── courseOffering/
│       │   │   │   │   ├── CourseOfferingController.java
│       │   │   │   │   └── CreateCourseOfferingFormController.java
│       │   │   │   ├── course/
│       │   │   │   ├── student/
│       │   │   │   └── schedule/
│       │   │   ├── student/
│       │   │   │   ├── StudentController.java
│       │   │   │   └── RegistrationController.java
│       │   │   └── navigation/
│       │   │       └── NavigationManager.java     # Screen navigation
│       │   │
│       │   ├── utils/                   # Utility classes
│       │   │   ├── FXUtils.java         # JavaFX utility methods
│       │   │   ├── GenericUtils.java    # Generic utility methods
│       │   │   ├── DateUtils.java       # Date formatting utilities
│       │   │   └── ValidationUtils.java # Input validation
│       │   │
│       │   ├── dto/                     # Data Transfer Objects
│       │   │   └── admin/
│       │   │       └── courseOffering/
│       │   │           └── ScheduleRow.java  # UI representation of Schedule
│       │   │
│       │   ├── view/                    # View management
│       │   │   ├── AppView.java         # Main application view
│       │   │   └── NavigationManager.java
│       │   │
│       │   └── test/                    # Unit tests
│       │       ├── DBTest.java
│       │       ├── AdminLoginTest.java
│       │       ├── CourseOfferingScheduleTest.java
│       │       ├── ScheduleServiceTest.java
│       │       ├── ScheduleFormatTest.java
│       │       └── DotenvTest.java
│       │
│       └── resources/                   # Resources and UI layouts
│           ├── assets/
│           │   ├── fonts/               # Custom fonts
│           │   └── images/              # Application images/logos
│           ├── css/
│           │   └── style.css            # Global stylesheet (button colors, tables, etc.)
│           └── fxml/                    # JavaFX UI layouts
│               ├── login.fxml           # Login screen
│               ├── admindashboard.fxml  # Admin dashboard
│               ├── studentdashboard.fxml # Student dashboard
│               └── sampledashboard.fxml
│
├── docs/                                # Documentation
│   ├── PROJECT_STRUCTURE.md             # This file
│   ├── mysql_schema.sql                 # MySQL database schema
│   ├── sql_server_schema.sql            # SQL Server schema (alternative)
│   ├── insert_data.sql                  # Sample data
│   ├── dbml.md                          # Database DBML diagram
│   ├── CourseOfferingRegistration.vpp   # Visual paradigm diagram
│   └── SCHEDULE_CONFLICT_CHECK.md       # Schedule conflict checking documentation
│
├── .env                                 # Environment variables (not in git)
├── .gitignore                           # Git ignore rules
├── .github/                             # GitHub workflows
├── .vscode/                             # VS Code settings
├── LICENSE                              # Project license
├── README.md                            # Project README
└── SCHEDULE_CONFLICT_CHECK.md           # Schedule conflict feature documentation
```



## 🏗️ Layer Architecture

### 1️⃣ Model Layer (`model/`)
**Purpose**: Data representation matching database tables

**Characteristics**:
- Plain Old Java Objects (POJO)
- Only data and getters/setters
- No business logic
- No database operations
- Serializable

**Key Classes**:
- `User.java` - Base class with id, username, password, fullName, email, role
- `Student.java` - Extends User, adds studentId, class, majorId, status
- `Admin.java` - Extends User for admin users
- `Course.java` - courseId, courseName, credits, facultyId
- `CourseOffering.java` - courseOfferingId, courseId, facultyId, roomId, semesterId, instructor, capacity, **schedules list**
- `Schedule.java` - scheduleId, dayOfWeek, startTime, endTime (with LocalTime)
- `Registration.java` - registrationId, studentId, courseOfferingId, registrationDate

### 2️⃣ Repository Layer (`repository/`)
**Purpose**: Data Access Object (CRUD) operations

**Characteristics**:
- SQL query execution
- Connection management
- ResultSet mapping to models
- One repository per model
- Static SQL queries

**Key Classes**:
- `CourseOfferingRepository.java`
  - `createCourseOffering()` - Insert new course offering
  - `findById()`, `findByCourse()`, `findBySemester()`, `findByRoom()`
  - `updateCourseOffering()` - Update offering details
  - `checkScheduleConflict()` - **CHECK FOR SCHEDULE CONFLICTS** ⭐
    - Query: Join course_offerings, course_offerings_schedules, schedules
    - Logic: `TIME(start_time) < newEndTime AND TIME(end_time) > newStartTime`
    - Returns: true if conflict found, false otherwise

- `ScheduleRepository.java`
  - `findById()`, `findAll()`
  - `getSchedulesByDayOfWeek()`

- `RegistrationRepository.java`
  - `createRegistration()` - Register student
  - `findByStudent()` - Get student's registrations
  - `cancelRegistration()` - Remove registration

### 3️⃣ Service Layer (`service/` + `service/impl/`)
**Purpose**: Business logic and validation

**Characteristics**:
- Interface-based design
- Input validation
- Business rule enforcement
- Transaction management
- Exception handling
- Calls repositories

**Key Classes**:

**CourseOfferingServiceImpl.java** - Create/Update/Delete course offerings
```java
createCourseOffering(CourseOffering, courseId, semesterId, roomId)
├─ Validate all inputs
├─ FOR EACH schedule in offering.getSchedules():
│  ├─ Check schedule validity
│  └─ Call repository.checkScheduleConflict()
│     └─ IF conflict found → THROW IllegalArgumentException ❌
├─ Create CourseOffering in database
├─ Create CourseOfferingSchedule links
└─ Return CourseOffering object
```

**RegistrationServiceImpl.java** - Handle student registrations
```java
registerCourse(studentId, courseOfferingId)
├─ Validate inputs
├─ Get all student's registrations
├─ FOR EACH registration:
│  └─ IF courseId matches → THROW "Already registered" ❌
├─ Create new registration
└─ Increment course offering capacity
```

**AdminServiceImpl.java** - Admin operations
```java
createCourseOffering(offering, courseId, semesterId, roomId)
└─ Delegates to CourseOfferingService
   └─ (Which performs conflict check)
```

### 4️⃣ Controller Layer (`controller/`)
**Purpose**: JavaFX UI event handling and screen navigation

**Characteristics**:
- JavaFX FXML controller
- UI event listeners
- Input validation before service call
- Error display to user
- Screen navigation

**Key Classes**:

**CreateCourseOfferingFormController.java** ⭐ IMPORTANT
```
User fills form:
- Offering ID, Course, Semester, Room
- Select schedules from list

Click Save:
├─ validateForm() - Check all fields filled
├─ buildCourseOffering() - Create offering object
├─ FOR EACH selected schedule:
│  └─ Get Schedule from DB via scheduleService.getScheduleById()
├─ offering.setSchedules(scheduleList) - ⭐ SET SCHEDULES HERE
├─ adminService.createCourseOffering(offering, ...)
│  └─ Service checks conflicts!
│     └─ IF conflict → CATCH exception & showError()
│     └─ IF OK → Success message
└─ Close form
```

**RegistrationController.java**
- Handle student course registration
- Display available course offerings
- Validate before submitting

**LoginController.java**
- Handle login form
- Authenticate user
- Route to appropriate dashboard

### 5️⃣ Utility Layer (`utils/`)
**Purpose**: Helper functions and common operations

**Key Classes**:
- `FXUtils.java` - JavaFX operations (showError, showSuccess, closeWindow)
- `GenericUtils.java` - Generic utilities (isBlank, parsing)
- `DateUtils.java` - Date formatting (format times, compare dates)
- `DatabaseConnection.java` - Singleton pattern for database connections

### 6️⃣ DTO Layer (`dto/`)
**Purpose**: Data Transfer Objects for UI display

**Key Classes**:
- `ScheduleRow.java` - JavaFX property wrapper for Schedule display
  - Contains scheduleId and display text
  - Used in ListViews in UI

---

## 🔄 Data Flow Diagrams

### User Registration Flow
```
UI Form → Controller.validateForm()
    ↓
→ adminService.registerStudent()
    ↓
→ studentService.createStudent() + userService.createUser()
    ↓
→ studentRepository.createStudent() + userRepository.createUser()
    ↓
→ INSERT INTO students / users
    ↓
→ Return Student object
    ↓
→ Controller shows success message
```

### Course Offering Creation (WITH CONFLICT CHECK)
```
UI Form (Select Schedules)
    ↓
Controller.handleSave()
├─ Get selected ScheduleRows
├─ Load full Schedule objects from DB
├─ offering.setSchedules(scheduleList) ⭐
    ↓
→ adminService.createCourseOffering()
    ↓
→ courseOfferingService.createCourseOffering()
    ├─ FOR EACH schedule in offering.getSchedules():
    │  ├─ Get dayOfWeek, startTime, endTime
    │  └─ repository.checkScheduleConflict(roomId, semesterId, dayOfWeek, startTime, endTime)
    │     └─ SQL: SELECT COUNT(*) WHERE room matches AND time overlaps
    │        └─ IF count > 0 → throw Exception ❌
    ├─ repository.createCourseOffering() - Insert offering
    ├─ FOR EACH schedule:
    │  └─ repository.createCourseOfferingSchedule() - Link schedule
    └─ return CourseOffering
    ↓
Controller.handleSave()
    ├─ IF success → showSuccess() + closeForm()
    └─ CATCH exception → showError("Phòng học bị trùng lịch...")
```

### Student Course Registration
```
UI Dashboard → Student clicks Register
    ↓
→ registrationService.registerCourse(studentId, courseOfferingId)
    ├─ Get all student's registrations
    ├─ Check if courseId already registered
    │  └─ IF YES → throw "Đã đăng ký môn này rồi" ❌
    ├─ Create new registration
    ├─ Increment course offering capacity
    └─ Return registration
    ↓
→ Controller displays success/error
```

---

## 🗄️ Database Schema

### Key Tables:
```sql
users (user_id, username, password, full_name, email, role)
students (student_id FK→users, class, major_id, status)
admins (user_id FK→users)
courses (course_id, course_name, credits, faculty_id)
rooms (room_id, capacity, projector, airconditioner)
semesters (semester_id, term, academic_year, start_date, end_date)
schedules (schedule_id, day_of_week, start_time, end_time)

course_offerings (
  course_offering_id, 
  course_id FK, 
  faculty_id FK, 
  room_id FK,
  semester_id FK,
  instructor,
  max_capacity,
  current_capacity
)

course_offerings_schedules (
  id,
  course_offering_id FK,
  schedule_id FK,
  start_date,
  end_date
)

registrations (
  registration_id,
  student_id FK,
  course_offering_id FK,
  registration_date
)
```

### Key Relationships:
```
Users (1) ─┬─ (M) Students
           └─ (M) Admins

Courses (1) ─ (M) CourseOfferings
Faculties (1) ─ (M) Courses
Faculties (1) ─ (M) CourseOfferings

Rooms (1) ─ (M) CourseOfferings

Semesters (1) ─ (M) CourseOfferings

Schedules (M) ─ (M) CourseOfferings
               via CourseOfferingSchedules

CourseOfferings (1) ─ (M) Registrations
Students (1) ─ (M) Registrations
```

---

## 🎨 UI/View Layer

### FXML Files:
- **login.fxml** - Login screen
  - Username/password input
  - Login button (red background, white text)
  - Logo display

- **admindashboard.fxml** - Admin dashboard
  - Navigation menu
  - Course offerings management section
  - Create course offering button → Opens form

- **studentdashboard.fxml** - Student dashboard
  - Table showing registered courses
  - Columns: Mã HP, Mã MH, Tên môn học, Số tín chỉ, Giảng viên, Học kỳ, Lịch học, Phòng, Sĩ số, Sĩ số còn lại
  - All columns optimized for 1366px width without horizontal scrolling

- **sampledashboard.fxml** - Sample/template dashboard

### Styling (css/style.css):
- Login button: Red background (#a3151a), white text
- Logout button: White background, red text (#a3151a)
- Table headers: Red background, white bold text
- Table rows: Center-aligned, padding 18px top, 8px bottom
- Color scheme: #a3151a (primary red), white, #F8F9FA (background)

---

## 🔍 Key Features Implementation

### ✅ Schedule Conflict Detection
**Where**: `CourseOfferingRepository.checkScheduleConflict()`
**When**: `CourseOfferingServiceImpl.createCourseOffering()` - FOR EACH schedule
**Check**: Same room + Same semester + Same day + Time overlap
**Result**: 
- Conflict found → ❌ IllegalArgumentException thrown
- No conflict → ✅ Continue with creation

**SQL Query**:
```sql
SELECT COUNT(*) FROM course_offerings co
  INNER JOIN course_offerings_schedules cos ON co.course_offering_id = cos.course_offering_id
  INNER JOIN schedules s ON cos.schedule_id = s.schedule_id
WHERE co.room_id = ?
  AND co.semester_id = ?
  AND s.day_of_week = ?
  AND TIME(s.start_time) < TIME(?) -- newEndTime
  AND TIME(s.end_time) > TIME(?)   -- newStartTime
```

### ✅ Duplicate Course Registration Prevention
**Where**: `RegistrationServiceImpl.registerCourse()`
**When**: Before creating registration
**Check**: Student already registered for same course
**Result**:
- Already registered → ❌ Exception: "Đã đăng ký môn này rồi"
- New course → ✅ Continue with registration

### ✅ Form Validation
**Where**: `CreateCourseOfferingFormController.validateForm()`
**Checks**:
- All fields filled (Offering ID, Course, Semester, Room, Max Capacity)
- Max Capacity is positive integer
- At least one schedule selected
- Current Capacity is valid number

---

## 🚀 Technology Stack

**Backend**:
- Java 21
- MySQL 8.0
- JDBC for database connection

**Frontend**:
- JavaFX 25
- FXML for UI layouts
- CSS for styling

**Build System**: None (Pure Java - no Maven/Gradle)

**Testing**: JUnit (basic tests in test/ folder)

---

## 📋 Naming Conventions

### Java Classes:
- **Model**: `User`, `Student`, `Course` (noun, singular)
- **Repository**: `UserRepository`, `CourseOfferingRepository` (Repository suffix)
- **Service Interface**: `UserService`, `CourseOfferingService` (Service suffix)
- **Service Implementation**: `UserServiceImpl`, `CourseOfferingServiceImpl` (Impl suffix)
- **Controller**: `LoginController`, `StudentController` (Controller suffix)
- **DTO**: `ScheduleRow` (Row/DTO suffix for UI objects)

### Methods:
- **Repository**: `create()`, `findById()`, `findAll()`, `update()`, `delete()`, `checkScheduleConflict()`
- **Service**: `registerUser()`, `validateEmail()`, `createCourseOffering()`, `registerCourse()`
- **Controller**: `handleSave()`, `handleCancel()`, `initialize()`, `loadOptionData()`

### Variables:
- camelCase: `userId`, `courseOfferingId`, `maxCapacity`
- Constants: `CONFLICT_ERROR_MESSAGE`, `MAX_CAPACITY`

---

## 🔗 Important Method Signatures

### Course Offering Creation:
```java
// Service Layer - Main entry point
courseOfferingService.createCourseOffering(
    CourseOffering offering,  // Must have schedules set!
    String courseId,
    String semesterId,
    String roomId
)

// Repository Layer - Conflict check
courseOfferingRepository.checkScheduleConflict(
    String roomId,
    String semesterId,
    int dayOfWeek,
    String startTime,      // HH:mm:ss
    String endTime         // HH:mm:ss
) → boolean
```

### Student Registration:
```java
// Service Layer
registrationService.registerCourse(
    String studentId,
    String courseOfferingId
) → Registration

// Repository Layer
registrationRepository.findByStudent(
    String studentId
) → List<Registration>
```

---

## 📝 Best Practices Implemented

1. ✅ **Single Responsibility** - Each class has one purpose
2. ✅ **Interface-based Services** - Easy to mock for testing
3. ✅ **Separation of Concerns** - Clear layer boundaries
4. ✅ **Validation at Service Layer** - Business rules enforced
5. ✅ **Exception Handling** - Meaningful error messages
6. ✅ **Resource Management** - Try-with-resources for DB connections
7. ✅ **DRY Principle** - Reusable utilities and base classes
8. ✅ **Consistent Naming** - Predictable class and method names
9. ✅ **Documentation** - Comments on complex logic
10. ✅ **Error User Feedback** - User-friendly error messages in UI

---

## 🎯 Key Files to Remember

**For Schedule Conflict Checking**:
- `repository/CourseOfferingRepository.java` - `checkScheduleConflict()` method
- `service/impl/CourseOfferingServiceImpl.java` - Calls conflict check in `createCourseOffering()`
- `controller/admin/courseOffering/CreateCourseOfferingFormController.java` - Sets schedules before calling service

**For Duplicate Registration**:
- `service/impl/RegistrationServiceImpl.java` - `registerCourse()` checks duplicates

**For UI Styling**:
- `resources/css/style.css` - Global styles for buttons, tables
- `resources/fxml/studentdashboard.fxml` - Table column widths
- `resources/fxml/login.fxml` - Login screen layout

**For Database**:
- `docs/mysql_schema.sql` - Database schema
- `docs/insert_data.sql` - Sample data
- `config/DatabaseConnection.java` - DB connection management

