package com.reddit.springredditclone.repository;

import com.reddit.springredditclone.model.Comment;
import com.reddit.springredditclone.model.Post;
import com.reddit.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
    List<Comment> findAllByUser(User user);
}
