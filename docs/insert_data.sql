USE course_registration;

-- ===== USERS =====
INSERT INTO users (user_id, username, password, full_name, email, role) VALUES
('U001', 'nguyenvana', '123456', N'Nguyễn Văn A', 'vana@student.university.edu', 0),
('U002', 'tranthib', '123456', N'Trần Thị B', 'thib@student.university.edu', 0),
('U003', 'lehoangc', '123456', N'Lê Hoàng C', 'hoangc@student.university.edu', 0),
('U004', 'phamminhd', '123456', N'Phạm Minh D', 'minhd@student.university.edu', 0),
('U005', 'admin', 'admin123', N'Quản trị viên', 'admin@university.edu', 1);

-- ===== FACULTIES =====
INSERT INTO faculties (faculty_id, faculty_name, phone_number, email, website, dean) VALUES
('FAC001', N'Khoa Công nghệ Thông tin', '0123456789', 'cntt@university.edu', 'http://cntt.university.edu', N'PGS.TS Nguyễn Văn A'),
('FAC002', N'Khoa Điện tử Viễn thông', '0987654321', 'dtvt@university.edu', 'http://dtvt.university.edu', N'TS. Lê Văn B'),
('FAC003', N'Khoa Kinh tế', '0911223344', 'kinhte@university.edu', 'http://kinhte.university.edu', N'TS. Nguyễn Thị C'),
('FAC004', N'Khoa Cơ khí', '0933445566', 'cokhi@university.edu', 'http://cokhi.university.edu', N'TS. Đỗ Văn D'),
('FAC005', N'Khoa Ngôn ngữ Anh', '0944556677', 'nnanh@university.edu', 'http://nnanh.university.edu', N'TS. Lê Thị E');

-- ===== MAJORS =====
INSERT INTO majors (major_id, major_name, faculty_id, degree_level, duration_years, description) VALUES
('MAJ001', N'Công nghệ Thông tin', 'FAC001', 'Cử nhân', 4, 'Ngành học về CNTT và phần mềm'),
('MAJ002', N'Kỹ thuật phần mềm', 'FAC001', 'Kỹ sư', 4, 'Chuyên ngành phát triển phần mềm'),
('MAJ003', N'Điện tử Viễn thông', 'FAC002', 'Cử nhân', 4, 'Nghiên cứu tín hiệu và viễn thông'),
('MAJ004', N'Quản trị kinh doanh', 'FAC003', 'Cử nhân', 4, 'Đào tạo nhà quản lý doanh nghiệp'),
('MAJ005', N'Ngôn ngữ Anh thương mại', 'FAC005', 'Cử nhân', 4, 'Tiếng Anh ứng dụng trong kinh doanh');

-- ===== STUDENTS =====
INSERT INTO students (student_id, class, faculty_id, status) VALUES
('U001', 'CNTT-K65A', 'FAC001', 'Đang học'),
('U002', 'KTPM-K65B', 'FAC001', 'Đang học'),
('U003', 'DTVT-K65A', 'FAC002', 'Đang học'),
('U004', 'QTKD-K65A', 'FAC003', 'Đang học');

-- ===== COURSES =====
INSERT INTO courses (course_id, course_name, credits, faculty_id) VALUES
('COU001', N'Cấu trúc dữ liệu và Giải thuật', 3, 'FAC001'),
('COU002', N'Lập trình Web', 3, 'FAC001'),
('COU003', N'Mạch điện tử cơ bản', 3, 'FAC002'),
('COU004', N'Nguyên lý quản trị', 2, 'FAC003'),
('COU005', N'Tiếng Anh giao tiếp nâng cao', 2, 'FAC005');

-- ===== ROOMS =====
INSERT INTO rooms (room_id, capacity, projector, airconditioner, micro_speaker) VALUES
('R101', 40, TRUE, TRUE, TRUE),
('R102', 35, TRUE, TRUE, FALSE),
('R103', 50, TRUE, TRUE, TRUE),
('R104', 30, FALSE, TRUE, FALSE),
('R105', 45, TRUE, TRUE, TRUE);

-- ===== SEMESTERS =====
INSERT INTO semesters (semester_id, term, academic_year, start_date, end_date) VALUES
('SEM001', 'Fall 2025', '2025-2026', '2025-09-01', '2026-01-15'),
('SEM002', 'Spring 2026', '2025-2026', '2026-02-01', '2026-06-01'),
('SEM003', 'Fall 2026', '2026-2027', '2026-09-01', '2027-01-15'),
('SEM004', 'Spring 2027', '2026-2027', '2027-02-01', '2027-06-01'),
('SEM005', 'Summer 2027', '2026-2027', '2027-07-01', '2027-08-15');

