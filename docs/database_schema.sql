# SQL Script để tạo database

-- Tạo database
CREATE DATABASE IF NOT EXISTS course_registration_db;
USE course_registration_db;

-- Bảng users
CREATE TABLE users (
    user_id VARCHAR(15) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name NVARCHAR(50),
    email VARCHAR(100),
    role BOOLEAN NOT NULL DEFAULT 0 COMMENT '0: student, 1: admin',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng faculties
CREATE TABLE faculties (
    faculty_id VARCHAR(10) PRIMARY KEY,
    faculty_name NVARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(200),
    dean NVARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng majors
CREATE TABLE majors (
    major_id VARCHAR(20) PRIMARY KEY,
    major_name NVARCHAR(40),
    faculty_id VARCHAR(20) NOT NULL,
    degree_level ENUM('Bachelor', 'Engineer'),
    duration_years INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
);

-- Bảng students
CREATE TABLE students (
    student_id VARCHAR(15) PRIMARY KEY,
    class VARCHAR(20),
    major_id NVARCHAR(30) NOT NULL,
    status ENUM('Đang học', 'Nghỉ học') DEFAULT 'Đang học',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(user_id),
    FOREIGN KEY (major_id) REFERENCES majors(major_id)
);

-- Bảng courses
CREATE TABLE courses (
    course_id VARCHAR(15) PRIMARY KEY,
    course_name NVARCHAR(70),
    credits INT,
    faculty_id VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
);

-- Bảng rooms
CREATE TABLE rooms (
    room_id VARCHAR(5) PRIMARY KEY,
    capacity INT,
    projector BOOLEAN DEFAULT FALSE,
    airconditioner BOOLEAN DEFAULT FALSE,
    micro_speaker BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng semesters
CREATE TABLE semesters (
    semester_id VARCHAR(15) PRIMARY KEY,
    term NVARCHAR(50),
    academic_year VARCHAR(9),
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng course_offerings
CREATE TABLE course_offerings (
    course_offering_id VARCHAR(15) PRIMARY KEY,
    course_id VARCHAR(15) NOT NULL,
    major_id VARCHAR(20) NOT NULL,
    instructor NVARCHAR(50),
    room_id VARCHAR(5) NOT NULL,
    semester_id VARCHAR(15) NOT NULL,
    max_capacity INT,
    current_capacity INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (major_id) REFERENCES majors(major_id),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id),
    FOREIGN KEY (semester_id) REFERENCES semesters(semester_id)
);

-- Bảng schedules
CREATE TABLE schedules (
    schedule_id VARCHAR(15) PRIMARY KEY,
    day_of_week INT NOT NULL COMMENT '1=Monday, 7=Sunday',
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng course_offerings_schedules
CREATE TABLE course_offerings_schedules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_offering_id VARCHAR(15) NOT NULL,
    schedule_id VARCHAR(15) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_offering_id) REFERENCES course_offerings(course_offering_id),
    FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id)
);

-- Bảng registrations
CREATE TABLE registrations (
    registration_id VARCHAR(20) PRIMARY KEY,
    student_id VARCHAR(15) NOT NULL,
    course_offering_id VARCHAR(15) NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('success', 'failed') DEFAULT 'success',
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_offering_id) REFERENCES course_offerings(course_offering_id)
);

-- Insert sample data
INSERT INTO faculties VALUES 
('FAC001', 'Khoa Công nghệ Thông tin', '0123456789', 'cntt@university.edu', 'http://cntt.university.edu', 'PGS.TS Nguyen Van A', NOW(), NOW());

INSERT INTO majors VALUES 
('MAJ001', 'Công nghệ Thông tin', 'FAC001', 'Bachelor', 4, 'Ngành Công nghệ Thông tin', NOW(), NOW());

INSERT INTO rooms VALUES 
('R101', 40, TRUE, TRUE, TRUE, NOW(), NOW()),
('R102', 35, TRUE, TRUE, FALSE, NOW(), NOW());

INSERT INTO semesters VALUES 
('SEM001', 'Fall 2025', '2025-2026', '2025-09-01', '2026-01-15', NOW(), NOW());

INSERT INTO schedules VALUES 
('SCH001', 2, '08:00:00', '10:00:00', NOW(), NOW()),
('SCH002', 4, '13:00:00', '15:00:00', NOW(), NOW());
