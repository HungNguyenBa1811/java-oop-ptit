package main.java.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableColumn;

public class AdminDashboardUserRow {
    private final StringProperty userId;
    private final StringProperty username;
    private final StringProperty fullname;
    private final StringProperty email;
    private final IntegerProperty role;
    public AdminDashboardUserRow() {
        this.userId = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.fullname = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.role = new SimpleIntegerProperty();
    }
    public AdminDashboardUserRow(
        String userId,
        String username,
        String password,
        String fullname,
        String email,
        int role
    ) {
        this();
        this.userId.set(userId);
        this.username.set(username);
        this.fullname.set(fullname);
        this.email.set(email);
        this.role.set(role);
    }

    public StringProperty getUserIdProperty() {
        return userId;
    }
    public StringProperty getUsernameProperty() {
        return username;
    }
    public StringProperty getFullnameProperty() {
        return fullname;
    }
    public StringProperty getEmailProperty() {
        return email;
    }
    public IntegerProperty getRoleProperty() {
        return role;
    }

    public String getUserId() {
        return userId.get();
    }
    public String getUsername() {
        return username.get();
    }
    public String getFullname() {
        return fullname.get();
    }
    public String getEmail() {
        return email.get();
    }
    public int getRole() {
        return role.get();
    }

    public static void bindColumns(
        TableColumn<AdminDashboardUserRow, String> colUserUserId,
        TableColumn<AdminDashboardUserRow, String> colUserUsername,
        TableColumn<AdminDashboardUserRow, String> colUserFullname,
        TableColumn<AdminDashboardUserRow, String> colUserEmail,
        TableColumn<AdminDashboardUserRow, String> colUserRole
    ) {
        colUserUserId.setCellValueFactory(cell -> cell.getValue().getUserIdProperty());
        colUserUsername.setCellValueFactory(cell -> cell.getValue().getUsernameProperty());
        colUserFullname.setCellValueFactory(cell -> cell.getValue().getFullnameProperty());
        colUserEmail.setCellValueFactory(cell -> cell.getValue().getEmailProperty());
        colUserRole.setCellValueFactory(cell -> {
            AdminDashboardUserRow row = cell.getValue();
            int roleInt = row.getRole();
            String roleText = (roleInt == 1) ? "Admin" : (roleInt == 0) ? "Sinh viên" : "Không xác định";
            return new SimpleStringProperty(roleText);
        });
    }
}