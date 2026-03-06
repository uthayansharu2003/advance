package com.oceanview.model;

public class Room {
    private int roomId;
    private String roomNumber;
    private int typeId;
    private String status;
    
    // Transient field for UI display purposes (Join result)
    private String typeName;

    public Room() {}

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public int getTypeId() { return typeId; }
    public void setTypeId(int typeId) { this.typeId = typeId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
}