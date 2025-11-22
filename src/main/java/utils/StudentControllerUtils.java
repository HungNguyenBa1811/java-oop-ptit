package main.java.utils;

import static main.java.utils.GenericUtils.safeParseInt;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import main.java.dto.StudentDashboardRow;
import main.java.model.Course;
import main.java.model.CourseOffering;
import main.java.model.Room;
import main.java.model.Schedule;
import main.java.model.Semester;
import main.java.repository.RoomRepository;
import main.java.repository.SemesterRepository;
import main.java.service.impl.CourseOfferingScheduleServiceImpl;
import main.java.service.impl.CourseServiceImpl;

public final class StudentControllerUtils {
    public static String resolveSemesterText(CourseOffering offering, SemesterRepository semesterRepository) {
        if (offering == null || offering.getSemesterId() == null) return "-";
        Semester semester = semesterRepository.findById(offering.getSemesterId());
        if (semester == null) return offering.getSemesterId();
        String term = semester.getTerm();
        String year = semester.getAcademicYear();
        if (term != null && year != null) {
            return term + "/" + year;
        } else if (term != null) {
            return term;
        } else {
            return offering.getSemesterId();
        }
    }
    public static String resolveRoomText(CourseOffering offering, RoomRepository roomRepository) {
        if (offering == null || offering.getRoomId() == null) return "-";
        
        Room room = roomRepository.findById(offering.getRoomId());
        return (room != null && room.getRoomId() != null) ? room.getRoomId() : offering.getRoomId();
    }
    public static String formatSchedulesForStudent(CourseOffering offering, CourseOfferingScheduleServiceImpl scheduleService) {
        try {
            List<Schedule> schedules = scheduleService.getSchedulesByCourseOfferingId(offering.getCourseOfferingId());
            if (schedules == null || schedules.isEmpty()) return "-";
            
            DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
            return schedules.stream().map(s -> {
                String day = dayOfWeekToString(s.getDayOfWeek());
                String start = s.getStartTime() != null ? tf.format(s.getStartTime()) : "";
                String end = s.getEndTime() != null ? tf.format(s.getEndTime()) : "";
                String time = (!start.isEmpty() && !end.isEmpty()) ? (start + "-" + end) : (start + end);
                return day + (time.isEmpty() ? "" : " " + time);
            }).collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "-";
        }
    }
    public static StudentDashboardRow toStudentRow(
        CourseOffering offering,
        CourseServiceImpl courseService,
        SemesterRepository semesterRepository,
        RoomRepository roomRepository,
        CourseOfferingScheduleServiceImpl scheduleService
    ) {
        if (offering == null) {
            return new StudentDashboardRow("INVALID", "-", "-", "-", "-", "-", "-", "-", "0", "0");
        }
        Course course = offering.getCourseId() != null ? courseService.getCourseById(offering.getCourseId()) : null;
        String courseId = offering.getCourseId() != null ? offering.getCourseId() : "-";
        String courseName = course != null && course.getCourseName() != null ? course.getCourseName() : "-";
        String credits = course != null ? String.valueOf(course.getCredits()) : "-";
        String semesterText = resolveSemesterText(offering, semesterRepository);
        String roomText = resolveRoomText(offering, roomRepository);
        String instructor = offering.getInstructor() != null ? offering.getInstructor() : "-";
        String scheduleText = formatSchedulesForStudent(offering, scheduleService);
        int current = safeParseInt(offering.getCurrentCapacity(), 0);
        int max = safeParseInt(offering.getMaxCapacity(), 0);
        String capacity = String.valueOf(max);
        String remaining = String.valueOf(Math.max(0, max - current));
        return new StudentDashboardRow(
            offering.getCourseOfferingId() != null ? offering.getCourseOfferingId() : "NO_ID",
            courseId,
            courseName,
            credits,
            instructor,
            semesterText,
            scheduleText,
            roomText,
            capacity,
            remaining
        );
    }
    private static String dayOfWeekToString(int dayOfWeek) {
        switch (dayOfWeek) {
            case 2: return "T2";
            case 3: return "T3";
            case 4: return "T4";
            case 5: return "T5";
            case 6: return "T6";
            case 7: return "T7";
            case 8:
            case 1: return "CN";
            default: return "T?";
        }
    }
    private StudentControllerUtils() {}
}
