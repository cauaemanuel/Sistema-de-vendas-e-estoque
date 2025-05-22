package com.sales_management_system.service;

import com.sales_management_system.Repository.UserRepository;
import com.sales_management_system.controller.dto.UserDTO;
import com.sales_management_system.entity.User;
import com.sales_management_system.mapper.UserMap;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserMap userMap;

    public UserService(UserMap userMap, UserRepository userRepository) {
        this.userMap = userMap;
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(UserDTO dto){
        var user = userMap.fromDTO(dto);
        return userRepository.save(user);
    }

    public List<UserDTO> findAll(){
        var users = userRepository.findAll().stream()
                .map(u -> userMap.fromUser(u)).toList();
        if(users.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return users;
    }

    @Transactional
    public UserDTO findById(String id){
        var user = findEntityByid(id);
        return userMap.fromUser(user);
    }

    @Transactional
    public void deleteById(String id){
        var user = findEntityByid(id);
        userRepository.deleteById(user.getId());
    }

    public UserDTO updateUser(UserDTO dto, String id){
        var user = findEntityByid(id);
        var userUpdate = userMap.updateFromDTO(dto, user);
        return userMap.fromUser(userRepository.save(userUpdate));
    }

    protected User findEntityByid(String id) {
        var uuid = UUID.fromString(id);
        return userRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado; Verifique o ID"));
    }
}