package com.oceanview.model;

import static org.junit.Assert.*;
import org.junit.Test;

public class RoomTypeTest {

    @Test
    public void testGettersAndSetters() {
        RoomType roomType = new RoomType();
        roomType.setTypeId(1);
        roomType.setTypeName("Deluxe");
        roomType.setBaseRate(150.0);

        assertEquals(1, roomType.getTypeId());
        assertEquals("Deluxe", roomType.getTypeName());
        assertEquals(150.0, roomType.getBaseRate(), 0.01);
    }

    @Test
    public void testConstructor() {
        // Since no parameterized constructor, test default
        RoomType roomType = new RoomType();
        assertNotNull(roomType);
    }
}