package model;

/**
 * Student entity - Kế thừa từ User
 * Sinh viên không thể truy cập password (getPassword() là protected ở User)
 */
public class Student extends User {
    private String studentId;
    private String studentClass;
    private String majorId;
    private String status; // ACTIVE, SUSPENDED, GRADUATED

    // Constructor mặc định
    public Student() {
        super();
    }

    // Constructor đầy đủ
    public Student(String userId, String username, String password, String fullName,
                   String email, String studentId, String studentClass, String majorId, String status) {
        super(userId, username, password, fullName, email, false); // role = false (student)
        this.studentId = studentId;
        this.studentClass = studentClass;
        this.majorId = majorId;
        this.status = status;
    }

    // Constructor không có User fields (dùng khi đã có userId)
    public Student(String studentId, String studentClass, String majorId, String status) {
        this.studentId = studentId;
        this.studentClass = studentClass;
        this.majorId = majorId;
        this.status = status;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getMajorId() {
        return majorId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", studentId='" + studentId + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", majorId='" + majorId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
