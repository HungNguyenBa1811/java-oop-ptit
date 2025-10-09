package model;

/**
 * Room entity - Phòng học
 */
public class Room {
    private String roomId;
    private String roomName;
    private String building;
    private int capacity;

    // Constructor mặc định
    public Room() {
    }

    // Constructor đầy đủ
    public Room(String roomId, String roomName, String building, int capacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.building = building;
        this.capacity = capacity;
    }

    // Getters
    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", building='" + building + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
