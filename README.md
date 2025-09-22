# System Features – Credit Registration Management

## 1. Student Module


Một dự án Java đơn giản để quản lý việc đăng kí tín chỉ của sinh viên, được xây dựng dựa trên cấu trúc MVC.

**Ràng buộc:**
- Không được đăng ký **trùng môn học** (mỗi sinh viên chỉ chọn một lớp học phần cho cùng một môn).
- Không được đăng ký **trùng lịch học**, tức là không được chọn 2 lớp mà có giao nhau về:
  - `day_of_week` (thứ trong tuần),
  - `start_time–end_time` (tiết/giờ học),
  - khoảng thời gian `start_date–end_date`.
- Không được đăng ký lớp đã đầy.

👉 Hệ thống kiểm tra các ràng buộc này khi sinh viên đăng ký. Nếu vi phạm → từ chối.

### Personal Information
Sinh viên có thể xem thông tin cá nhân (họ tên, lớp, ngành, khoa) được lưu trong hệ thống.

### Class Schedule
Sinh viên có thể xem lịch học cố định theo tuần từ `course_offerings_schedule` của các lớp học phần đã đăng ký.  
Thông tin hiển thị: **ngày bắt đầu – kết thúc, thứ trong tuần, giờ học**.

## Tính năng

- Quản lý danh sách sinh viên.
- Quản lý danh sách môn học.
- Cho phép sinh viên đăng kí môn học.
- Hiển thị danh sách các môn đã đăng kí.
- ... và nhiều tính năng khác!

## 4. Database Notes

**Bảng liên quan:**
- `users`: thông tin chung (username, password, role).
- `students`: thông tin riêng cho sinh viên.
- `courses`: thông tin môn học.
- `course_offerings`: lớp học phần mở theo kỳ/năm học.
- `course_offerings_schedule`: lịch học cố định (ngày bắt đầu/kết thúc, thứ, giờ học).
- `registrations`: lưu đăng ký của sinh viên.

**ERD:** [View Diagram](https://dbdiagram.io/d/erd-oop-db-68d0c6b07c85fb9961bc7bee)