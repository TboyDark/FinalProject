package com.Thinkee.security.token;

import com.Thinkee.security.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="token")
@SequenceGenerator(name = "token_seq", sequenceName = "hibernate_sequence", allocationSize = 1)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name="tokenId")   
    private long id;

    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    
    private boolean expired;
    
    private boolean revoked;

    @ManyToOne 
    @JoinColumn(name = "userId") 
    private User user;
}
