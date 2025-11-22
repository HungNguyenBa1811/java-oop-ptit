package main.java.dto.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class UserFormData {
        private final StringProperty userId = new SimpleStringProperty();
        private final StringProperty username = new SimpleStringProperty();
        private final StringProperty password = new SimpleStringProperty();
        private final StringProperty fullName = new SimpleStringProperty();
        private final StringProperty email = new SimpleStringProperty();
        private final StringProperty role = new SimpleStringProperty(); // "Admin" | "Sinh viên"
        private final StringProperty studentClass = new SimpleStringProperty();
        private final StringProperty majorId = new SimpleStringProperty();
        private final StringProperty facultyId = new SimpleStringProperty();
        private final StringProperty status = new SimpleStringProperty(); // "Đang học" | "Nghỉ học"
        public StringProperty userIdProperty() { return userId; }
        public StringProperty usernameProperty() { return username; }
        public StringProperty passwordProperty() { return password; }
        public StringProperty fullNameProperty() { return fullName; }
        public StringProperty emailProperty() { return email; }
        public StringProperty roleProperty() { return role; }
        public StringProperty studentClassProperty() { return studentClass; }
        public StringProperty majorIdProperty() { return majorId; }
        public StringProperty facultyIdProperty() { return facultyId; }
        public StringProperty statusProperty() { return status; }
        public String getUserId() { return userId.get(); }
        public String getUsername() { return username.get(); }
        public String getPassword() { return password.get(); }
        public String getFullName() { return fullName.get(); }
        public String getEmail() { return email.get(); }
        public String getRole() { return role.get(); }
        public String getStudentClass() { return studentClass.get(); }
        public String getMajorId() { return majorId.get(); }
        public String getFacultyId() { return facultyId.get(); }
        public String getStatus() { return status.get(); }
    }