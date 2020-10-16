package com.reddit.springredditclone.controller;

import com.reddit.springredditclone.dto.CommentsDto;
import com.reddit.springredditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.createComment(commentsDto);
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@RequestParam("postId") Long postId) {
        return status(OK).body(commentService.getCommentByPost(postId));
    }

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@RequestParam("userName") String userName) {
        return status(OK).body(commentService.getCommentsByUser(userName));
    }
}
