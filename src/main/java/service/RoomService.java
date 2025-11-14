package main.java.service;

import java.util.List;
import main.java.model.Room;

/**
 * RoomService - Interface cho business logic của Room
 */
public interface RoomService {
    
    /**
     * Tạo phòng học mới
     * @param room Room object cần tạo
     * @param projector Có máy chiếu không
     * @param airconditioner Có điều hòa không
     * @param microSpeaker Có micro/loa không
     * @return true nếu tạo thành công
     */
    boolean createRoom(Room room, boolean projector, boolean airconditioner, boolean microSpeaker);
    
    /**
     * Lấy tất cả phòng học
     * @return List<Room> danh sách các phòng học
     */
    List<Room> getAllRooms();
    
    /**
     * Lấy phòng học theo ID
     * @param roomId Room ID cần tìm
     * @return Room nếu tìm thấy, null nếu không
     */
    Room getRoomById(String roomId);
    
    /**
     * Lấy danh sách phòng theo sức chứa tối thiểu
     * @param minCapacity Sức chứa tối thiểu
     * @return List<Room> danh sách phòng phù hợp
     */
    List<Room> getRoomsByMinCapacity(int minCapacity);
    
    /**
     * Lấy danh sách phòng có máy chiếu
     * @return List<Room> danh sách phòng có máy chiếu
     */
    List<Room> getRoomsWithProjector();
    
    /**
     * Cập nhật thông tin phòng học
     * @param room Room object cần cập nhật
     * @param projector Có máy chiếu không
     * @param airconditioner Có điều hòa không
     * @param microSpeaker Có micro/loa không
     * @return true nếu cập nhật thành công
     */
    boolean updateRoom(Room room, boolean projector, boolean airconditioner, boolean microSpeaker);
    
    /**
     * Xóa phòng học
     * @param roomId Room ID cần xóa
     * @return true nếu xóa thành công
     */
    boolean deleteRoom(String roomId);
    
    /**
     * Kiểm tra room ID đã tồn tại chưa
     * @param roomId Room ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean existsById(String roomId);
    
    /**
     * Validate capacity (phải > 0)
     * @param capacity Sức chứa cần validate
     * @return true nếu hợp lệ
     */
    boolean isValidCapacity(int capacity);
}
