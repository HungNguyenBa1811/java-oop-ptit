package main.java.dto.courseOffering;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.model.Schedule;

public class ScheduleRow {
    private final StringProperty scheduleId;
    private final StringProperty display;
    
    public ScheduleRow(Schedule schedule) {
        this.scheduleId = new SimpleStringProperty(schedule.getScheduleId());
        this.display = new SimpleStringProperty(schedule.getFullSchedule());
    }
    
    public ScheduleRow(String day, String timeRange) {
        this.scheduleId = new SimpleStringProperty("MANUAL_" + day + "_" + timeRange);
        this.display = new SimpleStringProperty(day + " " + timeRange);
    }
    
    public String getScheduleId() { return scheduleId.get(); }
    public StringProperty getDisplayProperty() { return display; }

    @Override 
    public String toString() { 
        return display.get(); 
    }
}
