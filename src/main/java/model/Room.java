package main.java.model;

/**
 * Room entity - Phòng học
 */
public class Room {
    private String roomId;
    private int capacity;
    private boolean projector;
    private boolean airconditioner;
    private boolean microSpeaker;

    // Constructor mặc định
    public Room() {
    }

    // Constructor đầy đủ
    public Room(String roomId, int capacity, boolean projector, boolean airconditioner, boolean microSpeaker) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.projector = projector;
        this.airconditioner = airconditioner;
        this.microSpeaker = microSpeaker;
    }

    // Getters
    public String getRoomId() {
        return roomId;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isProjector() {
        return projector;
    }

    public boolean isAirconditioner() {
        return airconditioner;
    }

    public boolean isMicroSpeaker() {
        return microSpeaker;
    }

    // Setters
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setProjector(boolean projector) {
        this.projector = projector;
    }

    public void setAirconditioner(boolean airconditioner) {
        this.airconditioner = airconditioner;
    }

    public void setMicroSpeaker(boolean microSpeaker) {
        this.microSpeaker = microSpeaker;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", capacity=" + capacity +
                ", projector=" + projector +
                ", airconditioner=" + airconditioner +
                ", microSpeaker=" + microSpeaker +
                '}';
    }
}
