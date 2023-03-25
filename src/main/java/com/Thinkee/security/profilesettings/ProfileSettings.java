package com.Thinkee.security.profilesettings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="profileconfig")
@SequenceGenerator(name = "profileconfig_seq", sequenceName = "hibernate_sequence", allocationSize = 1)
public class ProfileSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private long id;

    @Column(name="userId")
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name="profilesetting")    
    private Settings profilesetting;
}
