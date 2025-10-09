# Project Structure

## Directory Layout

```
java-oop-ptit/
│
├── src/
│   ├── main/
│   │   ├── java/                  # Source code
│   │   │   ├── model/            # Entity classes (POJO)
│   │   │   ├── dao/              # Data Access Object (Repository)
│   │   │   ├── service/          # Business Logic layer
│   │   │   ├── controller/       # Request handling
│   │   │   ├── util/             # Utilities
│   │   │   ├── exception/        # Custom exceptions
│   │   │   └── Main.java         # Entry point
│   │   │
│   │   └── resources/            # Resources
│   │       ├── config/           # Configuration files
│   │       │   └── database.properties
│   │       └── sql/              # SQL scripts
│   │           ├── schema.sql
│   │           └── sample_data.sql
│   │
│   └── test/                     # Unit tests
│       └── java/
│
├── lib/                          # External libraries
│   └── mysql-connector-java-x.x.x.jar
│
├── docs/                         # Documentation
│   ├── PROJECT_STRUCTURE.md
│   ├── ClassDiagram.png
│   └── class diagram.vpp
│
├── .gitignore
├── LICENSE
└── README.md
```

## Layer Responsibilities

### Model Layer (`model/`)
- Entity classes representing database tables
- Plain Old Java Objects (POJO)
- Only contains data and getters/setters
- No business logic
- No database logic

**Files:**
- `User.java` - Base class for users
- `Student.java` - Extends User
- `Admin.java` - Extends User
- `Course.java` - Course entity
- `CourseOffering.java` - Course offering entity
- `Registration.java` - Registration entity
- `Major.java` - Major entity
- `Faculty.java` - Faculty entity
- `Schedule.java` - Schedule entity
- `Semester.java` - Semester entity

### DAO Layer (`dao/`)
- Data Access Object pattern
- Database operations (CRUD)
- SQL queries
- Map ResultSet to Entity objects
- Generic BaseDAO to avoid code duplication

**Files:**
- `BaseDAO.java` - Generic repository base class
- `UserDAO.java` - User database operations
- `StudentDAO.java` - Student database operations
- `CourseDAO.java` - Course database operations
- `CourseOfferingDAO.java` - Course offering database operations
- `RegistrationDAO.java` - Registration database operations
- etc.

### Service Layer (`service/`)
- Business logic
- Validation rules
- Transaction management
- Calls multiple DAOs if needed
- Exception handling

**Files:**
- `UserService.java` - User business logic
- `StudentService.java` - Student business logic
- `CourseService.java` - Course business logic
- `RegistrationService.java` - Registration business logic
- `AuthService.java` - Authentication logic

### Controller Layer (`controller/`)
- Request handling
- Input validation
- Calls service layer
- Response formatting
- Error handling

**Files:**
- `UserController.java` - User request handler
- `StudentController.java` - Student request handler
- `AdminController.java` - Admin request handler
- `CourseController.java` - Course request handler
- `RegistrationController.java` - Registration request handler

### Utility Layer (`util/`)
- Common utilities
- Helper functions
- Database connection management

**Files:**
- `DatabaseConnection.java` - Singleton database connection
- `Validator.java` - Input validation utilities
- `DateUtils.java` - Date/time utilities

### Exception Layer (`exception/`)
- Custom exception classes
- Specific error handling

**Files:**
- `DatabaseException.java` - Database errors
- `ValidationException.java` - Validation errors
- `BusinessException.java` - Business logic errors

## Data Flow

```
Client/UI Request
    ↓
Controller (validate input)
    ↓
Service (business logic)
    ↓
DAO (database operations)
    ↓
Database
    ↓
DAO (map to Entity)
    ↓
Service (process)
    ↓
Controller (format response)
    ↓
Client/UI Response
```

## Naming Conventions

### Classes
- **Model**: `User`, `Student`, `Course` (singular, noun)
- **DAO**: `UserDAO`, `StudentDAO` (DAO suffix)
- **Service**: `UserService`, `RegistrationService` (Service suffix)
- **Controller**: `UserController`, `AdminController` (Controller suffix)

### Methods
- **DAO**: `insert()`, `update()`, `delete()`, `findById()`, `findAll()`
- **Service**: `registerUser()`, `validateEmail()`, `processRegistration()`
- **Controller**: `handleRequest()`, `register()`, `login()`

### Variables
- camelCase: `userId`, `courseName`, `maxCapacity`
- Constants: `MAX_CAPACITY`, `DEFAULT_ROLE`

## Best Practices

1. **Single Responsibility**: Each class has one responsibility
2. **DRY**: Don't Repeat Yourself - use BaseDAO
3. **Separation of Concerns**: Keep layers separate
4. **Immutability**: Models are immutable where possible
5. **Exception Handling**: Use custom exceptions
6. **Validation**: Validate at service layer
7. **Documentation**: Comment complex logic
8. **Testing**: Write unit tests for service layer
