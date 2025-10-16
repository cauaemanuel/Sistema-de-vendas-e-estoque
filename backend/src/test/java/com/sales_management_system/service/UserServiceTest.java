package com.sales_management_system.service;

import com.sales_management_system.Repository.UserRepository;
import com.sales_management_system.controller.dto.UserDTO;
import com.sales_management_system.entity.User;
import com.sales_management_system.mapper.UserMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMap userMap;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        user = new User("senha123", "joao@email.com", "João Silva");
        user.setId(userId);

        userDTO = new UserDTO("João Silva", "joao@email.com", "senha123");
    }

    @Test
    void testCreate_ShouldCreateUser() {
        when(userMap.fromDTO(userDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(userDTO);

        assertNotNull(result);
        assertEquals("João Silva", result.getNome());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindAll_ShouldReturnListOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);
        when(userMap.fromUser(any(User.class))).thenReturn(userDTO);

        List<UserDTO> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ShouldThrowExceptionWhenEmpty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> userService.findAll());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ShouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMap.fromUser(user)).thenReturn(userDTO);

        UserDTO result = userService.findById(userId.toString());

        assertNotNull(result);
        assertEquals("João Silva", result.nome());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_ShouldThrowExceptionWhenNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.findById(userId.toString()));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteById_ShouldDeleteUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteById(userId.toString());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testUpdateUser_ShouldUpdateAndReturnUser() {
        UserDTO updatedDTO = new UserDTO("João Silva Atualizado", "joao.novo@email.com", "novaSenha");
        User updatedUser = new User("novaSenha", "joao.novo@email.com", "João Silva Atualizado");
        updatedUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMap.updateFromDTO(updatedDTO, user)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMap.fromUser(updatedUser)).thenReturn(updatedDTO);

        UserDTO result = userService.updateUser(updatedDTO, userId.toString());

        assertNotNull(result);
        assertEquals("João Silva Atualizado", result.nome());
        assertEquals("joao.novo@email.com", result.email());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void testFindEntityById_ShouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findEntityByid(userId.toString());

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId);
    }
}
