package com.oceanview.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class SecurityUtilTest {

    @Test
    public void testHashPassword_NotNull() {
        String password = "testpassword";
        String hash = SecurityUtil.hashPassword(password);
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    public void testHashPassword_Consistent() {
        String password = "testpassword";
        String hash1 = SecurityUtil.hashPassword(password);
        String hash2 = SecurityUtil.hashPassword(password);
        assertEquals(hash1, hash2);
    }

    @Test
    public void testHashPassword_DifferentPasswords() {
        String password1 = "password1";
        String password2 = "password2";
        String hash1 = SecurityUtil.hashPassword(password1);
        String hash2 = SecurityUtil.hashPassword(password2);
        assertNotEquals(hash1, hash2);
    }

    @Test
    public void testHashPassword_Length() {
        String password = "a";
        String hash = SecurityUtil.hashPassword(password);
        // SHA-256 produces 64 hex characters
        assertEquals(64, hash.length());
    }
}