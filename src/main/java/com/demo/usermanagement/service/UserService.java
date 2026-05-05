package com.demo.usermanagement.service;

import org.springframework.stereotype.Service;
import com.demo.usermanagement.model.User;
import com.demo.usermanagement.repository.UserRepository;
import java.util.List;
import com.demo.usermanagement.Dto.UserDTO;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User registerUser(User user) {
        User savedUser = userRepository.save(user);

        // send mail to user after registration
        emailService.sendEmail(savedUser.getEmail());
        return savedUser;
    }

    public boolean authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
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
    
    // public boolean deleteUser(String email) {
    //     return userRepository.findByEmail(email)
    //             .map(user -> {
    //                 userRepository.delete(user);
    //                 return true;
    //             })
    //             .orElse(false);
    // }
}
