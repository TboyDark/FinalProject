package com.Thinkee.security.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.Thinkee.security.posts.Posts;
import com.Thinkee.security.posts.PostsRepository;
import com.Thinkee.security.profilesettings.ProfileSettings;
import com.Thinkee.security.profilesettings.ProfileSettingsRepository;
import com.Thinkee.security.profilesettings.Settings;
import com.Thinkee.security.user.User;



@RestController
@RequestMapping("/api/v1/posts")
public class PostsController {
    
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private ProfileSettingsRepository profileSettingsRepository;
    

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Posts posts, Authentication authentication) {        
        try {
            User user = (User) authentication.getPrincipal();
            posts.setUserId(user.getId());            
            posts.setTimestamp(new Date());            
            Posts savedPost = postsRepository.save(posts);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id, Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            Optional<Posts> posts = postsRepository.findByIdAndUserId(id, user.getId());
            if(posts.isPresent()){
                return ResponseEntity.ok(posts.get());
            }else{
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }        
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Posts updatedPosts, Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            Optional<Posts> existingPost = postsRepository.findByIdAndUserId(id, user.getId());
            if(existingPost.isPresent()){
                Posts post = existingPost.get();
                post.setContent(updatedPosts.getContent());
                Posts savedPost = postsRepository.save(post);
                return ResponseEntity.ok(savedPost);
            }else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Post not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            Optional<Posts> post = postsRepository.findById(id);
            if(post.isPresent() && post.get().getUserId() == user.getId() && post.get().getId() == id){
                postsRepository.deleteByIdAndUserId(id, user.getId());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Post deleted successfuly!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Post not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable Long id) {        
        try { 
            Optional<ProfileSettings> profileSetting = profileSettingsRepository.findByUserId(id);
            if(profileSetting.get().getProfilesetting() == Settings.PUBLIC){
                List<Posts> posts = postsRepository.findByUserId(id);
                return ResponseEntity.ok(posts);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Could not show posts, User profile is private.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }         
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{postId}/users/{userId}")
    public ResponseEntity<?> getPostbyUserId(@PathVariable Long postId, @PathVariable Long userId){  
        try {
            Optional<Posts> postOptional = postsRepository.findByIdAndUserId(postId, userId);
            if(postOptional.isPresent()){
                Optional<ProfileSettings> profileSettings = profileSettingsRepository.findByUserId(userId);
                if(profileSettings.get().getProfilesetting() == Settings.PUBLIC){
                    return ResponseEntity.ok(postOptional.get());
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("error", "Could not show posts, User profile is private.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }   
    }
}
