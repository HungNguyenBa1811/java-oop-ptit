package main.java.utils.helper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import main.java.model.Registration;
import main.java.model.User;
import main.java.service.AuthService;
import main.java.service.impl.RegistrationServiceImpl;
import main.java.utils.FXUtils;

public final class StudentControllerHelper {
    public static String validateStudentAccess(AuthService auth) {
        User current = auth.getCurrentUser();
        if (current == null || !auth.isStudent()) {
            return null; // Invalid access
        }
        return current.getUserId();
    }
    public static Map<String, Registration> buildRegistrationMap(String studentId, RegistrationServiceImpl registrationService) {
        Map<String, Registration> regsByOfferingId = new java.util.HashMap<>();
        try {
            List<Registration> existingRegs = registrationService.getRegistrationsByStudent(studentId);
            for (Registration r : existingRegs) {
                if (r.getCourseOfferingId() != null) {
                    regsByOfferingId.put(r.getCourseOfferingId(), r);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regsByOfferingId;
    }
    public static String findRegistrationId(
        String offeringId, 
        String studentId, 
        Map<String, Registration> regsByOfferingId, 
        RegistrationServiceImpl registrationService) {
        Registration reg = regsByOfferingId.get(offeringId);
        if (reg != null && reg.getRegistrationId() != null) {
            return reg.getRegistrationId();
        }
        // Fallback
        try {
            List<Registration> fallback = registrationService.getRegistrationsByStudent(studentId);
            for (Registration r : fallback) {
                if (offeringId.equals(r.getCourseOfferingId())) {
                    return r.getRegistrationId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String buildSuccessMessage(int regSuccess, int cancelSuccess) {
        StringBuilder successMsg = new StringBuilder();
        if (regSuccess > 0) {
            successMsg.append("Đăng ký thành công ").append(regSuccess).append(" lớp. ");
        }
        if (cancelSuccess > 0) {
            successMsg.append("Hủy đăng ký thành công ").append(cancelSuccess).append(" lớp.");
        }
        return successMsg.length() > 0 ? successMsg.toString().trim() : null;
    }
    public static Set<String> getRegisteredOfferingIds(String studentId, RegistrationServiceImpl registrationService) {
        try {
            if (studentId == null) return Set.of();
            
            List<Registration> regs = registrationService.getRegistrationsByStudent(studentId);
            return regs.stream()
                .filter(r -> r.getCourseOfferingId() != null)
                .map(Registration::getCourseOfferingId)
                .collect(Collectors.toSet());
        } catch (Exception ex) {
            return Set.of();
        }
    }
    public static void showRegistrationResults(String successMessage, StringBuilder errors) {
        if (successMessage != null) {
            FXUtils.showSuccess(successMessage);
        }
        if (errors.length() > 0) {
            FXUtils.showError("Một số thao tác thất bại:\\n" + errors.toString());
        }
    }
    private StudentControllerHelper() {}
}
