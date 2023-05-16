package com.ness.userservice.service;

import com.ness.userservice.dto.UserDTO;

public interface UserService {
    public Integer addUser(UserDTO userDTO);

    UserDTO getUser(Integer userId);

    boolean checkIfUserPresent(int userId);

    void updateUserDetails(Integer userId, UserDTO userDTO);

    void deleteUser(Integer userid);
}
