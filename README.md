# 📚 Hệ thống Đăng Ký Tín Chỉ (Course Registration System)

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/HungNguyenBa1811/java-oop-ptit/blob/main/LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.java.com)
[![JavaFX](https://img.shields.io/badge/JavaFX-25-blue.svg)](https://gluonhq.com/products/javafx/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-brightgreen.svg)](https://www.mysql.com)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)](#)

## 📝 Giới Thiệu (Description)

**Hệ thống Đăng Ký Tín Chỉ** là một ứng dụng desktop Java toàn diện được xây dựng nhằm quản lý quy trình đăng ký tín chỉ của sinh viên. Dự án này áp dụng các nguyên tắc lập trình hướng đối tượng (OOP), mô hình kiến trúc Model-View-Controller (MVC) hiện đại, và các design patterns chuẩn trong Java.

**Giao diện:** JavaFX 25 - Ứng dụng desktop modern với UI responsive  
**Cơ sở dữ liệu:** MySQL 8.0 với JDBC connectivity  
**Kiến trúc:** Service Layer + Repository Pattern (3-tier architecture)  
**Tính năng chính:** Quản lý đăng ký tín chỉ, kiểm tra ràng buộc lịch học, ngăn chặn trùng môn

---

## 📑 Mục Lục

1. [Tính Năng Chính](#-tính-năng-chính)
2. [Yêu Cầu Hệ Thống](#-yêu-cầu-hệ-thống)
3. [Công Nghệ Sử Dụng](#-công-nghệ-sử-dụng)
4. [Cấu Trúc Dự Án](#-cấu-trúc-dự-án)
5. [Cài Đặt & Setup](#-cài-đặt--setup)
6. [Cấu Hình Database](#-cấu-hình-database)
7. [Chạy Ứng Dụng](#-chạy-ứng-dụng)
8. [Hướng Dẫn Sử Dụng](#-hướng-dẫn-sử-dụng)
9. [API & Tính Năng](#-api--tính-năng)
10. [Troubleshooting](#-troubleshooting)
11. [Nguyên Tắc Thiết Kế](#-nguyên-tắc-thiết-kế)
12. [Đóng Góp & Phân Công](#-đóng-góp--phân-công-công-việc)
13. [Tài Liệu & Resources](#-tài-liệu--resources)
14. [Giấy Phép](#-giấy-phép)

---

## 🎯 Tính Năng Chính

### 👨‍🎓 Module Sinh Viên (Student)

#### 1. Đăng Ký Tín Chỉ (Credit Registration)
- ✅ Xem danh sách lớp học phần khả dụng trong kỳ hiện tại
- ✅ Đăng ký môn học với giao diện thân thiện
- ✅ Xem thông tin chi tiết: giảng viên, số tín chỉ, lịch học, sĩ số
- ✅ Kiểm tra real-time các ràng buộc:
  - **Không trùng môn học** (mỗi sinh viên chỉ đăng ký một lớp cho cùng một môn)
  - **Không trùng lịch học** (không chọn 2 lớp có cùng thứ/tiết/khoảng thời gian)
  - **Không đăng ký lớp đã đầy** (kiểm tra max_capacity)

#### 2. Xem Thông Tin Cá Nhân
- 👤 Thông tin sinh viên: Họ tên, MSSV, lớp, ngành, khoa
- 📅 Lịch học từng tuần
- 📊 Số tín chỉ đã đăng ký

#### 3. Quản Lý Đăng Ký Cá Nhân
- ❌ Hủy đăng ký môn học
- 📋 Xem danh sách lớp đã đăng ký
- 🔍 Tìm kiếm và lọc

### 👨‍💼 Module Quản Trị Viên (Admin)

#### 1. Quản Lý Lớp Học Phần
- ➕ Tạo lớp học phần mới
- 📝 Chỉnh sửa thông tin lớp
- 🗑️ Xóa lớp học phần
- 📊 Xem danh sách tất cả lớp

#### 2. Quản Lý Lịch Học
- 📅 Thêm lịch học cố định
- 🚨 **Hệ thống tự động phát hiện trùng lịch phòng học**
- ⚠️ Thông báo lỗi nếu trùng

#### 3. Quản Lý Đăng Ký
- 👥 Xem danh sách tất cả đăng ký
- 📝 Chỉnh sửa thông tin đăng ký
- 🗑️ Xóa đăng ký
- 📊 Theo dõi sĩ số

### 🔐 Hệ Thống Xác Thực
- 🔑 Đăng nhập bằng username/password
- 👤 Phân quyền: Student, Admin, Faculty
- 🔒 Mã hóa password
- 🚪 Đăng xuất an toàn

---

## ⚙️ Yêu Cầu Hệ Thống

### Phần Cứng
- 💾 RAM: Tối thiểu 2GB (khuyến nghị 4GB+)
- 💿 Ổ cứng: 500MB trống
- 🖥️ CPU: Dual-core, 1.5GHz trở lên

### Phần Mềm
- **Java JDK 21**: https://www.oracle.com/java/technologies/downloads/
- **JavaFX SDK 25**: https://gluonhq.com/products/javafx/
- **MySQL Server 8.0+**: https://dev.mysql.com/downloads/mysql/
- **MySQL Connector/J 8.0.33+**: https://dev.mysql.com/downloads/connector/j/
- **IDE**: IntelliJ IDEA / Eclipse / VS Code
- **Optional**: Scene Builder, MySQL Workbench

### Kiểm Tra Java
```bash
java -version
javac -version
```

---

## 🛠️ Công Nghệ Sử Dụng

| Lĩnh Vực | Công Nghệ | Phiên Bản | Mục Đích |
|---------|----------|---------|---------|
| **Backend** | Java | 21 | Ngôn ngữ chính |
| **GUI** | JavaFX | 25 | Giao diện desktop |
| **UI Markup** | FXML | 25 | Layout giao diện |
| **Styling** | CSS | 3 | Thiết kế UI |
| **Database** | MySQL | 8.0 | Lưu trữ dữ liệu |
| **JDBC Driver** | MySQL Connector/J | 8.0.33 | Kết nối Java-MySQL |
| **Architecture** | MVC + Service Layer | - | Tổ chức code |
| **Pattern** | Repository | - | Data Access |
| **Version Control** | Git | - | Quản lý source |

---

## 📂 Cấu Trúc Dự Án

```
java-oop-ptit/
├── 📄 README.md                          # Hướng dẫn này
├── 📄 LICENSE                            # Giấy phép MIT
├── .env                                  # Cấu hình database
│
├── 📁 docs/                              # Tài liệu
│   ├── PROJECT_STRUCTURE.md              # Chi tiết kiến trúc
│   ├── mysql_schema.sql                  # Database schema
│   ├── insert_data.sql                   # Dữ liệu mẫu
│   └── dbml.md                           # Database diagram
│
├── 📁 lib/                               # Thư viện
│   └── mysql-connector-j-8.0.33.jar      # JDBC Driver
│
├── 📁 src/main/
│   ├── 📁 java/
│   │   ├── config/
│   │   │   └── DatabaseConnection.java
│   │   ├── model/                        # Entity classes
│   │   ├── repository/                   # Data Access
│   │   ├── service/                      # Business Logic Interfaces
│   │   ├── service/impl/                 # Implementations
│   │   ├── controller/                   # UI Controllers
│   │   ├── view/                         # View Managers
│   │   ├── utils/                        # Utilities
│   │   ├── test/                         # Test cases
│   │   └── Main.java                     # Entry Point
│   └── 📁 resources/
│       ├── fxml/                         # FXML layouts
│       ├── css/                          # Stylesheets
│       └── assets/                       # Images, fonts
│
└── 📁 target/                            # Build output
```

### Kiến Trúc Ứng Dụng

```
┌─────────────────────────────────────────────────────┐
│  Presentation Layer (Controllers + FXML + CSS)    │
└────────────────────┬────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────┐
│  Business Logic Layer (Services)                   │
│  • Validation                                       │
│  • Schedule Conflict Detection                      │
│  • Duplicate Registration Prevention                │
└────────────────────┬────────────────────────────────┘
                     ↓
┌─────────────────────────────────────────────────────┐
│  Data Access Layer (Repositories)                  │
│  • JDBC Queries                                     │
│  • CRUD Operations                                  │
└────────────────────┬────────────────────────────────┘
                     ↓
         ┌───────────────────────┐
         │   Model/Entity        │
         │   (POJO Classes)      │
         └───────────┬───────────┘
                     ↓
         ┌───────────────────────┐
         │  MySQL Database       │
         └───────────────────────┘
```

---

## 🚀 Cài Đặt & Setup

### Bước 1: Clone Repository

```bash
git clone https://github.com/HungNguyenBa1811/java-oop-ptit.git
cd java-oop-ptit
```

### Bước 2: Cài Đặt JDK 21

**Windows:**
1. Download từ: https://www.oracle.com/java/technologies/downloads/
2. Chạy installer
3. Kiểm tra: `java -version`

**Linux (Ubuntu):**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
java -version
```

**macOS:**
```bash
brew install openjdk@21
java -version
```

### Bước 3: Cài Đặt JavaFX SDK

**Option A (IntelliJ IDEA - Recommended):**
1. File → Project Structure → Libraries
2. Chọn "+" → Add Java → Chọn JavaFX folder
3. Apply

**Option B (Manual):**
1. Download từ: https://gluonhq.com/products/javafx/
2. Giải nén vào: `lib/javafx-sdk-25/`

### Bước 4: Cài Đặt MySQL Server

**Windows:**
1. Download từ: https://dev.mysql.com/downloads/mysql/
2. Chạy installer
3. Chọn port: 3306

**Linux:**
```bash
sudo apt install mysql-server
sudo mysql_secure_installation
sudo systemctl start mysql
```

**macOS:**
```bash
brew install mysql
mysql.server start
```

### Bước 5: Cấu Hình .env

Tạo file `.env` trong thư mục root:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=course_registration_db
DB_USER=root
DB_PASSWORD=your_password
DB_DRIVER=com.mysql.cj.jdbc.Driver
```

---

## 🗄️ Cấu Hình Database

### Bước 1: Tạo Database

```bash
mysql -u root -p
```

```sql
CREATE DATABASE course_registration_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE course_registration_db;
```

### Bước 2: Import Schema

```bash
mysql -u root -p course_registration_db < docs/mysql_schema.sql
```

### Bước 3: Import Sample Data

```bash
mysql -u root -p course_registration_db < docs/insert_data.sql
```

### Bước 4: Kiểm Tra Database

```sql
SHOW TABLES;
SELECT * FROM users LIMIT 5;
SELECT * FROM students LIMIT 5;
```

### Tài Khoản Mẫu

```
Admin:
- Username: admin
- Password: admin123

Student:
- Username: student1
- Password: student123
- ID: SV001
```

---

## ▶️ Chạy Ứng Dụng

### Option 1: IntelliJ IDEA (Recommended)

1. Mở project: File → Open
2. Navigate: `src/main/java/Main.java`
3. Right-click → Run 'Main.main()'
4. Hoặc nhấn `Shift + F10`

### Option 2: VS Code

1. Cài Extension: "Extension Pack for Java"
2. Click "Run" button trên Main.java
3. Hoặc nhấn `Ctrl + F5`

### Option 3: Terminal (PowerShell)

```powershell
$JAVAFX_PATH = "lib\javafx-sdk-25\lib"
javac --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml `
      -d bin -cp "lib\*" src\main\java\**\*.java
java --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml `
     -cp "bin;lib\*" Main
```

### Option 4: Terminal (Linux/Mac)

```bash
JAVAFX_PATH="lib/javafx-sdk-25/lib"
javac --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml \
      -d bin -cp "lib/*" src/main/java/**/*.java
java --module-path $JAVAFX_PATH --add-modules javafx.controls,javafx.fxml \
     -cp "bin:lib/*" Main
```

---

## 📖 Hướng Dẫn Sử Dụng

### 🔐 Đăng Nhập

1. Khởi động ứng dụng
2. Chọn role: Student hoặc Admin
3. Nhập Username & Password
4. Nhấn "Đăng nhập"

### 👨‍🎓 Giao Diện Sinh Viên

#### Đăng Ký Tín Chỉ
1. Chọn tab "Đăng Ký Tín Chỉ"
2. Xem danh sách lớp học phần
3. Nhấn "Đăng Ký" cho lớp mong muốn
4. Hệ thống kiểm tra:
   - ✅ Không trùng môn?
   - ✅ Không trùng lịch?
   - ✅ Lớp còn chỗ?
5. Nếu hợp lệ → Đăng ký thành công
6. Nếu không → Hiển thị lý do từ chối

#### Xem Lịch Học
1. Chọn tab "Lịch Học"
2. Xem các lớp đã đăng ký
3. Thông tin: Ngày, thứ, tiết, phòng, giảng viên

#### Hủy Đăng Ký
1. Chọn tab "Đăng Ký Của Tôi"
2. Chọn lớp cần hủy
3. Nhấn "Hủy Đăng Ký"
4. Xác nhận → Hủy thành công

### 👨‍💼 Giao Diện Quản Trị Viên

#### Tạo Lớp Học Phần
1. Chọn "Quản Lý Lớp Học Phần"
2. Nhấn "Tạo Lớp Mới"
3. Điền thông tin:
   - Chọn Môn Học
   - Chọn Giảng Viên
   - Nhập Số Tín Chỉ
   - Chọn Học Kỳ
4. Thêm Lịch Học:
   - Chọn Phòng Học
   - Chọn Ngày/Thứ
   - Nhập Tiết
   - Hệ thống kiểm tra trùng lịch phòng
5. Nhấn "Lưu"

#### Quản Lý Đăng Ký
1. Chọn "Quản Lý Đăng Ký"
2. Xem danh sách tất cả đăng ký
3. Sửa, xóa, hoặc xuất báo cáo

---

## 💻 API & Tính Năng

### Student Service
```java
studentService.registerCourse(studentId, courseOfferingId);
studentService.cancelRegistration(registrationId);
studentService.getMyRegistrations(studentId);
studentService.getSchedule(studentId);
```

### Course Offering Service
```java
courseOfferingService.createCourseOffering(offering);  // ⭐ Schedule conflict check
courseOfferingService.updateCourseOffering(offering);
courseOfferingService.deleteCourseOffering(offeringId);
```

### Registration Service
```java
registrationService.register(registration);  // ⭐ Duplicate check
registrationService.cancelRegistration(registrationId);
registrationService.isDuplicateRegistration(studentId, courseId);
```

### Key Features

#### 🚨 Schedule Conflict Detection
**Location:** `CourseOfferingRepository.checkScheduleConflict()`

Kiểm tra: Same room + Same semester + Same day + Time overlap

#### 🔒 Duplicate Registration Prevention
**Location:** `RegistrationServiceImpl.register()`

Kiểm tra: Sinh viên không đăng ký cùng một môn 2 lần

---

## 🐛 Troubleshooting

### ❌ "Cannot connect to database"
- Kiểm tra MySQL Server đang chạy
- Kiểm tra `.env` config
- Kiểm tra database tồn tại

### ❌ "JDBC Driver not found"
- Đảm bảo `mysql-connector-j-8.0.33.jar` trong `lib/`
- Add library vào project (IDE)

### ❌ "JavaFX runtime components missing"
- Cài JavaFX SDK
- Thêm module-path VM options

### ❌ "FXML file not found"
- Kiểm tra file nằm trong `resources/fxml/`
- Kiểm tra đường dẫn có "/" ở đầu

### ❌ "ClassNotFoundException"
- Clean & rebuild project
- Kiểm tra classpath

---

## 🏗️ Nguyên Tắc Thiết Kế

### Separation of Concerns
```
MODEL → REPOSITORY → SERVICE → CONTROLLER
```

### Design Patterns
- **MVC**: Model-View-Controller
- **Repository**: Data Access abstraction
- **Service Layer**: Business logic
- **Singleton**: DatabaseConnection

### SOLID Principles
- **S**: Mỗi class có 1 trách nhiệm
- **O**: Mở để mở rộng, đóng để sửa
- **L**: Liskov Substitution (inheritance)
- **I**: Interface Segregation
- **D**: Dependency Inversion

### Best Practices
✅ Clean Code - Đặt tên rõ ràng  
✅ Error Handling - Try-catch  
✅ Validation - Service layer  
✅ DRY - Không lặp code  
✅ Transactions - Safe operations

---

## 👥 Đóng Góp & Phân Công Công Việc

| STT | Thành Viên | Vai Trò | Contact |
|-----|-----------|--------|---------|
| 1️⃣ | Vũ Hoàng Anh | 👑 CO-Leader + Backend Leader + Backend Dev | anhvh189@gmail.com |
| 2️⃣ | Phan Nguyễn Việt Dũng | 🔧 Backend Dev + Frontend Dev | phannguyenvietdung@gmail.com |
| 3️⃣ | Nguyễn Bá Hùng | 👑 CO-Leader + Frontend Leader + Frontend Dev | hungba1811@gmail.com |
| 4️⃣ | Lê Duy Anh | 🎨 Frontend + UI | duyanhle9c1@gmail.com |
| 5️⃣ | Nguyễn Trung Nam |🔧 Fronted Dev + Backend Dev | Trungnam0708qwert@gmail.com |

**Quy Trình Contribute:**
1. Fork repository
2. Tạo branch: `git checkout -b feature/name`
3. Commit: `git commit -m "Add feature"`
4. Push: `git push origin feature/name`
5. Mở Pull Request

---

## 📚 Tài Liệu & Resources

### Official Documentation
- [Java 21 Docs](https://docs.oracle.com/en/java/javase/21/)
- [JavaFX 25](https://openjfx.io/)
- [MySQL Docs](https://dev.mysql.com/doc/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)

### Project Documentation
- [PROJECT_STRUCTURE.md](docs/PROJECT_STRUCTURE.md)
- [mysql_schema.sql](docs/mysql_schema.sql)
- [insert_data.sql](docs/insert_data.sql)
- [dbml.md](docs/dbml.md)

### Tools
- [Scene Builder](https://gluonhq.com/products/scene-builder/)
- [MySQL Workbench](https://www.mysql.com/products/workbench/)
- [DBeaver](https://dbeaver.io/)
- [Git Kraken](https://www.gitkraken.com/)

---

## 📄 Giấy Phép

Dự án này được cấp phép theo **Giấy phép MIT**.

Bạn được phép:
- ✅ Sử dụng cho mục đích thương mại & cá nhân
- ✅ Sửa đổi & phân phối lại
- ✅ Sử dụng là phần của project khác

Điều kiện:
- ⚠️ Phải kèm bản copy của license
- ⚠️ Phải công nhân các thay đổi
- ⚠️ Không có warranty

**Chi tiết:** Xem file [LICENSE](LICENSE)

---

## 📞 Support & Contact

- **GitHub Issues**: [Report bugs](https://github.com/HungNguyenBa1811/java-oop-ptit/issues)
- **Discussions**: [Ask questions](https://github.com/HungNguyenBa1811/java-oop-ptit/discussions)
- **Email**: hungba1811@gmail.com

---

## 🎓 Học Tập & Tài Nguyên

Dự án được tạo ra nhằm:
1. 📖 Học tập OOP
2. 🏗️ Kiến trúc phần mềm
3. 🗄️ Quản lý Database
4. 🎨 UI Development
5. 📊 Real-world Features

**Khuyến khích:**
- 🔍 Khám phá code từng layer
- 🧪 Viết test cases
- 🐛 Debug & trace
- 📝 Thêm comments
- ⭐ Optimize code
---

**Status:** ✅ Active Development | 🎯 Production Ready
