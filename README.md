# System Features – Credit Registration Management

## 1. Student Module

### Credit Registration
Sinh viên đăng nhập và tự đăng ký lớp học phần (`course_offering`).

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

---

## 2. Admin Module

### Registration Management
Quản lý toàn bộ dữ liệu đăng ký tín chỉ.  
**Chức năng:**
- Xem danh sách đăng ký của tất cả sinh viên.
- Sửa thông tin đăng ký (chuyển lớp, cập nhật ghi chú).
- Xóa đăng ký (hủy môn học, chỉnh sửa sai sót).

### Monitoring & Control
- Theo dõi sĩ số từng lớp học phần, đảm bảo không vượt `max_capacity`.
- Có thể tra cứu lịch học (`course_offerings_schedule`) để kiểm soát trùng lịch khi cần.

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