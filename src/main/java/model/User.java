package main.java.model;

public class User{
    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private int role;

    public User(){}
    
    public User(String userId, String username, String password, String fullName, String email, int role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}