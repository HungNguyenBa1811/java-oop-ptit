CREATE DATABASE course_registration
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;  -- Không phân biệt chữ hoa chữ thường

USE course_registration;

-- Bảng người dùng
CREATE TABLE users(
    user_id VARCHAR(15) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    full_name NVARCHAR(70) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role TINYINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng khoa
CREATE TABLE faculties(
    faculty_id VARCHAR(20) PRIMARY KEY,
    faculty_name NVARCHAR(100),
    phone_number VARCHAR(20),
    email VARCHAR(50),
    website VARCHAR(50),
    dean NVARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

ALTER TABLE faculties
ADD COLUMN description TEXT;

-- Bảng ngành
CREATE TABLE majors(
    major_id VARCHAR(20) PRIMARY KEY,
    major_name NVARCHAR(40),
    faculty_id VARCHAR(20) NOT NULL,
    degree_level ENUM('Cử nhân', 'Kỹ sư'),
    duration_years INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
        ON UPDATE CASCADE
);

-- Bảng sinh viên
CREATE TABLE students(
    student_id VARCHAR(15) PRIMARY KEY,
    class VARCHAR(20) NOT NULL,
    faculty_id VARCHAR(20) NOT NULL,
    status ENUM('Đang học', 'Nghỉ học') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
        ON UPDATE CASCADE
);

-- Bảng môn học
CREATE TABLE courses(
    course_id VARCHAR(15) PRIMARY KEY,
    course_name NVARCHAR(70),
    credits INT,
    faculty_id VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (faculty_id) REFERENCES faculties(faculty_id)
        ON UPDATE CASCADE
);

-- Bảng phòng học
CREATE TABLE rooms(
    room_id VARCHAR(5) PRIMARY KEY,
    capacity INT,
    projector TINYINT,
    airconditioner TINYINT,
    micro_speaker TINYINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng học kỳ
CREATE TABLE semesters(
    semester_id VARCHAR(15) PRIMARY KEY,
    term NVARCHAR(50),
    academic_year VARCHAR(10),
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng lớp mở môn học (course_offerings)
CREATE TABLE course_offerings(
    course_offering_id VARCHAR(15) PRIMARY KEY,
    course_id VARCHAR(15) NOT NULL,
    major_id VARCHAR(20) NOT NULL,
    instructor NVARCHAR(50),
    room_id VARCHAR(5) NOT NULL,
    semester_id VARCHAR(15) NOT NULL,
    max_capacity INT,
    current_capacity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
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

-- Bảng lịch học
CREATE TABLE schedules(
    schedule_id VARCHAR(15) PRIMARY KEY,
    day_of_week INT NOT NULL, -- 2: T2, 3: T3, ..., 8: CN
    start_time TIME, 
    end_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Liên kết giữa lớp mở và lịch học
CREATE TABLE course_offerings_schedules(
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_offering_id VARCHAR(15) NOT NULL,
    schedule_id VARCHAR(15) NOT NULL,
    start_date DATE,
    end_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_offering_id) REFERENCES course_offerings(course_offering_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Bảng đăng ký môn học
CREATE TABLE registrations(
    registration_id VARCHAR(20) PRIMARY KEY,
    student_id VARCHAR(15) NOT NULL,
    course_offering_id VARCHAR(15) NOT NULL,
    registered_at TIMESTAMP,
    status ENUM('Thành công', 'Thất bại'),
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_offering_id) REFERENCES course_offerings(course_offering_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
