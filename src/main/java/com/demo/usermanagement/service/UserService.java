package com.demo.usermanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.demo.usermanagement.model.User;
import com.demo.usermanagement.repository.UserRepository;
import java.util.List;
import com.demo.usermanagement.Dto.UserDTO;
import com.demo.usermanagement.Dto.PasswordUpdateDTO;


@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        // send mail to user after registration
        emailService.sendEmail(savedUser.getEmail());
        return savedUser;
    }

    public String authenticateUser(User user) {
        User existingUser = checkUserStatus(user.getEmail());

        // verify the password
        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return "User logged in successfully";
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findByStatusTrue()
        .stream()
        .map(user->new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getCreatedAt()
        )).toList();
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = checkUserStatus(userDTO.getEmail());

        existingUser.setName(userDTO.getName());
        existingUser.setRole(userDTO.getRole());    

        User updatedUser = userRepository.save(existingUser);
        UserDTO updaterUserDTO = new UserDTO(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getEmail(),
            updatedUser.getRole(), 
            updatedUser.getUpdatedAt()
        );
        return updaterUserDTO;
    }

    public User checkUserStatus(String email) {
        User existingUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.isStatus()) {
            throw new RuntimeException("User is inactive");
        }
        return existingUser;
    }

    public String updatePassword(PasswordUpdateDTO passwordUpdateDTO) {
        User existingUser = checkUserStatus(passwordUpdateDTO.getEmail());

        // verify old password
        if(!passwordEncoder.matches(passwordUpdateDTO.getNewPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        // encode the new password 
        existingUser.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNewPassword()));
        userRepository.save(existingUser);
        return "Password updated successfully";
    }

    public UserDTO deleteUser(String email) {
        User existingUser = checkUserStatus(email);

        existingUser.setStatus(false);
        userRepository.save(existingUser);

        UserDTO deletedUserDTO = new UserDTO(
            existingUser.getId(),
            existingUser.getName(),
            existingUser.getEmail(),
            existingUser.getRole(), 
            existingUser.getUpdatedAt()
        );
        return deletedUserDTO;
    }   

}   
