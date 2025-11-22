package main.java.model;

/**
 * Student entity - Kế thừa từ User
 * Sinh viên không thể truy cập password (getPassword() là protected ở User)
 */
public class Student extends User {
    private String studentId;
    private String studentClass;
    private String facultyId;
    private String status; // ACTIVE, SUSPENDED, GRADUATED

    // Constructor mặc định
    public Student() {
        super();
    }

    // Constructor đầy đủ
    public Student(String userId, String username, String password, String fullName, String email, String studentId, String studentClass, String facultyId, String status) {
        super(userId, username, password, fullName, email, 0); // role = false (student)
        this.studentId = studentId;
        this.studentClass = studentClass;
        this.facultyId = facultyId;
        this.status = status;
    }

    // Constructor không có User fields (dùng khi đã có userId)
    public Student(String studentId, String studentClass, String facultyId, String status) {
        this.studentId = studentId;
        this.studentClass = studentClass;
        this.facultyId = facultyId;
        this.status = status;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Student{" +
                "userId='" + getUserId() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", studentId='" + studentId + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", facultyId='" + facultyId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
