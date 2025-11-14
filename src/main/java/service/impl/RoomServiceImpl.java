package main.java.service.impl;

import java.util.List;
import main.java.model.Room;
import main.java.repository.RoomRepository;
import main.java.service.RoomService;

/**
 * RoomServiceImpl - Implementation của RoomService
 */
public class RoomServiceImpl implements RoomService {
    
    private final RoomRepository repository;
    
    public RoomServiceImpl() {
        this.repository = new RoomRepository();
    }
    
    public RoomServiceImpl(RoomRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public boolean createRoom(Room room, boolean projector, boolean airconditioner, boolean microSpeaker) {
        // Validate
        if (room == null) {
            System.err.println("Room không được null");
            return false;
        }
        
        if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
            System.err.println("Room ID không được rỗng");
            return false;
        }
        
        if (room.getRoomName() == null || room.getRoomName().trim().isEmpty()) {
            System.err.println("Room name không được rỗng");
            return false;
        }
        
        // Validate capacity
        if (!isValidCapacity(room.getCapacity())) {
            System.err.println("Capacity phải lớn hơn 0");
            return false;
        }
        
        // Kiểm tra trùng ID
        if (existsById(room.getRoomId())) {
            System.err.println("Room ID đã tồn tại: " + room.getRoomId());
            return false;
        }
        
        return repository.createRoom(room, projector, airconditioner, microSpeaker);
    }
    
    @Override
    public List<Room> getAllRooms() {
        return repository.findAll();
    }
    
    @Override
    public Room getRoomById(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            System.err.println("Room ID không được rỗng");
            return null;
        }
        return repository.findById(roomId);
    }
    
    @Override
    public List<Room> getRoomsByMinCapacity(int minCapacity) {
        if (!isValidCapacity(minCapacity)) {
            System.err.println("Min capacity phải lớn hơn 0");
            return List.of();
        }
        return repository.findByMinCapacity(minCapacity);
    }
    
    @Override
    public List<Room> getRoomsWithProjector() {
        return repository.findRoomsWithProjector();
    }
    
    @Override
    public boolean updateRoom(Room room, boolean projector, boolean airconditioner, boolean microSpeaker) {
        // Validate
        if (room == null) {
            System.err.println("Room không được null");
            return false;
        }
        
        if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
            System.err.println("Room ID không được rỗng");
            return false;
        }
        
        // Validate capacity
        if (!isValidCapacity(room.getCapacity())) {
            System.err.println("Capacity phải lớn hơn 0");
            return false;
        }
        
        // Kiểm tra tồn tại
        if (!existsById(room.getRoomId())) {
            System.err.println("Room không tồn tại: " + room.getRoomId());
            return false;
        }
        
        return repository.updateRoom(room, projector, airconditioner, microSpeaker);
    }
    
    @Override
    public boolean deleteRoom(String roomId) {
        if (roomId == null || roomId.trim().isEmpty()) {
            System.err.println("Room ID không được rỗng");
            return false;
        }
        
        if (!existsById(roomId)) {
            System.err.println("Room không tồn tại: " + roomId);
            return false;
        }
        
        return repository.deleteRoom(roomId);
    }
    
    @Override
    public boolean existsById(String roomId) {
        Room room = repository.findById(roomId);
        return room != null;
    }
    
    @Override
    public boolean isValidCapacity(int capacity) {
        return capacity > 0;
    }
}
