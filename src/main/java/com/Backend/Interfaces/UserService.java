package com.Backend.Interfaces;

import java.util.List;

import com.Backend.DTOs.UserDTO;

public interface UserService {
    UserDTO getUserById(String userId); 
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserByEmail(String email);

    UserDTO updateUser(UserDTO userDTO, String userId);

    UserDTO deleteUser(String userId);

    List<UserDTO> getAllUsers();

}
