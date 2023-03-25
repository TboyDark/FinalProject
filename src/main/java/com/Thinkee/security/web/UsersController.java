package com.Thinkee.security.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Thinkee.security.user.User;
import com.Thinkee.security.user.UserRepository;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
        
    @PutMapping("/{id}/email")
    public ResponseEntity<?> updateUserEmail(@PathVariable Long id, @RequestBody User updateUser, Authentication authentication) {
        try {            
            User user = (User) authentication.getPrincipal();                        
            Optional<User> optionalUser = userRepository.findById(id);
            if(!optionalUser.isPresent() && user.getId() != id) {                
                Map<String, String> response = new HashMap<>();
                response.put("error", "User not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            User existingUser = optionalUser.get();  
            existingUser.setEmail(updateUser.getEmail());
            User savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long id, @RequestBody User newPassword, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();            
            Optional<User> optionalUser = userRepository.findById(id);
            if (!optionalUser.isPresent() && user.getId() != id) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "User not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            User existingUser = optionalUser.get();
            String encPassword = passwordEncoder.encode(newPassword.getPassword());
            existingUser.setPassword(encPassword);
            User savedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
