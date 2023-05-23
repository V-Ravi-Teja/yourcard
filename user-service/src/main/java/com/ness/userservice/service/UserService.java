package com.ness.userservice.service;

import com.ness.userservice.dto.UserDTO;
import com.ness.userservice.exception.UserAlreadyExists;
import com.ness.userservice.exception.UserNotFound;

public interface UserService {
    public Integer addUser(UserDTO userDTO) throws UserAlreadyExists;

    UserDTO getUser(Integer userId) throws UserNotFound;

    void updateUserDetails(Integer userId, UserDTO userDTO);

    void deleteUser(Integer userid);

    public String generateToken(String userName);

    public void validateToken(String token);
}
