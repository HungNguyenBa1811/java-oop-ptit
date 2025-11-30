package main.java.dto.student;

/**
 * DTO representing a single entry in a timetable cell.
 */
public class TimetableCell {
    private final String courseName;
    private final String instructor;
    private final String room;
    private final String courseOfferingId;
    private final int duration; // Duration in hours (for rowspan)

    public TimetableCell(String courseName, String instructor, String room, String courseOfferingId) {
        this(courseName, instructor, room, courseOfferingId, 1);
    }

    public TimetableCell(String courseName, String instructor, String room, String courseOfferingId, int duration) {
        this.courseName = courseName;
        this.instructor = instructor;
        this.room = room;
        this.courseOfferingId = courseOfferingId;
        this.duration = Math.max(1, duration);
    }

    public String getCourseName() {
        return courseName;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getRoom() {
        return room;
    }

    public String getCourseOfferingId() {
        return courseOfferingId;
    }

    public int getDuration() {
        return duration;
    }

    /**
     * Format for display in timetable cell
     */
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        if (courseName != null && !courseName.isEmpty()) {
            sb.append(courseName);
        }
        if (instructor != null && !instructor.isEmpty()) {
            if (sb.length() > 0) sb.append("\n");
            sb.append(instructor);
        }
        if (room != null && !room.isEmpty()) {
            if (sb.length() > 0) sb.append("\n");
            sb.append("P. ").append(room);
        }
        return sb.toString();
    }
}
