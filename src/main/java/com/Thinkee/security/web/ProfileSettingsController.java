package com.Thinkee.security.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Thinkee.security.profilesettings.ProfileSettings;
import com.Thinkee.security.profilesettings.ProfileSettingsRepository;
import com.Thinkee.security.profilesettings.Settings;
import com.Thinkee.security.user.User;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileSettingsController {
    
    @Autowired
    private ProfileSettingsRepository profileSettingsRepository;

    @PostMapping
    public ResponseEntity<?> createProfileSetting(@RequestBody ProfileSettings profileSettings, Authentication authentication){
        try {
            User user = (User) authentication.getPrincipal();
            profileSettings.setUserId(user.getId());            
            ProfileSettings settings = profileSettingsRepository.save(profileSettings);
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfileSetting(@PathVariable Long id, @RequestBody ProfileSettings profileSettings, Authentication authentication){
        try {            
            User user = (User) authentication.getPrincipal();                
            Optional<ProfileSettings> existingSettings = profileSettingsRepository.findByIdAndUserId(id, user.getId());
            if(existingSettings.isPresent()){
                ProfileSettings existing = existingSettings.get();
                Settings newSetting = profileSettings.getProfilesetting();
                if (existing.getProfilesetting() != newSetting) {
                    existing.setProfilesetting(newSetting);
                    ProfileSettings updatedSettings = profileSettingsRepository.save(existing);
                    return ResponseEntity.ok(updatedSettings);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Cannot update to the same profile setting!");
                    return ResponseEntity.ok(response);
                }
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Profile setting not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileSetting(@PathVariable Long id, @RequestBody ProfileSettings profileSettings, Authentication authentication ){
        try {
            User user = (User) authentication.getPrincipal();
            Optional<ProfileSettings> settings = profileSettingsRepository.findByIdAndUserId(id, user.getId());
            if(settings.isPresent()){
                return ResponseEntity.ok(settings.get());
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfileSetting(@PathVariable Long id, Authentication authentication){
        try {
            User user = (User) authentication.getPrincipal();
            Optional<ProfileSettings> profileSetting = profileSettingsRepository.findById(id);
            if(profileSetting.isPresent() && profileSetting.get().getUserId() == user.getId() && profileSetting.get().getId() == id){
                profileSettingsRepository.deleteByIdAndUserId(id, user.getId());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Profile settings deleted successfuly!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Profile setting not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        
    }
}
