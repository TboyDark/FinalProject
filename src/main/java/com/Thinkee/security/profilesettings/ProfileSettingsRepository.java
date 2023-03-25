package com.Thinkee.security.profilesettings;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileSettingsRepository extends JpaRepository<ProfileSettings, Long>{
    Optional<ProfileSettings> findByUserId(Long id);
    Optional<ProfileSettings> findByIdAndUserId(Long id, Long userId);
    void deleteByIdAndUserId(Long id, Long userId);
    Optional<ProfileSettings> findByIdAndUserIdAndProfilesetting(Long id, Long userId, Settings profilesetting);    
}
