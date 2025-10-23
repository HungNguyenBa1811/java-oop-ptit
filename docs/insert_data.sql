USE course_registration;

-- ===== USERS =====
INSERT INTO users VALUES
('U001', 'nguyenvana', '123456', N'Nguyễn Văn A', 'vana@student.university.edu', 0, NOW(), NOW()),
('U002', 'tranthib', '123456', N'Trần Thị B', 'thib@student.university.edu', 0, NOW(), NOW()),
('U003', 'lehoangc', '123456', N'Lê Hoàng C', 'hoangc@student.university.edu', 0, NOW(), NOW()),
('U004', 'phamminhd', '123456', N'Phạm Minh D', 'minhd@student.university.edu', 0, NOW(), NOW()),
('U005', 'admin', 'admin123', N'Quản trị viên', 'admin@university.edu', 1, NOW(), NOW());

-- ===== FACULTIES =====
INSERT INTO faculties VALUES
('FAC001', N'Khoa Công nghệ Thông tin', '0123456789', 'cntt@university.edu', 'http://cntt.university.edu', N'PGS.TS Nguyễn Văn A', NOW(), NOW()),
('FAC002', N'Khoa Điện tử Viễn thông', '0987654321', 'dtvt@university.edu', 'http://dtvt.university.edu', N'TS. Lê Văn B', NOW(), NOW()),
('FAC003', N'Khoa Kinh tế', '0911223344', 'kinhte@university.edu', 'http://kinhte.university.edu', N'TS. Nguyễn Thị C', NOW(), NOW()),
('FAC004', N'Khoa Cơ khí', '0933445566', 'cokhi@university.edu', 'http://cokhi.university.edu', N'TS. Đỗ Văn D', NOW(), NOW()),
('FAC005', N'Khoa Ngôn ngữ Anh', '0944556677', 'nnanh@university.edu', 'http://nnanh.university.edu', N'TS. Lê Thị E', NOW(), NOW());

-- ===== MAJORS =====
INSERT INTO majors VALUES
('MAJ001', N'Công nghệ Thông tin', 'FAC001', 'Cử nhân', 4, 'Ngành học về CNTT và phần mềm', NOW(), NOW()),
('MAJ002', N'Kỹ thuật phần mềm', 'FAC001', 'Kỹ sư', 4, 'Chuyên ngành phát triển phần mềm', NOW(), NOW()),
('MAJ003', N'Điện tử Viễn thông', 'FAC002', 'Cử nhân', 4, 'Nghiên cứu tín hiệu và viễn thông', NOW(), NOW()),
('MAJ004', N'Quản trị kinh doanh', 'FAC003', 'Cử nhân', 4, 'Đào tạo nhà quản lý doanh nghiệp', NOW(), NOW()),
('MAJ005', N'Ngôn ngữ Anh thương mại', 'FAC005', 'Cử nhân', 4, 'Tiếng Anh ứng dụng trong kinh doanh', NOW(), NOW());

-- ===== STUDENTS =====
INSERT INTO students VALUES
('U001', 'CNTT-K65A', 'MAJ001', 'Đang học', NOW(), NOW()),
('U002', 'KTPM-K65B', 'MAJ002', 'Đang học', NOW(), NOW()),
('U003', 'DTVT-K65A', 'MAJ003', 'Đang học', NOW(), NOW()),
('U004', 'QTKD-K65A', 'MAJ004', 'Đang học', NOW(), NOW());

-- ===== COURSES =====
INSERT INTO courses VALUES
('COU001', N'Cấu trúc dữ liệu và Giải thuật', 3, 'FAC001', NOW(), NOW()),
('COU002', N'Lập trình Web', 3, 'FAC001', NOW(), NOW()),
('COU003', N'Mạch điện tử cơ bản', 3, 'FAC002', NOW(), NOW()),
('COU004', N'Nguyên lý quản trị', 2, 'FAC003', NOW(), NOW()),
('COU005', N'Tiếng Anh giao tiếp nâng cao', 2, 'FAC005', NOW(), NOW());

