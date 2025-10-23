package main.java.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import main.java.config.DatabaseConnection;
import main.java.model.Room;

/**
 * RoomRepository - Quản lý CRUD cho bảng rooms (Phòng học)
 */
public class RoomRepository {
    
    private static final String INSERT_ROOM =
        "INSERT INTO rooms (room_id, capacity, projector, airconditioner, micro_speaker) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_ROOMS =
        "SELECT * FROM rooms ORDER BY room_id";
    
    private static final String SELECT_ROOM_BY_ID =
        "SELECT * FROM rooms WHERE room_id = ?";
    
    private static final String SELECT_ROOMS_BY_MIN_CAPACITY =
        "SELECT * FROM rooms WHERE capacity >= ? ORDER BY capacity";
    
    private static final String SELECT_ROOMS_WITH_PROJECTOR =
        "SELECT * FROM rooms WHERE projector = TRUE ORDER BY room_id";
    
    private static final String UPDATE_ROOM =
        "UPDATE rooms SET capacity = ?, projector = ?, airconditioner = ?, micro_speaker = ? WHERE room_id = ?";
    
    private static final String DELETE_ROOM =
        "DELETE FROM rooms WHERE room_id = ?";

    /**
     * Tạo phòng học mới
     * @param room Room object cần tạo
     * @param projector Có máy chiếu không
     * @param airconditioner Có điều hòa không
     * @param microSpeaker Có micro/loa không
     * @return true nếu tạo thành công
     */
    public boolean createRoom(Room room, boolean projector, boolean airconditioner, boolean microSpeaker) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_ROOM)) {

            stmt.setString(1, room.getRoomId());
            stmt.setInt(2, room.getCapacity());
            stmt.setBoolean(3, projector);
            stmt.setBoolean(4, airconditioner);
            stmt.setBoolean(5, microSpeaker);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Room created: " + room.getRoomId());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy tất cả phòng học
     * @return List danh sách rooms
     */
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_ROOMS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            System.out.println("Found " + rooms.size() + " rooms");
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách room: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Tìm phòng học theo room ID
     * @param roomId Room ID cần tìm
     * @return Room nếu tìm thấy, null nếu không
     */
    public Room findById(String roomId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ROOM_BY_ID)) {
            
            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("Room found: " + roomId);
                return mapResultSetToRoom(rs);
            } else {
                System.out.println("Room not found: " + roomId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm room by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tìm phòng theo sức chứa tối thiểu
     * @param minCapacity Sức chứa tối thiểu
     * @return List danh sách rooms có sức chứa >= minCapacity
     */
    public List<Room> findByMinCapacity(int minCapacity) {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ROOMS_BY_MIN_CAPACITY)) {
            
            stmt.setInt(1, minCapacity);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            System.out.println("Found " + rooms.size() + " rooms with capacity >= " + minCapacity);
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm room by capacity: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Tìm các phòng có máy chiếu
     * @return List danh sách rooms có projector
     */
    public List<Room> findRoomsWithProjector() {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ROOMS_WITH_PROJECTOR);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            System.out.println("Found " + rooms.size() + " rooms with projector");
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm rooms with projector: " + e.getMessage());
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Cập nhật thông tin phòng học
     * @param room Room object với thông tin mới
     * @param projector Có máy chiếu không
     * @param airconditioner Có điều hòa không
     * @param microSpeaker Có micro/loa không
     * @return true nếu update thành công
     */
    public boolean updateRoom(Room room, boolean projector, boolean airconditioner, boolean microSpeaker) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ROOM)) {
            
            stmt.setInt(1, room.getCapacity());
            stmt.setBoolean(2, projector);
            stmt.setBoolean(3, airconditioner);
            stmt.setBoolean(4, microSpeaker);
            stmt.setString(5, room.getRoomId());
            
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Room updated: " + room.getRoomId());
                return true;
            } else {
                System.out.println("No room found to update: " + room.getRoomId());
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi update room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa phòng học
     * @param roomId ID của phòng học cần xóa
     * @return true nếu xóa thành công
     */
    public boolean deleteRoom(String roomId) {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE_ROOM)) {
            
            stmt.setString(1, roomId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                System.out.println("Room deleted: " + roomId);
                return true;
            } else {
                System.out.println("No room found to delete: " + roomId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa room: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Kiểm tra room ID đã tồn tại chưa
     * @param roomId Room ID cần kiểm tra
     * @return true nếu đã tồn tại
     */
    public boolean existsById(String roomId) {
        Room room = findById(roomId);
        return room != null;
    }

    /**
     * Map ResultSet thành Room object
     * Note: Model Room không có projector, airconditioner, micro_speaker
     * Chỉ lấy room_id và capacity
     */
    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        String roomId = rs.getString("room_id");
        int capacity = rs.getInt("capacity");
        // Model Room có roomName và building nhưng schema không có
        // Tạm thời dùng constructor với null values
        
        return new Room(roomId, null, null, capacity);
    }
}
