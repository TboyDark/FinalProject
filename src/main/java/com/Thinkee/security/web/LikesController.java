package com.Thinkee.security.web;

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

import com.Thinkee.security.likes.LikeStatus;
import com.Thinkee.security.likes.Likes;
import com.Thinkee.security.likes.LikesRepository;
import com.Thinkee.security.posts.Posts;
import com.Thinkee.security.posts.PostsRepository;
import com.Thinkee.security.profilesettings.ProfileSettings;
import com.Thinkee.security.profilesettings.ProfileSettingsRepository;
import com.Thinkee.security.profilesettings.Settings;
import com.Thinkee.security.user.User;

@RestController
@RequestMapping("/api/v1/posts/{id}/likes")
public class LikesController {
    
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private ProfileSettingsRepository profileSettingsRepository;
    @Autowired
    private PostsRepository postsRepository;
     
    @PostMapping
    public ResponseEntity<?> putLike(@PathVariable Long id, @RequestBody Likes likes, Authentication authentication){
        try {
            Optional<Posts> post = postsRepository.findById(id);
            Optional<ProfileSettings> userSetting = profileSettingsRepository.findByUserId(post.get().getUserId());
            if(userSetting.get().getProfilesetting() == Settings.PUBLIC){
                User user = (User) authentication.getPrincipal();            
                likes.setUserId(user.getId());
                likes.setPostId(id);
                Likes newLike = likesRepository.save(likes);
                return ResponseEntity.status(HttpStatus.CREATED).body(newLike);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Could not put like, User profile is private.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }        
    }    

    @GetMapping
    public ResponseEntity<?> getPostLikes(@PathVariable Long id){        
        try {
            LikeStatus status = LikeStatus.LIKED;
            List<Likes> likesList = likesRepository.findByPostIdAndStatus(id, status);
            if(!likesList.isEmpty()){                
                return ResponseEntity.ok(likesList); 
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }           
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/totalamount")
    public ResponseEntity<?> getTotalAmountOfLikes(@PathVariable Long id){        
        try {
            LikeStatus status = LikeStatus.LIKED;
            List<Likes> likesList = likesRepository.findByPostIdAndStatus(id, status);
            if(!likesList.isEmpty()){ 
                Long likeAmount = 0L;                
                for(@SuppressWarnings("unused") Likes like : likesList){
                    likeAmount += 1;  
                } 
                Map<String, String> response = new HashMap<>();
                response.put("TotalAmountOfLikes", String.valueOf(likeAmount));
                return ResponseEntity.ok(response);  
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }           
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{likeId}")
    public ResponseEntity<?> updatePost(@PathVariable Long likeId, @PathVariable Long id, @RequestBody Likes updatedLike, Authentication authentication) {

        try {
            User user = (User) authentication.getPrincipal();
            Optional<Likes> existingLike = likesRepository.findByIdAndPostIdAndUserId(likeId, id, user.getId());
            if(existingLike.isPresent()){
                Likes existing = existingLike.get();
                LikeStatus newStatus = updatedLike.getStatus();
                if(existing.getStatus() != newStatus){
                    existing.setStatus(newStatus);
                    Likes savedLike = likesRepository.save(existing);
                    return ResponseEntity.ok(savedLike);
                } else {
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Cannot update to the same like!");
                    return ResponseEntity.ok(response);
                }                
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    protected ResponseEntity<?> deleteLike(@PathVariable Long id, Authentication authentication){
        try {
            User user = (User) authentication.getPrincipal();
            Optional<Likes> existingLike = likesRepository.findByPostIdAndUserId(id, user.getId()); 
            if(existingLike.isPresent()){
                likesRepository.deleteByPostIdAndUserId(id, user.getId());
                Map<String, String> response = new HashMap<>();
            response.put("message", "Like deleted successfuly!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Like not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
}
