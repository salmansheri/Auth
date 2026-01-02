package com.Backend.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.Backend.Auth.Models.User;
import com.Backend.Auth.Repositories.UserRepository;
import com.Backend.DTOs.UserDTO;
import com.Backend.Interfaces.UserService;
import com.Backend.Mappers.UserMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // public UserServiceImpl(UserRepository userRepository) {
    // this.userRepository = userRepository;
    // }

    @Override
    @CacheEvict(value = "Auth::UserList", allEntries = true)
    public UserDTO createUser(UserDTO userDTO) {
        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank() || userDTO.getEmail().isEmpty())
            throw new IllegalArgumentException("Email cannot be null or empty"); 

        if (userRepository.existsByEmail(userDTO.getEmail()))
            throw new IllegalArgumentException("Email already exists"); 


        User user = userMapper.toEntity(userDTO);



        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);

    }

    @Override
    @Cacheable(value = "Auth::UserByEmail", key = "#email")
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        return userMapper.toDTO(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "Auth::UserList", allEntries = true),
            @CacheEvict(value = "Auth::UserById", key = "#userId"),
            @CacheEvict(value = "Auth::UserByEmail", key = "#result.email", condition = "#result != null")
    }

    )
    public UserDTO updateUser(UserDTO userDTO, String userId) {
        UUID userUUID = UUID.fromString(userId);
        User existingUser = userRepository.findById(userUUID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Id: " + userId));
        // existingUser.setId(userUUID);
        userMapper.updateUserFromDTO(userDTO, existingUser);

        User user = userRepository.save(existingUser);

        return userMapper.toDTO(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "Auth::UserList", allEntries = true),
            @CacheEvict(value = "Auth::UserById", key = "#userId"),
            @CacheEvict(value = "Auth::UserByEmail", key = "#result.email", condition = "#result != null")
    }

    )
    public UserDTO deleteUser(String userId) {
        UUID userUUID = UUID.fromString(userId);
        User existingUser = userRepository.findById(userUUID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with Id: " + userId));

        userRepository.delete(existingUser);
        return userMapper.toDTO(existingUser);

    }

    @Override
    @Cacheable(value = "Auth::UserList")
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return userMapper.toDTOList(users);
    }

    @Override
    @Cacheable(value = "Auth::UserById", key = "#userId")
    public UserDTO getUserById(String userId) {
        UUID userUUID = UUID.fromString(userId);
        User user = userRepository.findById(userUUID)
                .orElseThrow(() -> new EntityNotFoundException("user with id " + userId + "Not found!"));

        return userMapper.toDTO(user);

    }

}
