package com.ness.userservice.controller;

import com.ness.userservice.dto.AuthRequest;
import com.ness.userservice.dto.UserDTO;
import com.ness.userservice.exception.UserAlreadyExists;
import com.ness.userservice.exception.UserNotFound;
import com.ness.userservice.model.User;
import com.ness.userservice.repository.UserRepository;
import com.ness.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    //  to add:verify if user present already
    @PostMapping(value = "/CreateUser")
    @ResponseStatus(HttpStatus.CREATED)
    public String addUser(@RequestBody UserDTO userDTO) throws UserAlreadyExists {
        Integer userId = userService.addUser(userDTO);
        return "User with id: " + userId + " added successfully.";
    }




    //  to add: check authorization and need to only show username and limit
    @GetMapping(value = "/GetUser/{userId}")
    @ResponseStatus(HttpStatus.FOUND)
    public UserDTO getUser(@PathVariable Integer userId) throws UserNotFound {
        UserDTO userdto = userService.getUser(userId);
        return userdto;
    }

    //  to add: verify whether user present and authorized
    @PutMapping(value= "UpdateUser/{UserId}")
    @ResponseStatus(HttpStatus.OK)
    public String updateUser(@PathVariable Integer userId, @RequestBody UserDTO userDTO) {
        userService.updateUserDetails(userId,userDTO);
        return "user with id: " + userId + " details updated.";
    }

    @DeleteMapping(value = "DeleteUser/{Userid}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@PathVariable Integer Userid){
        userService.deleteUser(Userid);
        return "User with id: " + Userid + " deleted successfully";
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getUserPassword()));

        if (authenticate.isAuthenticated()) {
            return userService.generateToken(authRequest.getUserName());
        }else{
            throw new RuntimeException("Invalid Access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        userService.validateToken(token);
        return "Token is valid";
    }

}
