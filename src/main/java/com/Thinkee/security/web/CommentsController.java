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

import com.Thinkee.security.comments.Comments;
import com.Thinkee.security.comments.CommentsRepository;
import com.Thinkee.security.posts.Posts;
import com.Thinkee.security.posts.PostsRepository;
import com.Thinkee.security.profilesettings.ProfileSettings;
import com.Thinkee.security.profilesettings.ProfileSettingsRepository;
import com.Thinkee.security.profilesettings.Settings;
import com.Thinkee.security.user.User;

@RestController
@RequestMapping("/api/v1/posts/{id}/comments")
public class CommentsController {
    
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private ProfileSettingsRepository profileSettingsRepository;
    @Autowired
    private PostsRepository postsRepository;

    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long id, @RequestBody Comments comment, Authentication authentication){        
        try {
            Optional<Posts> post = postsRepository.findById(id);
            Optional<ProfileSettings> userSetting = profileSettingsRepository.findByUserId(post.get().getUserId());
            if(userSetting.get().getProfilesetting() == Settings.PUBLIC){
                User user = (User) authentication.getPrincipal();
                comment.setPostId(id);
                comment.setUserId(user.getId());
                Comments createdComment = commentsRepository.save(comment);
                return ResponseEntity.ok(createdComment);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Could not post comment, User profile is private.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long id){
        try {
            Optional<List<Comments>> optionalComment = commentsRepository.getCommentsByPostId(id);
            if(optionalComment.isPresent()){
                List<Comments> commentList = optionalComment.get();
                return ResponseEntity.ok(commentList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getComment(@PathVariable Long id, @PathVariable Long commentId){
        try {
            Optional<Comments> commentOptional = commentsRepository.getCommentByPostIdAndId(id,commentId);
            if(commentOptional.isPresent()){
                return ResponseEntity.ok(commentOptional.get());
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody Comments comment, Authentication authentication){
        try {
            User user = (User) authentication.getPrincipal();
            Optional<Comments> existingComment = commentsRepository.findByIdAndPostIdAndUserId(commentId, id, user.getId());
            if(existingComment.isPresent()){
                Comments updateComment = existingComment.get();
                updateComment.setMessage(comment.getMessage());
                Comments savedComment = commentsRepository.save(updateComment);
                return ResponseEntity.ok(savedComment);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Comment not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @PathVariable Long commentId, Authentication authentication){
        try {
            User user = (User) authentication.getPrincipal();            
            Optional<Comments> comment = commentsRepository.findById(commentId);            
            if(comment.isPresent() && comment.get().getUserId() == user.getId() && comment.get().getPostId() == id){
                commentsRepository.deleteByIdAndPostIdAndUserId(commentId, id, user.getId());
                Map<String, String> response = new HashMap<>();
                response.put("message", "Comment deleted successfuly!");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Comment not found or unauthorized");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