-- ===== ROOMS =====
INSERT INTO rooms VALUES
('R101', 40, TRUE, TRUE, TRUE, NOW(), NOW()),
('R102', 35, TRUE, TRUE, FALSE, NOW(), NOW()),
('R103', 50, TRUE, TRUE, TRUE, NOW(), NOW()),
('R104', 30, FALSE, TRUE, FALSE, NOW(), NOW()),
('R105', 45, TRUE, TRUE, TRUE, NOW(), NOW());

-- ===== SEMESTERS =====
INSERT INTO semesters VALUES
('SEM001', 'Fall 2025', '2025-2026', '2025-09-01', '2026-01-15', NOW(), NOW()),
('SEM002', 'Spring 2026', '2025-2026', '2026-02-01', '2026-06-01', NOW(), NOW()),
('SEM003', 'Fall 2026', '2026-2027', '2026-09-01', '2027-01-15', NOW(), NOW()),
('SEM004', 'Spring 2027', '2026-2027', '2027-02-01', '2027-06-01', NOW(), NOW()),
('SEM005', 'Summer 2027', '2026-2027', '2027-07-01', '2027-08-15', NOW(), NOW());

-- ===== SCHEDULES =====
INSERT INTO schedules VALUES
('SCH001', 2, '08:00:00', '10:00:00', NOW(), NOW()),
('SCH002', 3, '09:00:00', '11:00:00', NOW(), NOW()),
('SCH003', 4, '13:00:00', '15:00:00', NOW(), NOW()),
('SCH004', 5, '15:00:00', '17:00:00', NOW(), NOW()),
('SCH005', 6, '08:00:00', '10:00:00', NOW(), NOW());

-- ===== COURSE OFFERINGS =====
INSERT INTO course_offerings VALUES
('OFFER001', 'COU001', 'MAJ001', N'TS. Phạm Văn C', 'R101', 'SEM001', 40, 30, NOW(), NOW()),
('OFFER002', 'COU002', 'MAJ002', N'ThS. Nguyễn Thị D', 'R102', 'SEM001', 35, 20, NOW(), NOW()),
('OFFER003', 'COU003', 'MAJ003', N'TS. Lê Văn B', 'R103', 'SEM002', 45, 15, NOW(), NOW()),
('OFFER004', 'COU004', 'MAJ004', N'TS. Trần Minh E', 'R104', 'SEM003', 30, 25, NOW(), NOW()),
('OFFER005', 'COU005', 'MAJ005', N'ThS. Nguyễn Hoàng F', 'R105', 'SEM004', 40, 35, NOW(), NOW());

-- ===== COURSE OFFERINGS SCHEDULES =====
INSERT INTO course_offerings_schedules (course_offering_id, schedule_id, start_date, end_date) VALUES
('OFFER001', 'SCH001', '2025-09-05', '2026-01-10'),
('OFFER002', 'SCH002', '2025-09-06', '2026-01-12'),
('OFFER003', 'SCH003', '2026-02-10', '2026-05-25'),
('OFFER004', 'SCH004', '2026-09-10', '2027-01-10'),
('OFFER005', 'SCH005', '2027-02-10', '2027-06-01');

-- ===== REGISTRATIONS =====
INSERT INTO registrations VALUES
('REG001', 'U001', 'OFFER001', NOW(), 'Thành công', N'Đăng ký thành công môn Cấu trúc dữ liệu', NOW(), NOW()),
('REG002', 'U002', 'OFFER002', NOW(), 'Thành công', N'Đăng ký môn Lập trình Web', NOW(), NOW()),
('REG003', 'U003', 'OFFER003', NOW(), 'Thành công', N'Đăng ký môn Mạch điện tử cơ bản', NOW(), NOW()),
('REG004', 'U004', 'OFFER004', NOW(), 'Thành công', N'Đăng ký môn Nguyên lý quản trị', NOW(), NOW()),
('REG005', 'U001', 'OFFER005', NOW(), 'Thất bại', N'Lỗi do vượt số tín chỉ tối đa', NOW(), NOW());