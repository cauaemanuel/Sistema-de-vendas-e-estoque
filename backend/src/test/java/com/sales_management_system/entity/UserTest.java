package com.sales_management_system.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        user = new User();
    }

    @Test
    void testUserCreation() {
        user.setId(id);
        user.setNome("João Silva");
        user.setEmail("joao@email.com");
        user.setPassword("senha123");

        assertEquals(id, user.getId());
        assertEquals("João Silva", user.getNome());
        assertEquals("joao@email.com", user.getEmail());
        assertEquals("senha123", user.getPassword());
    }

    @Test
    void testUserEqualsAndHashCode() {
        User user1 = new User();
        user1.setId(id);
        user1.setNome("João Silva");
        user1.setEmail("joao@email.com");

        User user2 = new User();
        user2.setId(id);
        user2.setNome("João Silva");
        user2.setEmail("joao@email.com");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testUserToString() {
        user.setNome("João Silva");
        user.setEmail("joao@email.com");
        
        String result = user.toString();
        
        assertNotNull(result);
        assertTrue(result.contains("João Silva"));
        assertTrue(result.contains("joao@email.com"));
    }
}
