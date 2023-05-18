package com.ness.userservice.service;

import com.ness.userservice.dto.UserDTO;
import com.ness.userservice.exception.UserAlreadyExists;
import com.ness.userservice.exception.UserNotFound;
import com.ness.userservice.model.User;
import com.ness.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Integer addUser(UserDTO userDTO) throws UserAlreadyExists {
        Optional<User> optional = userRepository.findById(userDTO.getUserId());
        if(optional.isPresent()){
            User user = optional.orElseThrow(() -> new UserAlreadyExists("Service.USER_ALREADY_EXISTS"));
            return user.getUserId();
        }
        User user = new User();
        userRepository.save(mapToModel(user,userDTO));
        log.info("User with id: {} added.",user.getUserId());
        return user.getUserId();
    }

    @Override
    public UserDTO getUser(Integer userId) throws UserNotFound {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.orElseThrow(() -> new UserNotFound("Service.INVALID_USERID"));
        UserDTO userDTO = new UserDTO();
        log.info("user with id: {} fetched successfully.",userId);
        return mapToDTO(user,userDTO);
    }

    @Override
    public void updateUserDetails(Integer userId, UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.get();
        userRepository.save(mapToModel(user,userDTO));
        log.info("user with id: {} updated.",userId);
    }

    @Override
    public void deleteUser(Integer userId) {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.get();
        userRepository.delete(user);
        log.info("user with id: {} deleted.",userId);
    }
    private User mapToModel(User user, UserDTO userDTO){
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setUserPassword(userDTO.getUserPassword());
        return user;
    }
    private UserDTO mapToDTO(User user, UserDTO userDTO){
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserPassword(user.getUserPassword());
        return userDTO;
    }
}