// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table users { // Bảng người dùng
  user_id varchar(15) [pk]
  username varchar(50) [unique, not null]
  password varchar(50) [not null]
  full_name nvarchar(70) [not null]
  email varchar(100) [unique, not null]
  role tinyint [not null, note: "0: student, 1: admin"]
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table faculties { // Bảng khoa
  faculty_id varchar(20) [pk]
  faculty_name nvarchar(100)
  phone_number varchar(20)
  email varchar(50)
  website varchar(50)
  dean nvarchar(50)
  description text
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table majors { // Bảng ngành
  major_id varchar(20) [pk]
  major_name nvarchar(40)
  faculty_id varchar(20) [not null, ref: > faculties.faculty_id]
  degree_level enum('Cử nhân', 'Kỹ sư') // Bậc đào tạo
  duration_years int // Thời gian đào tạo (năm)
  description text // Mô tả tổng quan ngành học
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table students { // Bảng sinh viên
  student_id varchar(15) [pk, ref: - users.user_id]
  class varchar(20) [not null]
  major_id varchar(20) [not null, ref: > majors.major_id]
  faculty_id varchar(20) [not null, ref: > faculties.faculty_id]
  status enum('Đang học', 'Nghỉ học') [not null]
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table courses { // Bảng môn học
  course_id varchar(15) [pk]
  course_name nvarchar(70)
  credits int
  faculty_id varchar(20) [not null, ref: > faculties.faculty_id]
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table rooms { // Bảng phòng học
  room_id varchar(5) [pk]
  capacity int
  projector tinyint
  airconditioner tinyint
  micro_speaker tinyint
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table semesters { // Bảng học kỳ
  semester_id varchar(15) [pk]
  term nvarchar(50) // Ví dụ: "Học kỳ 1"
  academic_year varchar(10) // Ví dụ: "2024-2025"
  start_date date
  end_date date
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table course_offerings { // Bảng lớp mở môn học
  course_offering_id varchar(15) [pk]
  course_id varchar(15) [not null, ref: > courses.course_id]
  faculty_id varchar(20) [not null, ref: > faculties.faculty_id]
  instructor nvarchar(50)
  room_id varchar(5) [not null, ref: > rooms.room_id]
  semester_id varchar(15) [not null, ref: > semesters.semester_id]
  max_capacity int
  current_capacity int
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table schedules { // Bảng lịch học
  schedule_id varchar(15) [pk]
  day_of_week int [not null, note: "2: T2, 3: T3, ..., 8: CN"]
  start_time time
  end_time time
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table course_offerings_schedules { // Liên kết giữa lớp mở và lịch học
  id int [pk, increment]
  course_offering_id varchar(15) [not null, ref: > course_offerings.course_offering_id]
  schedule_id varchar(15) [not null, ref: > schedules.schedule_id]
  start_date date
  end_date date
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}

Table registrations { // Bảng đăng ký môn học
  registration_id varchar(20) [pk]
  student_id varchar(15) [not null, ref: > students.student_id]
  course_offering_id varchar(15) [not null, ref: > course_offerings.course_offering_id]
  registered_at timestamp
  status enum('Thành công', 'Thất bại')
  note text
  created_at timestamp [default: `CURRENT_TIMESTAMP`]
  updated_at timestamp [default: `CURRENT_TIMESTAMP`]
}
