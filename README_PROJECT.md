# Course Registration System

Hệ thống đăng ký môn học cho sinh viên, được phát triển bằng Java thuần với kiến trúc MVC (Model-DAO-Service-Controller).

## 📋 Tính năng

### Student
- Đăng ký môn học
- Xem danh sách môn học đã đăng ký
- Hủy đăng ký môn học
- Xem lịch học

### Admin
- Quản lý Course Offering (CRUD)
- Đăng ký môn học cho sinh viên
- Xem danh sách sinh viên đã đăng ký
- Quản lý capacity

## 🏗️ Kiến trúc

```
src/main/java/
├── model/              # Entity classes (POJO)
├── dao/                # Data Access Object (Repository)
├── service/            # Business Logic layer
├── controller/         # Request handling
├── util/               # Utilities
└── exception/          # Custom exceptions
```

### Flow
```
Controller → Service → DAO → Database
               ↕
            Model (Entity)
```

## 🚀 Cài đặt và Chạy

### 1. Yêu cầu
- Java JDK 8+
- MySQL Server 5.7+
- MySQL Connector/J (JDBC Driver)

### 2. Cấu hình Database

#### Tạo database và import schema:
```bash
mysql -u root -p < src/main/resources/sql/schema.sql
mysql -u root -p < src/main/resources/sql/sample_data.sql
```

#### Cấu hình kết nối:
Sửa file `src/main/resources/config/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/course_registration_db
db.username=root
db.password=your_password
```

### 3. Thêm JDBC Driver
- Tải MySQL Connector/J: https://dev.mysql.com/downloads/connector/j/
- Copy file `.jar` vào thư mục `lib/`

### 4. Compile và Run

#### VS Code (recommended):
```
1. Install "Extension Pack for Java"
2. Open folder "java-oop-ptit"
3. Press F5 to run Main.java
```

#### Command Line:
```bash
# Windows PowerShell
javac -d bin -cp "lib\*" src\main\java\**\*.java
java -cp "bin;lib\*" Main
```

## 📚 Documentation

- [Project Structure](docs/PROJECT_STRUCTURE.md) - Chi tiết cấu trúc dự án
- [Class Diagram](docs/ClassDiagram.png) - Sơ đồ class
- [Database Schema](src/main/resources/sql/schema.sql) - Schema database

## 🎯 Nguyên tắc thiết kế

1. **MVC Pattern**: Tách biệt rõ ràng Model-DAO-Service-Controller
2. **Generic Repository**: BaseDAO để tránh code lặp
3. **Immutable Models**: Entity classes không có setters
4. **Separation of Concerns**: Mỗi layer có trách nhiệm riêng
5. **Clean Code**: Code dễ đọc, dễ maintain

## 📦 Database Schema

- `users` - Người dùng (admin/student)
- `students` - Thông tin sinh viên
- `courses` - Môn học
- `course_offerings` - Lớp học phần
- `registrations` - Đăng ký môn học
- `majors` - Ngành học
- `faculties` - Khoa
- `schedules` - Lịch học
- `semesters` - Học kỳ
- `rooms` - Phòng học

## 👥 Contributors

Dự án được phát triển dựa trên DBML và Class Diagram được cung cấp.

## 📄 License

[MIT License](LICENSE)
