package com.ness.userservice.service;

import com.ness.userservice.dto.UserDTO;
import com.ness.userservice.model.User;
import com.ness.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public Integer addUser(UserDTO userDTO){
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setUserPassword(userDTO.getUserPassword());
        userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public UserDTO getUser(Integer userId) {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setUserPassword(user.getUserPassword());
        return userDTO;
    }

    @Override
    public boolean checkIfUserPresent(int userId) {
        Optional<User> optional = userRepository.findById(userId);
        return optional.isPresent();
    }

    @Override
    public void updateUserDetails(Integer userId, UserDTO userDTO) {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.get();
        user.setUserId(userId);
        user.setUserName(userDTO.getUserName());
        user.setUserPassword(userDTO.getUserPassword());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer userid) {
        Optional<User> optional = userRepository.findById(userid);
        User user = optional.get();
        userRepository.delete(user);
    }
}