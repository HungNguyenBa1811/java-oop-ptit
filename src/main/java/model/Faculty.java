package main.java.model;

/**
 * Faculty entity - Khoa
 */
public class Faculty {
    private String facultyId;
    private String facultyName;
    private String phoneNumber;
    private String email;
    private String website;
    private String dean;
    private String description;

    // Constructor mặc định
    public Faculty() {
    }

    public Faculty(String dean, String description, String email, String facultyId, String facultyName, String phoneNumber, String website) {
        this.dean = dean;
        this.description = description;
        this.email = email;
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.phoneNumber = phoneNumber;
        this.website = website;
    }


    // Getters
    public String getFacultyId() {
        return facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "facultyId='" + facultyId + '\'' +
                ", facultyName='" + facultyName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDean() {
        return dean;
    }

    public void setDean(String dean) {
        this.dean = dean;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
