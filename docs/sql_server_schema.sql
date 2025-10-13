CREATE DATABASE course_registration;
GO

USE course_registration;
GO

-- Bảng người dùng
CREATE TABLE users (
    user_id VARCHAR(15) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    full_name NVARCHAR(70) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role TINYINT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- Bảng khoa
CREATE TABLE faculties (
    faculty_id VARCHAR(20) PRIMARY KEY,
    faculty_name NVARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(50),
    website VARCHAR(50),
    dean NVARCHAR(50),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- Bảng ngành học
CREATE TABLE majors (
    major_id VARCHAR(20) PRIMARY KEY,
    major_name NVARCHAR(40),
    faculty_id VARCHAR(20) NOT NULL,
    degree_level NVARCHAR(20) CHECK (degree_level IN (N'Cử nhân', N'Kỹ sư')),
    duration_years INT,
    description NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
);
GO

-- Bảng sinh viên
CREATE TABLE students (
    student_id VARCHAR(15) PRIMARY KEY,
    class VARCHAR(20) NOT NULL,
    major_id VARCHAR(20) NOT NULL,
    status NVARCHAR(20) CHECK (status IN (N'Đang học', N'Nghỉ học')),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (student_id) REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (major_id) REFERENCES majors(major_id)
        ON UPDATE CASCADE
);
GO

-- Bảng môn học
CREATE TABLE courses (
    course_id VARCHAR(15) PRIMARY KEY,
    course_name NVARCHAR(70),
    credits INT,
    faculty_id VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
);
GO

-- Bảng phòng học
CREATE TABLE rooms (
    room_id VARCHAR(5) PRIMARY KEY,
    capacity INT,
    projector BIT,
    airconditioner BIT,
    micro_speaker BIT,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- Bảng học kỳ
CREATE TABLE semesters (
    semester_id VARCHAR(15) PRIMARY KEY,
    term NVARCHAR(50),
    academic_year VARCHAR(10),
    start_date DATE,
    end_date DATE,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- Bảng mở lớp học phần
CREATE TABLE course_offerings (
    course_offering_id VARCHAR(15) PRIMARY KEY,
    course_id VARCHAR(15) NOT NULL,
    major_id VARCHAR(20) NOT NULL,
    instructor NVARCHAR(50),
    room_id VARCHAR(5) NOT NULL,
    semester_id VARCHAR(15) NOT NULL,
    max_capacity INT,
    current_capacity INT,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (major_id) REFERENCES majors(major_id)
        ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
        ON UPDATE CASCADE,
    FOREIGN KEY (semester_id) REFERENCES semesters(semester_id)
        ON UPDATE CASCADE
);
GO

-- Bảng lịch học
CREATE TABLE schedules (
    schedule_id VARCHAR(15) PRIMARY KEY,
    day_of_week INT NOT NULL CHECK (day_of_week BETWEEN 2 AND 8),
    start_time TIME,
    end_time TIME,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE()
);
GO

-- Bảng liên kết học phần - lịch học
CREATE TABLE course_offerings_schedules (
    id INT IDENTITY(1,1) PRIMARY KEY,
    course_offering_id VARCHAR(15) NOT NULL,
    schedule_id VARCHAR(15) NOT NULL,
    start_date DATE,
    end_date DATE,
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (course_offering_id) REFERENCES course_offerings(course_offering_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO

-- Bảng đăng ký học phần
CREATE TABLE registrations (
    registration_id VARCHAR(20) PRIMARY KEY,
    student_id VARCHAR(15) NOT NULL,
    course_offering_id VARCHAR(15) NOT NULL,
    registered_at DATETIME DEFAULT GETDATE(),
    status NVARCHAR(20) CHECK (status IN (N'Thành công', N'Thất bại')),
    note NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (course_offering_id) REFERENCES course_offerings(course_offering_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
GO
