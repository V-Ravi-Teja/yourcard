package com.ness.userservice.controller;

import com.ness.userservice.dto.UserDTO;
import com.ness.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/yourcard/user")
public class UserController {
    @Autowired
    UserService userService;
    //  to add:verify if user present already
    @PostMapping(value = "/CreateUser")
    public String addUser(@RequestBody UserDTO userDTO){
        Integer userId = userService.addUser(userDTO);
        return "User with id: " + userId + " added successfully.";
    }
    //  to add: check authorization and need to only show username and limit
    @GetMapping(value = "/GetUser/{userId}")
    public UserDTO getUser(@PathVariable Integer userId) {
        UserDTO userdto = userService.getUser(userId);
        return userdto;
    }
    //  to add: verify whether user present and authorized
    @PutMapping(value= "UpdateUser/{UserId}")
    public String updateCustomer(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        userService.updateUserDetails(userId,userDTO);
        return "user with id: " + userId + " details updated.";
    }
    @DeleteMapping(value = "DeleteUser/{Userid}")
    public String deleteCustomer(@PathVariable Integer Userid){
        userService.deleteUser(Userid);
        return "User with id: " + Userid + " deleted successfully";
    }
}
