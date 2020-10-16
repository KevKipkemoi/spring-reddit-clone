package com.reddit.springredditclone.service;

import com.reddit.springredditclone.dto.CommentsDto;
import com.reddit.springredditclone.exception.PostNotFoundException;
import com.reddit.springredditclone.mapper.CommentMapper;
import com.reddit.springredditclone.model.Comment;
import com.reddit.springredditclone.model.NotificationEmail;
import com.reddit.springredditclone.model.Post;
import com.reddit.springredditclone.model.User;
import com.reddit.springredditclone.repository.CommentRepository;
import com.reddit.springredditclone.repository.PostRepository;
import com.reddit.springredditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private static final String POST_URL = "";
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
            .orElseThrow(
                    () -> new PostNotFoundException(commentsDto.getPostId().toString())
            );
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);
        String message = mailContentBuilder.build(
            post.getUser().getUserName() + " posted a comment on your post." + POST_URL
        );
        sendCommentNotification(message, post.getUser());
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
            .stream()
            .map(commentMapper::mapToDto)
            .collect(toList());
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName)
            .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
            .stream()
            .map(commentMapper::mapToDto)
            .collect(toList());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(
            new NotificationEmail(
                user.getUserName() + " commented on your post",
                user.getEmail(), message
            )
        );
    }
}
