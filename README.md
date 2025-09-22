# Hệ thống đăng kí tín chỉ

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/HungNguyenBa1811/java-oop-ptit/blob/main/LICENSE)

# Description
Một dự án Java đơn giản để quản lý việc đăng kí tín chỉ của sinh viên, được xây dựng dựa trên cấu trúc MVC.

## Mục lục

- [Giới thiệu](#giới-thiệu)
- [Tính năng](#tính-năng)
- [Công nghệ sử dụng](#công-nghệ-sử-dụng)
- [Cấu trúc dự án](#cấu-trúc-dự-án)
- [Cài đặt](#cài-đặt)
- [Sử dụng](#sử-dụng)
- [Đóng góp & Phân công công việc](#đóng-góp--phân-công-công-việc)
- [Giấy phép](#giấy-phép)

## Giới thiệu

Đây là một dự án được phát triển nhằm mục đích học tập và thực hành các khái niệm về lập trình hướng đối tượng (OOP) trong Java, cũng như áp dụng mô hình kiến trúc Model-View-Controller (MVC). Hệ thống cho phép sinh viên đăng kí và quản lý các môn học của mình.

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

## Công nghệ sử dụng

- **Ngôn ngữ:** Java
- **Kiến trúc:** Model-View-Controller (MVC)
- **Cơ sở dữ liệu:** MySQL

## Cấu trúc dự án

Dự án được tổ chức theo mô hình MVC:

- **Model:** Chứa các lớp đại diện cho dữ liệu của ứng dụng (ví dụ: `SinhVien.java`, `MonHoc.java`) và logic nghiệp vụ, tương tác với cơ sở dữ liệu.
- **View:** Chịu trách nhiệm hiển thị dữ liệu cho người dùng (ví dụ: các lớp giao diện người dùng).
- **Controller:** Xử lý các yêu cầu từ người dùng, tương tác với `Model` và cập nhật `View`.

Class Diagram:
<div align="center">
<img align="center" style="width: 40%; height: auto;" src="./ClassDiagram.png">
</div>

Cấu trúc thư mục:
```
.
├── model
│   ├── SinhVien.java
│   └── MonHoc.java
├── view
│   └── AppView.java
├── controller
│   └── AppController.java
└── Main.java
```

Sơ đồ kiến trúc:
```ascii
+---------+         +-------------+         +------------------+
|  View   | <-----> |  Controller | <-----> |      Model       |
+---------+         +-------------+         +------------------+
    ^                     |                      |
    |                     v                      v
Người dùng          Xử lý logic           Database (MySQL)
```

## Cài đặt

1.  Clone repository về máy của bạn:
    ```sh
    git clone https://github.com/HungNguyenBa1811/java-oop-ptit.git
    ```
2.  Mở dự án bằng IDE yêu thích của bạn (ví dụ: IntelliJ, Eclipse).
3.  Biên dịch và chạy dự án.

## Sử dụng

Để chạy ứng dụng, bạn chỉ cần chạy file `Main.java`.

```java
public class Main {
    public static void main(String[] args) {
        // Khởi tạo và chạy ứng dụng
    }
}
```

## Đóng góp & Phân công công việc

| Thành Viên | Vai trò | Contact |
| :--- | :--- | :--- |
| Vũ Hoàng Anh | Leader + BE dev | anhvh189@gmail.com |
| Phan Nguyễn Việt Dũng | BE dev | phannguyenvietdung@gmail.com |
| Nguyễn Bá Hùng | FE dev + UI design | hungba1811@gmail.com |
| Lê Duy Anh | FE dev + UI design | duyanhle9c1@gmail.com |
| Nguyễn Trung Nam | Tester + BA | Trungnam0708qwert@gmail.com |

*Mọi người đều tham gia vào việc thiết kế cơ sở dữ liệu.*

## Giấy phép

Dự án này được cấp phép theo Giấy phép MIT. Xem file `LICENSE` để biết thêm chi tiết.