-- ===== SCHEDULES =====
INSERT INTO schedules (schedule_id, day_of_week, start_time, end_time) VALUES
('SCH001', 2, '07:00:00', '08:00:00'),
('SCH002', 2, '08:00:00', '09:00:00'),
('SCH003', 2, '09:00:00', '10:00:00'),
('SCH004', 2, '10:00:00', '11:00:00'),
('SCH005', 2, '11:00:00', '12:00:00'),
('SCH006', 2, '12:00:00', '13:00:00'),
('SCH007', 2, '13:00:00', '14:00:00'),
('SCH008', 2, '14:00:00', '15:00:00'),
('SCH009', 2, '15:00:00', '16:00:00'),
('SCH010', 2, '16:00:00', '17:00:00'),
('SCH011', 2, '17:00:00', '18:00:00'),
('SCH012', 2, '18:00:00', '19:00:00'),
('SCH013', 2, '19:00:00', '20:00:00'),
('SCH014', 2, '20:00:00', '21:00:00'),
('SCH015', 2, '21:00:00', '22:00:00'),
('SCH016', 2, '22:00:00', '23:00:00'),
('SCH017', 2, '23:00:00', '00:00:00'),
('SCH018', 3, '07:00:00', '08:00:00'),
('SCH019', 3, '08:00:00', '09:00:00'),
('SCH020', 3, '09:00:00', '10:00:00'),
('SCH021', 3, '10:00:00', '11:00:00'),
('SCH022', 3, '11:00:00', '12:00:00'),
('SCH023', 3, '12:00:00', '13:00:00'),
('SCH024', 3, '13:00:00', '14:00:00'),
('SCH025', 3, '14:00:00', '15:00:00'),
('SCH026', 3, '15:00:00', '16:00:00'),
('SCH027', 3, '16:00:00', '17:00:00'),
('SCH028', 3, '17:00:00', '18:00:00'),
('SCH029', 3, '18:00:00', '19:00:00'),
('SCH030', 3, '19:00:00', '20:00:00'),
('SCH031', 3, '20:00:00', '21:00:00'),
('SCH032', 3, '21:00:00', '22:00:00'),
('SCH033', 3, '22:00:00', '23:00:00'),
('SCH034', 3, '23:00:00', '00:00:00'),
('SCH035', 4, '07:00:00', '08:00:00'),
('SCH036', 4, '08:00:00', '09:00:00'),
('SCH037', 4, '09:00:00', '10:00:00'),
('SCH038', 4, '10:00:00', '11:00:00'),
('SCH039', 4, '11:00:00', '12:00:00'),
('SCH040', 4, '12:00:00', '13:00:00'),
('SCH041', 4, '13:00:00', '14:00:00'),
('SCH042', 4, '14:00:00', '15:00:00'),
('SCH043', 4, '15:00:00', '16:00:00'),
('SCH044', 4, '16:00:00', '17:00:00'),
('SCH045', 4, '17:00:00', '18:00:00'),
('SCH046', 4, '18:00:00', '19:00:00'),
('SCH047', 4, '19:00:00', '20:00:00'),
('SCH048', 4, '20:00:00', '21:00:00'),
('SCH049', 4, '21:00:00', '22:00:00'),
('SCH050', 4, '22:00:00', '23:00:00'),
('SCH051', 4, '23:00:00', '00:00:00'),
('SCH052', 5, '07:00:00', '08:00:00'),
('SCH053', 5, '08:00:00', '09:00:00'),
('SCH054', 5, '09:00:00', '10:00:00'),
('SCH055', 5, '10:00:00', '11:00:00'),
('SCH056', 5, '11:00:00', '12:00:00'),
('SCH057', 5, '12:00:00', '13:00:00'),
('SCH058', 5, '13:00:00', '14:00:00'),
('SCH059', 5, '14:00:00', '15:00:00'),
('SCH060', 5, '15:00:00', '16:00:00'),
('SCH061', 5, '16:00:00', '17:00:00'),
('SCH062', 5, '17:00:00', '18:00:00'),
('SCH063', 5, '18:00:00', '19:00:00'),
('SCH064', 5, '19:00:00', '20:00:00'),
('SCH065', 5, '20:00:00', '21:00:00'),
('SCH066', 5, '21:00:00', '22:00:00'),
('SCH067', 5, '22:00:00', '23:00:00'),
('SCH068', 5, '23:00:00', '00:00:00'),
('SCH069', 6, '07:00:00', '08:00:00'),
('SCH070', 6, '08:00:00', '09:00:00'),
('SCH071', 6, '09:00:00', '10:00:00'),
('SCH072', 6, '10:00:00', '11:00:00'),
('SCH073', 6, '11:00:00', '12:00:00'),
('SCH074', 6, '12:00:00', '13:00:00'),
('SCH075', 6, '13:00:00', '14:00:00'),
('SCH076', 6, '14:00:00', '15:00:00'),
('SCH077', 6, '15:00:00', '16:00:00'),
('SCH078', 6, '16:00:00', '17:00:00'),
('SCH079', 6, '17:00:00', '18:00:00'),
('SCH080', 6, '18:00:00', '19:00:00'),
('SCH081', 6, '19:00:00', '20:00:00'),
('SCH082', 6, '20:00:00', '21:00:00'),
('SCH083', 6, '21:00:00', '22:00:00'),
('SCH084', 6, '22:00:00', '23:00:00'),
('SCH085', 6, '23:00:00', '00:00:00'),
('SCH086', 7, '07:00:00', '08:00:00'),
('SCH087', 7, '08:00:00', '09:00:00'),
('SCH088', 7, '09:00:00', '10:00:00'),
('SCH089', 7, '10:00:00', '11:00:00'),
('SCH090', 7, '11:00:00', '12:00:00'),
('SCH091', 7, '12:00:00', '13:00:00'),
('SCH092', 7, '13:00:00', '14:00:00'),
('SCH093', 7, '14:00:00', '15:00:00'),
('SCH094', 7, '15:00:00', '16:00:00'),
('SCH095', 7, '16:00:00', '17:00:00'),
('SCH096', 7, '17:00:00', '18:00:00'),
('SCH097', 7, '18:00:00', '19:00:00'),
('SCH098', 7, '19:00:00', '20:00:00'),
('SCH099', 7, '20:00:00', '21:00:00'),
('SCH100', 7, '21:00:00', '22:00:00'),
('SCH101', 7, '22:00:00', '23:00:00'),
('SCH102', 7, '23:00:00', '00:00:00'),
('SCH103', 8, '07:00:00', '08:00:00'),
('SCH104', 8, '08:00:00', '09:00:00'),
('SCH105', 8, '09:00:00', '10:00:00'),
('SCH106', 8, '10:00:00', '11:00:00'),
('SCH107', 8, '11:00:00', '12:00:00'),
('SCH108', 8, '12:00:00', '13:00:00'),
('SCH109', 8, '13:00:00', '14:00:00'),
('SCH110', 8, '14:00:00', '15:00:00'),
('SCH111', 8, '15:00:00', '16:00:00'),
('SCH112', 8, '16:00:00', '17:00:00'),
('SCH113', 8, '17:00:00', '18:00:00'),
('SCH114', 8, '18:00:00', '19:00:00'),
('SCH115', 8, '19:00:00', '20:00:00'),
('SCH116', 8, '20:00:00', '21:00:00'),
('SCH117', 8, '21:00:00', '22:00:00'),
('SCH118', 8, '22:00:00', '23:00:00'),
('SCH119', 8, '23:00:00', '00:00:00');

