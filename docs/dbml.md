// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table users { // Người dùng
  user_id varchar(15) [pk, unique]
  username varchar [unique]
  password varchar
  full_name nvarchar(50)
  email varchar(100)
  role boolean [not null, note: "0: student, 1: admin", default: 0]
  created_at timestamp
  updated_at timestamp
}

Table students { // Bảng sinh viên để hiển thị thông tin
  student_id varchar(15) [pk, unique, ref : - users.user_id]
  class varchar(20)
  major_id nvarchar(30) [ref : > majors.major_id, not null]
  status enum('Đang học', 'Nghỉ học')
  created_at timestamp
  updated_at timestamp
}

Table courses { // Bảng môn học
  course_id varchar(15) [pk, unique]
  course_name nvarchar(70)
  credits int
  faculty_id varchar(20) [ref : > faculties.faculty_id, not null] 
  created_at timestamp
  updated_at timestamp
}

Table course_offerings { // Bảng lớp học phần
  course_offering_id varchar(15) [pk, unique, note: "Course offering ID"]
  course_id varchar(15) [not null, ref: > courses.course_id]
  major_id varchar(20) [not null, ref: > majors.major_id]
  instructor nvarchar(50)
  room_id varchar(5) [ref : > rooms.room_id, not null]
  semester_id varchar(15) [not null, ref : > semesters.semester_id]
  max_capacity int
  current_capacity int [default: 0]
  created_at timestamp
  updated_at timestamp
}

Table schedules{
  schedule_id varchar(15) [pk, not null]
  day_of_week int [not null, note: "1=Monday, 7=Sunday"]
  start_time time [not null]
  end_time time [not null]
  created_at timestamp
  updated_at timestamp
   
}

Table course_offerings_schedules { // Bảng lịch học
  id int [pk, increment]
  course_offering_id varchar(15) [not null, ref: > course_offerings.course_offering_id]
  schedule_id varchar(15) [not null, ref : > schedules.schedule_id]
  start_date date [not null]
  end_date date [not null]
  created_at timestamp
  updated_at timestamp
}

Table rooms { // Bảng phòng học 
  room_id varchar(5) [pk, not null]
  capacity int
  projector boolean
  airconditioner boolean
  micro_speaker boolean 
  created_at timestamp
  updated_at timestamp
}

Table faculties {
  faculty_id varchar(10) [pk]             
  faculty_name nvarchar(100)               
  phone_number varchar(20)                 
  email varchar(100)                      
  website varchar(200)                     
  dean nvarchar(50)                        
  created_at timestamp
  updated_at timestamp
}


Table majors{
  major_id varchar(20) [pk]
  major_name nvarchar(40)
  faculty_id varchar(20) [not null, ref : > faculties.faculty_id]
  degree_level enum('Bachelor','Engineer')  // Bậc đào tạo
  duration_years int                        // Thời gian đào tạo (năm)
  description text                          // Mô tả tổng quan ngành học
  created_at timestamp
  updated_at timestamp 
}

Table registrations { // Bảng đăng kí môn học
  registration_id varchar(20) [pk, unique]
  student_id varchar(15) [not null, ref: > students.student_id]
  course_offering_id varchar(15) [not null, ref: > course_offerings.course_offering_id]
  registered_at timestamp [default: `now()`]
  status enum('success', 'failed')
  note text
  created_at timestamp
  updated_at timestamp
}

Table semesters {
  semester_id varchar(15) [pk]
  term nvarchar(50) // Ví dụ: "Fall 2025"
  academic_year varchar(9) // Ví dụ: "2025-2026"
  start_date date
  end_date date
  created_at timestamp
  updated_at timestamp
}
