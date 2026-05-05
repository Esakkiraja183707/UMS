package com.demo.usermanagement.Dto;
import java.sql.Timestamp;
import com.demo.usermanagement.model.User.Role;


public class UserDTO {
     private Long id;
    private String name;
    private String email;
    private Role role;
    private Timestamp createdAt;

    public UserDTO(Long id, String name, String email, Role role, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public Timestamp getCreatedAt() { return createdAt; }
}
