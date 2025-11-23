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

1. [Giới Thiệu](#giới-thiệu)
2. [Tính Năng Chính](#tính-năng-chính)
3. [Yêu Cầu Hệ Thống](#yêu-cầu-hệ-thống)
4. [Công Nghệ Sử Dụng](#công-nghệ-sử-dụng)
5. [Cấu Trúc Dự Án](#cấu-trúc-dự-án)
6. [Cài Đặt & Setup](#cài-đặt--setup)
7. [Cấu Hình Database](#cấu-hình-database)
8. [Chạy Ứng Dụng](#chạy-ứng-dụng)
9. [Hướng Dẫn Sử Dụng](#hướng-dẫn-sử-dụng)
10. [API & Tính Năng](#api--tính-năng)
11. [Troubleshooting](#troubleshooting)
12. [Nguyên Tắc Thiết Kế](#nguyên-tắc-thiết-kế)
13. [Đóng Góp & Phân Công](#đóng-góp--phân-công-công-việc)
14. [Tài Liệu & Resources](#tài-liệu--resources)
15. [Giấy Phép](#giấy-phép)

---

## 🎯 Tính Năng Chính

### 👨‍🎓 Module Sinh Viên (Student)

#### 1. Đăng Ký Tín Chỉ (Credit Registration)
- ✅ Xem danh sách lớp học phần khả dụng trong kỳ hiện tại
- ✅ Đăng ký môn học với giao diện thân thiện
- ✅ Xem thông tin chi tiết: giảng viên, số tín chỉ, lịch học, sĩ số
- ✅ Kiểm tra real-time các ràng buộc:
  - **Không trùng môn học** (mỗi sinh viên chỉ đăng ký một lớp cho cùng một môn)
  - **Không trùng lịch học** (không chọn 2 lớp có:
    - Cùng thứ trong tuần (`day_of_week`)
    - Cùng tiết học (`start_time–end_time`)
    - Khoảng thời gian (`start_date–end_date`) bị trùng lặp)
  - **Không đăng ký lớp đã đầy** (kiểm tra `max_capacity`)

#### 2. Xem Thông Tin Cá Nhân
- 👤 Thông tin sinh viên: Họ tên, MSSV, lớp, ngành, khoa
- 📅 Lịch học từng tuần từ các lớp đã đăng ký
- 📊 Số tín chỉ đã đăng ký trong kỳ

#### 3. Quản Lý Đăng Ký Cá Nhân
- ❌ Hủy đăng ký môn học (trước hạn quy định)
- 📋 Xem danh sách lớp đã đăng ký
- 🔍 Tìm kiếm và lọc theo môn học

**Luồng hoạt động sinh viên:**
```
Đăng nhập → Xem danh sách lớp → Chọn lớp → Hệ thống kiểm tra ràng buộc
→ Hợp lệ: Lưu đăng ký ✓ | Không hợp lệ: Hiển thị lỗi ✗
→ Xem lịch học cập nhật
```

---

### 👨‍💼 Module Quản Trị Viên (Admin)

#### 1. Quản Lý Lớp Học Phần (Course Offering Management)
- ➕ **Tạo** lớp học phần mới
- 📝 **Chỉnh sửa** thông tin lớp (giảng viên, sĩ số tối đa, ngày bắt đầu/kết thúc)
- 🗑️ **Xóa** lớp học phần
- 📊 Xem danh sách tất cả lớp mở trong kỳ

#### 2. Quản Lý Lịch Học (Schedule Management)
- 📅 Thêm lịch học cố định cho lớp (ngày, tiết, phòng học)
- 🚨 Hệ thống tự động phát hiện **trùng lịch phòng học** khi tạo lớp
- ⚠️ Thông báo lỗi: "Phòng học [roomId] bị trùng lịch trong học kỳ này"

#### 3. Quản Lý Đăng Ký (Registration Management)
- 👥 Xem danh sách tất cả đăng ký của sinh viên
- 📝 Chỉnh sửa/sửa đổi thông tin đăng ký
- 🗑️ Xóa đăng ký (hủy môn học)
- 📊 Theo dõi sĩ số từng lớp

#### 4. Quản Lý Phòng Học (Room Management)
- 🏛️ Thêm/sửa/xóa phòng học
- 🔍 Kiểm tra lịch trống trong phòng

**Luồng hoạt động admin:**
```
Đăng nhập Admin → Chọn chức năng
→ Tạo lớp → Thêm lịch → Hệ thống kiểm tra trùng lịch phòng
→ Tạo thành công ✓ | Lịch trùng: Từ chối ✗
→ Xem danh sách đăng ký → Quản lý sinh viên
```

---

### 🔐 Hệ Thống Xác Thực (Authentication)

- 🔑 Đăng nhập bằng username/password
- 👤 Phân quyền: Student, Admin, Faculty (Giảng viên)
- 🔒 Mã hóa password (Password hashing)
- 🚪 Đăng xuất an toàn
- ⏱️ Session management

---

## ⚙️ Yêu Cầu Hệ Thống

### Phần Cứng (Hardware)
- 💾 RAM: Tối thiểu 2GB (khuyến nghị 4GB+)
- 💿 Ổ cứng: 500MB trống cho project + dependencies
- 🖥️ CPU: Dual-core, 1.5GHz trở lên

### Phần Mềm (Software)
- **Java JDK 21** (Java Development Kit)
  - Download từ: https://www.oracle.com/java/technologies/downloads/
  - Hoặc dùng OpenJDK: https://openjdk.org/
  
- **JavaFX SDK 25** (GUI Framework)
  - Download từ: https://gluonhq.com/products/javafx/
  - Bao gồm: javafx.controls, javafx.fxml, javafx.graphics
  
- **MySQL Server 8.0+**
  - Download từ: https://dev.mysql.com/downloads/mysql/
  - Hoặc dùng MariaDB (tương thích)
  
- **MySQL Connector/J (JDBC Driver)**
  - Download từ: https://dev.mysql.com/downloads/connector/j/
  - Phiên bản: 8.0.33 trở lên
  
- **IDE hoặc Editor**
  - IntelliJ IDEA (Recommended) - Community Edition miễn phí
  - Eclipse IDE + extension
  - VS Code + Extension Pack for Java
  
- **Optional Tools**
  - Scene Builder (thiết kế FXML): https://gluonhq.com/products/scene-builder/
  - MySQL Workbench (quản lý database): https://www.mysql.com/products/workbench/

### Kiểm Tra Java
```bash
java -version
# Output: java version "21" ...

javac -version
# Output: javac 21 ...
```

---

## 🛠️ Công Nghệ Sử Dụng

| Lĩnh Vực | Công Nghệ | Phiên Bản | Mục Đích |
|---------|----------|---------|---------|
| **Backend** | Java | 21 | Ngôn ngữ chính, xử lý business logic |
| **GUI Framework** | JavaFX | 25 | Tạo giao diện desktop modern |
| **UI Markup** | FXML | 25 | Định nghĩa layout giao diện (XML-based) |
| **Styling** | CSS | 3 | Thiết kế giao diện, màu sắc, font |
| **Database** | MySQL | 8.0 | Lưu trữ dữ liệu |
| **Database Driver** | JDBC (MySQL Connector/J) | 8.0.33 | Kết nối Java ↔ MySQL |
| **Architecture** | MVC + Service Layer | - | Tổ chức code theo pattern |
| **Design Pattern** | Repository Pattern | - | Data Access abstraction |
| **Build Tool** | Maven (Optional) | 3.8+ | Quản lý dependencies (nếu có) |
| **Version Control** | Git | - | Quản lý mã nguồn |

### Kiến Trúc Chi Tiết

```
┌─────────────────────────────────────────────────────────────────┐
│                         Application Layers                      │
├──────────────────────┬─────────────────────┬────────────────────┤
│  Presentation Layer  │   Business Layer    │   Data Access      │
│  (Presentation)      │   (Service Layer)   │   (Repository)     │
├──────────────────────┼─────────────────────┼────────────────────┤
│ • JavaFX Views       │ • Validation Logic  │ • JDBC Queries     │
│ • FXML Layouts       │ • Business Rules    │ • CRUD Operations  │
│ • CSS Styling        │ • Service Classes   │ • Repository       │
│ • Controllers        │ • Error Handling    │ • Connection Pool  │
└──────────────────────┴─────────────────────┴────────────────────┘
                              ↓ ↓ ↓
                     ┌─────────────────────┐
                     │   Model / Entity    │
                     │   (POJO Classes)    │
                     └─────────────────────┘
                              ↓ ↓ ↓
                     ┌─────────────────────┐
                     │  MySQL Database     │
                     │  (Persistence)      │
                     └─────────────────────┘
```

---

---

## 3. Student Registration Screen

Khi sinh viên truy cập vào màn hình đăng ký tín chỉ, hệ thống hiển thị:

- **Danh sách lớp học phần (course_offerings)** khả dụng trong kỳ hiện tại, gồm các thông tin:
  - Tên môn học
  - Mã lớp học phần (`course_offering_id`)
  - Giảng viên phụ trách
  - Số tín chỉ
  - Sĩ số hiện tại / Sĩ số tối đa
  - Thông tin lịch học từ `course_offerings_schedule` (thứ, giờ học, ngày bắt đầu, ngày kết thúc)

- **Trạng thái đăng ký** cho từng lớp:
  - **Nút "Đăng ký"** (enable) → nếu lớp còn chỗ trống và không vi phạm ràng buộc.
  - **Nút "Đăng ký"** (disable, màu xám) → nếu lớp:
    - Đã được sinh viên đăng ký trước đó,
    - Trùng môn học đã đăng ký,
    - Trùng lịch học với lớp khác,
    - Hoặc đã đầy sĩ số (`max_capacity`).
  - Khi hover vào nút disable → hiển thị tooltip thông báo lý do (ví dụ: *“Môn học đã được đăng ký”*, *“Lịch học trùng với lớp XYZ”*, *“Lớp đã đầy”*).

- **Bảng đăng ký cá nhân**:
  - Hiển thị danh sách các lớp học phần sinh viên đã đăng ký trong kỳ.
  - Có thể hủy/xóa đăng ký trước hạn quy định.

**Luồng thao tác chính:**
1. Sinh viên chọn lớp học phần từ danh sách.
2. Hệ thống kiểm tra ràng buộc (trùng môn, trùng lịch, sĩ số).
3. Nếu hợp lệ → lưu vào `registrations` và cập nhật sĩ số `course_offerings`.
4. Nếu không hợp lệ → hiển thị thông báo lỗi, không cho đăng ký.


## 4. Database Notes

**Bảng liên quan:**
- `users`: thông tin chung (username, password, role).
- `students`: thông tin riêng cho sinh viên.
- `courses`: thông tin môn học.
- `course_offerings`: lớp học phần mở theo kỳ/năm học.
- `course_offerings_schedule`: lịch học cố định (ngày bắt đầu/kết thúc, thứ, giờ học).
- `registrations`: lưu đăng ký của sinh viên.

**ERD:** [View Diagram](https://dbdiagram.io/d/erd-oop-db-68d0c6b07c85fb9961bc7bee)

## Công nghệ sử dụng

- **Ngôn ngữ:** Java (JDK 8+)
- **UI Framework:** JavaFX - Giao diện desktop hiện đại
- **Kiến trúc:** Model-View-Controller (MVC) với Service Layer
- **Cơ sở dữ liệu:** MySQL 5.7+
- **JDBC Driver:** MySQL Connector/J
- **Design Patterns:** 
  - Repository Pattern
  - Singleton Pattern (Database Connection)
  - Generic Repository Pattern
  - MVC Pattern với JavaFX

## Cấu trúc dự án

Dự án được tổ chức theo mô hình MVC với JavaFX:

- **Model:** Chứa các lớp đại diện cho dữ liệu của ứng dụng (Entity classes: User, Student, Admin, Course, CourseOffering, Registration, etc.)
- **View:** JavaFX FXML files và UI components - Giao diện người dùng desktop
- **Controller:** Xử lý các yêu cầu từ người dùng, tương tác với `Model` thông qua Service layer và cập nhật `View`
- **Repository:** Data Access Objects - Tương tác trực tiếp với database
- **Service:** Business Logic Layer - Xử lý validation và ràng buộc nghiệp vụ

Class Diagram:
<div align="center">
<img align="center" style="width: 40%; height: auto;" src="./docs/ClassDiagram.png">
</div>

Cấu trúc thư mục:
```
java-oop-ptit/
├── docs/                           # Documentation
│   ├── mysql_schema.sql           # Database schema
│   ├── insert_data.sql            # Sample data
│   └── PROJECT_STRUCTURE.md       # Project structure details
├── lib/                           # External libraries
│   └── mysql-connector-j-8.0.33.jar
├── src/
│   └── main/
│       ├── java/
│       │   ├── config/
│       │   │   └── DatabaseConnection.java    # DB connection manager
│       │   ├── model/                         # Domain models
│       │   │   ├── User.java
│       │   │   ├── Student.java
│       │   │   ├── Admin.java
│       │   │   ├── Course.java
│       │   │   ├── CourseOffering.java
│       │   │   ├── Registration.java
│       │   │   └── ... (11 models total)
│       │   ├── repository/                    # Data access layer
│       │   │   ├── UserRepository.java
│       │   │   ├── StudentRepository.java
│       │   │   ├── CourseRepository.java
│       │   │   ├── CourseOfferingRepository.java
│       │   │   ├── RegistrationRepository.java
│       │   │   └── ... (10 repositories total)
│       │   ├── service/                       # Business logic interfaces
│       │   │   ├── AuthService.java
│       │   │   ├── UserService.java
│       │   │   ├── AdminService.java
│       │   │   ├── StudentService.java
│       │   │   ├── CourseService.java
│       │   │   ├── CourseOfferingService.java
│       │   │   └── RegistrationService.java
│       │   ├── service/impl/                  # Service implementations
│       │   │   ├── AuthServiceImpl.java
│       │   │   ├── UserServiceImpl.java
│       │   │   ├── AdminServiceImpl.java
│       │   │   ├── StudentServiceImpl.java
│       │   │   ├── CourseServiceImpl.java
│       │   │   ├── CourseOfferingServiceImpl.java
│       │   │   └── RegistrationServiceImpl.java
│       │   ├── controller/                    # UI controllers
│       │   ├── view/                          # JavaFX views
│       │   └── test/                          # Test cases
│       │       ├── DBTest.java
│       │       └── AdminLoginTest.java
│       └── resources/
│           ├── fxml/                          # FXML layouts
│           ├── css/                           # Stylesheets
│           └── assets/                        # Images, fonts
├── target/                        # Compiled classes
├── .env                          # Environment variables
├── README.md
└── LICENSE
```

Sơ đồ kiến trúc:
```ascii
┌─────────────┐       ┌──────────────┐       ┌──────────────┐       ┌─────────┐       ┌──────────┐
│  Controller │ ----> │   Service    │ ----> │  Repository  │ ----> │  Model  │ ----> │ Database │
│  (Handler)  │ <---- │  (Business)  │ <---- │ (Repository) │ <---- │ (POJO)  │ <---- │  MySQL   │
└─────────────┘       └──────────────┘       └──────────────┘       └─────────┘       └──────────┘
      ↑                                                                                       
      │                                                                                       
  User Request                                                                               
```

**Flow xử lý:**
1. **Controller** nhận request từ user
2. **Service** xử lý business logic (validation, ràng buộc)
3. **Repository** thực hiện CRUD operations với database
4. **Model** là entity đại diện cho data
5. Kết quả trả về theo chiều ngược lại

## Cài đặt

### 1. Yêu cầu hệ thống
- **Java JDK 11 trở lên** (JavaFX yêu cầu JDK 11+)
- **JavaFX SDK 11+** (nếu không dùng module system)
- **MySQL Server 5.7+**
- **IDE:** IntelliJ IDEA (recommended) / Eclipse / VS Code (với Extension Pack for Java)
- **MySQL Connector/J** (JDBC Driver)
- **Scene Builder** (optional - để design FXML trực quan)

### 2. Clone repository
```bash
git clone https://github.com/HungNguyenBa1811/java-oop-ptit.git
cd java-oop-ptit
```

### 3. Cài đặt MySQL Database

#### Bước 1: Tạo database
```bash
mysql -u root -p
```

```sql
CREATE DATABASE course_registration_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE course_registration_db;
```

#### Bước 2: Import schema và sample data
```bash
# Import schema
mysql -u root -p course_registration_db < src/main/resources/sql/schema.sql

# Import sample data (optional)
mysql -u root -p course_registration_db < src/main/resources/sql/sample_data.sql
```

### 4. Cấu hình Database Connection

Chỉnh sửa file `src/main/resources/config/database.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/course_registration_db
db.username=root
db.password=your_password_here
db.driver=com.mysql.cj.jdbc.Driver
```

### 5. Cài đặt JavaFX

#### Cách 1: Sử dụng IntelliJ IDEA (Recommended)
IntelliJ IDEA đã tích hợp sẵn JavaFX, chỉ cần:
1. Mở project trong IntelliJ
2. File → Project Structure → Libraries → Add JavaFX SDK
3. Hoặc dùng Maven/Gradle dependencies (xem bên dưới)

#### Cách 2: Tải JavaFX SDK thủ công
1. Tải JavaFX SDK từ: https://gluonhq.com/products/javafx/
2. Giải nén vào thư mục `lib/javafx-sdk-xx/`
3. Add VM options khi chạy:
```
--module-path "lib/javafx-sdk-xx/lib" --add-modules javafx.controls,javafx.fxml
```

### 6. Thêm JDBC Driver (nếu không dùng Maven)

1. Tải driver từ: https://dev.mysql.com/downloads/connector/j/
2. Chọn **Platform Independent**
3. Giải nén và copy file `mysql-connector-java-x.x.xx.jar` vào thư mục `lib/`

### 7. Biên dịch và chạy

#### IntelliJ IDEA (Recommended cho JavaFX)
1. Mở project
2. File → Project Structure → Libraries:
   - Add JavaFX SDK
   - Add JDBC driver
3. Run → Edit Configurations → VM options (nếu cần):
   ```
   --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
   ```
4. Chạy `Main.java`

#### VS Code
1. Cài đặt **Extension Pack for Java**
2. Cài đặt extension **JavaFX Support**
3. Mở folder `java-oop-ptit`
4. Cấu hình `launch.json` với VM arguments cho JavaFX
5. Nhấn `F5` để chạy

#### Command Line với JavaFX (Windows PowerShell)
```powershell
# Compile
javac --module-path "lib\javafx-sdk-xx\lib" --add-modules javafx.controls,javafx.fxml -d bin -cp "lib\*" src\main\java\**\*.java

# Run
java --module-path "lib\javafx-sdk-xx\lib" --add-modules javafx.controls,javafx.fxml -cp "bin;lib\*" Main
```

#### Command Line với JavaFX (Linux/Mac)
```bash
# Compile
javac --module-path "lib/javafx-sdk-xx/lib" --add-modules javafx.controls,javafx.fxml -d bin -cp "lib/*" src/main/java/**/*.java

# Run
java --module-path "lib/javafx-sdk-xx/lib" --add-modules javafx.controls,javafx.fxml -cp "bin:lib/*" Main
```

### 8. Cài đặt Scene Builder (Optional)

Scene Builder giúp thiết kế FXML UI một cách trực quan:

1. Tải từ: https://gluonhq.com/products/scene-builder/
2. Cài đặt Scene Builder
3. Trong IntelliJ: Settings → Languages & Frameworks → JavaFX → Set Scene Builder path
4. Double-click file `.fxml` sẽ mở trong Scene Builder

## Sử dụng

### Chạy ứng dụng

Ứng dụng sử dụng **JavaFX** để hiển thị giao diện desktop:

```java
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML for login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Course Registration System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Test database connection
        DatabaseConnection dbConn = DatabaseConnection.getInstance();
        
        // Launch JavaFX application
        launch(args);
    }
}
```

### Giao diện ứng dụng

Ứng dụng cung cấp giao diện desktop với JavaFX bao gồm:

#### 🔐 Login Screen
- Đăng nhập cho Student/Admin
- Xác thực username/password

#### 👨‍🎓 Student Dashboard
- Xem danh sách course offerings khả dụng
- Đăng ký môn học với kiểm tra ràng buộc real-time
- Xem lịch học theo tuần
- Quản lý đăng ký cá nhân (hủy đăng ký)

#### 👨‍💼 Admin Dashboard
- Quản lý Course Offerings (CRUD)
- Xem danh sách đăng ký
- Cập nhật sĩ số, điểm số
- Thống kê và báo cáo

### Các chức năng chính

#### Dành cho Student
```java
// Đăng ký môn học
studentService.registerCourse(studentId, courseOfferingId);

// Xem lịch học
List<Schedule> schedules = studentService.getSchedule(studentId);

// Xem thông tin cá nhân
Student student = studentService.getStudentInfo(studentId);

// Hủy đăng ký
studentService.cancelRegistration(registrationId);
```

#### Dành cho Admin
```java
// Xem tất cả đăng ký
List<Registration> registrations = adminService.getAllRegistrations();

// Quản lý course offering
adminService.createCourseOffering(courseOffering);
adminService.updateCourseOffering(courseOffering);
adminService.deleteCourseOffering(offeringId);

// Kiểm tra sĩ số
int currentCapacity = adminService.getCurrentCapacity(offeringId);
```

### Testing

Chạy các test case với sample data đã import:

1. **Test đăng ký thành công**
2. **Test ràng buộc trùng môn học**
3. **Test ràng buộc trùng lịch học**
4. **Test ràng buộc lớp đã đầy**

### Tài liệu chi tiết

- [PROJECT_STRUCTURE.md](docs/PROJECT_STRUCTURE.md) - Chi tiết về architecture và design patterns
- [Class Diagram](docs/ClassDiagram.png) - Sơ đồ class đầy đủ
- [DBML Schema](docs/dbml.md) - Mô tả database schema
- [Database Schema SQL](docs/database_schema.sql) - Script tạo database

### JavaFX Resources

- [JavaFX Documentation](https://openjfx.io/) - Official JavaFX docs
- [Scene Builder](https://gluonhq.com/products/scene-builder/) - Visual FXML editor
- [JavaFX Tutorial](https://jenkov.com/tutorials/javafx/index.html) - Comprehensive guide
- [FXML Guide](https://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm) - FXML basics

## Nguyên tắc thiết kế

### 1. Separation of Concerns
- **Model**: Chỉ chứa data, không có business logic
- **View**: JavaFX FXML files - UI layout và styling
- **Controller**: JavaFX Controllers - Xử lý user interactions và cập nhật UI
- **Repository**: Chỉ thao tác với database (CRUD)
- **Service**: Xử lý business logic, validation, ràng buộc

### 2. Design Patterns
- **MVC Pattern với JavaFX**: Tách biệt UI (FXML) và logic (Controller)
- **Repository Pattern**: Generic BaseRepository để tránh code lặp
- **Singleton**: DatabaseConnection duy nhất trong toàn app
- **Inheritance**: Student/Admin extends User
- **Immutability**: Entity classes không có setters
- **Observer Pattern**: JavaFX Properties cho data binding

### 3. Best Practices
- **Clean Code**: Đặt tên rõ ràng, dễ hiểu
- **DRY Principle**: Không lặp code
- **SOLID Principles**: Single Responsibility, Open/Closed
- **Error Handling**: Sử dụng try-catch cho các thao tác cơ sở dữ liệu
- **Security**: Password được hash, không expose trực tiếp

## Troubleshooting

### Lỗi kết nối database
```
Error: Cannot connect to database
```
**Giải pháp:**
- Kiểm tra MySQL Server đang chạy
- Kiểm tra username/password trong `database.properties`
- Kiểm tra database `course_registration_db` đã được tạo

### Lỗi JDBC Driver
```
Error: ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Giải pháp:**
- Kiểm tra file `.jar` trong thư mục `lib/`
- Đảm bảo đã add library vào project (IntelliJ/Eclipse)
- Kiểm tra classpath khi compile/run

### Lỗi duplicate entry
```
Error: Duplicate entry for key 'PRIMARY'
```
**Giải pháp:**
- Kiểm tra ID đã tồn tại trong database
- Sử dụng `AUTO_INCREMENT` cho primary key
- Xử lý exception trong code

### Lỗi JavaFX Runtime
```
Error: JavaFX runtime components are missing
```
**Giải pháp:**
- Cài đặt JavaFX SDK hoặc dùng Maven dependencies
- Thêm VM options: `--module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.fxml`
- Đảm bảo JDK 11+ được sử dụng

### Lỗi FXML Load
```
Error: Location is not set / IOException loading FXML
```
**Giải pháp:**
- Kiểm tra đường dẫn FXML file đúng (phải có `/` ở đầu nếu ở resources)
- Đảm bảo FXML file nằm trong `src/main/resources/view/`
- Kiểm tra fx:controller trong FXML trỏ đúng class

## Contributing

Mọi đóng góp đều được chào đón! Vui lòng:

1. Fork repository
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

## Đóng góp & Phân công công việc

| Thành Viên | Vai trò | Contact |
| :--- | :--- | :--- |
| Vũ Hoàng Anh | Leader + BE dev | anhvh189@gmail.com |
| Phan Nguyễn Việt Dũng | BE dev | phannguyenvietdung@gmail.com |
| Nguyễn Bá Hùng | FE dev + UI design | hungba1811@gmail.com |
| Lê Duy Anh | FE dev + UI design | duyanhle9c1@gmail.com |
| Nguyễn Trung Nam | Tester + BA | Trungnam0708qwert@gmail.com |

*Mọi người đều tham gia vào việc thiết kế cơ sở dữ liệu.*

## Resources

- [Java Documentation](https://docs.oracle.com/en/java/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [MVC Pattern](https://www.tutorialspoint.com/design_pattern/mvc_pattern.htm)
- [DBML Documentation](https://www.dbml.org/)

## Giấy phép

Dự án này được cấp phép theo Giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.
