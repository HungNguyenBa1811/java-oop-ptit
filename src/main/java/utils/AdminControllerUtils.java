package main.java.utils;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import main.java.dto.admin.AdminDashboardCourseRow;
import main.java.dto.admin.AdminDashboardOfferingRow;
import main.java.dto.admin.AdminDashboardUserRow;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Faculty;
import main.java.model.Room;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.model.User;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseServiceImpl;
import main.java.service.impl.FacultyServiceImpl;
import main.java.service.impl.RoomServiceImpl;
import main.java.service.impl.SemesterServiceImpl;

public final class AdminControllerUtils {
    public static Course resolveCourse(CourseOffering offering, CourseServiceImpl courseService) {
        if (offering == null) return null;
        String cid = GenericUtils.safeOr(offering.getCourseId(), null);
        return cid == null ? null : courseService.getCourseById(cid);
    }
    public static int resolveCredits(Course course) {
        return course == null ? 0 : course.getCredits();
    }
    public static String resolveSemesterText(CourseOffering offering, SemesterServiceImpl semesterService) {
        if (offering == null) return "-";
        String semId = offering.getSemesterId();
        if (semId == null) return "-";
        Semester s = semesterService.getSemesterById(semId);
        if (s == null) return semId;
        String term = GenericUtils.safeOr(s.getTerm(), "");
        String year = GenericUtils.safeOr(s.getAcademicYear(), "");
        if (!term.isEmpty() && !year.isEmpty()) return term + "/" + year;
        if (!term.isEmpty()) return term;
        return semId;
    }
    public static String resolveRoomText(CourseOffering offering, RoomServiceImpl roomService) {
        if (offering == null) return "-";
        String rid = offering.getRoomId();
        if (rid == null) return "-";
        Room r = roomService.getRoomById(rid);
        return GenericUtils.safeOr(r != null ? r.getRoomId() : null, rid);
    }
    public static String resolveFacultyText(CourseOffering offering, FacultyServiceImpl facultyService) {
        if (offering == null) return "-";
        String facultyId = offering.getFacultyId();
        if (facultyId == null) return "-";
        Faculty f = facultyService.getFacultyById(facultyId);
        return GenericUtils.safeOr(f != null ? f.getFacultyName() : null, facultyId);
    }
    public static String formatSchedulesForOffering(CourseOffering offering, CourseOfferingScheduleServiceImpl scheduleService, DateTimeFormatter tf) {
        List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
        if (schedules == null || schedules.isEmpty()) return "-";
        return schedules.stream().map(s -> {
            String day;
            switch (s.getDayOfWeek()) {
                case 2: day = "T2"; break;
                case 3: day = "T3"; break;
                case 4: day = "T4"; break;
                case 5: day = "T5"; break;
                case 6: day = "T6"; break;
                case 7: day = "T7"; break;
                case 1:
                case 8: day = "CN"; break;
                default: day = "T?"; break;
            }
            String start = s.getStartTime() != null ? tf.format(s.getStartTime()) : "";
            String end = s.getEndTime() != null ? tf.format(s.getEndTime()) : "";
            String time = (!start.isEmpty() && !end.isEmpty()) ? (start + "-" + end) : (start + end);
            return day + (time.isEmpty() ? "" : " " + time);
        }).collect(Collectors.joining(", "));
    }
    public static AdminDashboardCourseRow toCourseRow(Course course) {
        if (course == null) {
            return new AdminDashboardCourseRow("-", "-", 0, "-");
        }
        return new AdminDashboardCourseRow(
            GenericUtils.safeOr(course.getCourseId(), "-"),
            GenericUtils.safeOr(course.getCourseName(), "-"),
            course.getCredits(),
            GenericUtils.safeOr(course.getFacultyId(), "-")
        );
    }
    public static AdminDashboardUserRow toUserRow(User user) {
        if (user == null) {
            return new AdminDashboardUserRow("-", "-", "", "-", "-", 0);
        }
        return new AdminDashboardUserRow(
            GenericUtils.safeOr(user.getUserId(), "-"),
            GenericUtils.safeOr(user.getUsername(), "-"),
            "",
            GenericUtils.safeOr(user.getFullName(), "-"),
            GenericUtils.safeOr(user.getEmail(), "-"),
            user.getRole()
        );
    }
    public static AdminDashboardOfferingRow toOfferingRow(
        CourseOffering offering,
        CourseServiceImpl courseService,
        SemesterServiceImpl semesterService,
        RoomServiceImpl roomService,
        CourseOfferingScheduleServiceImpl scheduleService
    ) {
        if (offering == null) {
            return new AdminDashboardOfferingRow("-","-","-",0,"-","-","-","-","-","0","0");
        }
        Course course = resolveCourse(offering, courseService);
        String courseId = GenericUtils.safeOr(offering.getCourseId(), "-");
        String courseName = GenericUtils.safeOr(course == null ? null : course.getCourseName(), "-");
        int credits = resolveCredits(course);
        String semesterText = resolveSemesterText(offering, semesterService);
        String roomText = resolveRoomText(offering, roomService);
        String facultyText = resolveFacultyText(offering, new FacultyServiceImpl());
        String instructor = GenericUtils.safeOr(offering.getInstructor(), "-");
        String scheduleText = formatSchedulesForOffering(offering, scheduleService, DateTimeFormatter.ofPattern("HH:mm"));
        String maxCapacity = GenericUtils.safeOr(offering.getMaxCapacity(), "0");
        String currentCapacity = GenericUtils.safeOr(offering.getCurrentCapacity(), "0");
        return new AdminDashboardOfferingRow(
            GenericUtils.safeOr(offering.getCourseOfferingId(), "-"),
            courseId,
            courseName,
            credits,
            instructor,
            facultyText,
            semesterText,
            scheduleText,
            roomText,
            maxCapacity,
            currentCapacity
        );
    }
    public static <T, R> void loadDataFromList(
        ObservableList<R> targetList,
        Supplier<List<T>> supplier,
        Function<T, R> mapper,
        String errorMessage
    ) {
        targetList.clear();
        try {
            List<T> items = supplier.get();
            for (T item : items) {
                targetList.add(mapper.apply(item));
            }
        } catch (Exception e) {
            FXUtils.showError(errorMessage + ": " + e.getMessage());
        }
    }
    private AdminControllerUtils() {}
}