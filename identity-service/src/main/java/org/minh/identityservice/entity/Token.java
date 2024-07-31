package org.minh.identityservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String token;
    @Column
    private boolean expired;
    @Column
    private String tokenType;
    @Column
    private LocalDateTime experiationDate;
    @Column
    private LocalDateTime refreshExpirationDate;
    @Column
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
    @Column
    private boolean isMobile;
    @Column
    private String refreshToken;

}