-- ===== COURSE OFFERINGS =====
INSERT INTO course_offerings (course_offering_id, course_id, faculty_id, instructor, room_id, semester_id, max_capacity, current_capacity) VALUES
('OFFER001', 'COU001', 'FAC001', N'TS. Phạm Văn C', 'R101', 'SEM001', 40, 30),
('OFFER002', 'COU002', 'FAC001', N'ThS. Nguyễn Thị D', 'R102', 'SEM001', 35, 20),
('OFFER003', 'COU003', 'FAC002', N'TS. Lê Văn B', 'R103', 'SEM002', 45, 15),
('OFFER004', 'COU004', 'FAC003', N'TS. Trần Minh E', 'R104', 'SEM003', 30, 25),
('OFFER005', 'COU005', 'FAC005', N'ThS. Nguyễn Hoàng F', 'R105', 'SEM004', 40, 35);

-- ===== COURSE OFFERINGS SCHEDULES =====
INSERT INTO course_offerings_schedules (course_offering_id, schedule_id, start_date, end_date) VALUES
('OFFER001', 'SCH001', '2025-09-05', '2026-01-10'),
('OFFER002', 'SCH002', '2025-09-06', '2026-01-12'),
('OFFER003', 'SCH003', '2026-02-10', '2026-05-25'),
('OFFER004', 'SCH004', '2026-09-10', '2027-01-10'),
('OFFER005', 'SCH005', '2027-02-10', '2027-06-01');

-- -- ===== REGISTRATIONS =====
-- INSERT INTO registrations (registration_id, student_id, course_offering_id, registered_at, status, note) VALUES
-- ('REG001', 'U001', 'OFFER001', NOW(), 'Thành công', N'Đăng ký thành công môn Cấu trúc dữ liệu'),
-- ('REG002', 'U002', 'OFFER002', NOW(), 'Thành công', N'Đăng ký môn Lập trình Web'),
-- ('REG003', 'U003', 'OFFER003', NOW(), 'Thành công', N'Đăng ký môn Mạch điện tử cơ bản'),
-- ('REG004', 'U004', 'OFFER004', NOW(), 'Thành công', N'Đăng ký môn Nguyên lý quản trị'),
-- ('REG005', 'U001', 'OFFER005', NOW(), 'Thất bại', N'Lỗi do vượt số tín chỉ tối đa');
