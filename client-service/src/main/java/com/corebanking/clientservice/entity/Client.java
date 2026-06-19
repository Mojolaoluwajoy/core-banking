package com.corebanking.clientservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

       @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

      @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

      @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private LocalDate dateOfBirth;

      @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientStatus status = ClientStatus.PENDING;

      @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}