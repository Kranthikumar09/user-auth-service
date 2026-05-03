package com.authservice.user_auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity()
@Table(name = "users")
@Data()
@Builder()
@NoArgsConstructor()
@AllArgsConstructor()
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, length = 50)
    String username;

    @Column(unique = true, nullable = false, length = 100)
    String email;

    @Column(nullable = false, length = 255)
    String password;

    @Builder.Default
    @Column(nullable = false, length = 20)
    String role = "USER";

    @Builder.Default
    @Column(nullable = false)
    boolean enabled = true;

    @Column(updatable = false)
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }


}
