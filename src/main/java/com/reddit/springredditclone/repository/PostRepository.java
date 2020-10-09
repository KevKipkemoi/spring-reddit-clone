package com.reddit.springredditclone.repository;

import com.reddit.springredditclone.model.Post;
import com.reddit.springredditclone.model.Subreddit;
import com.reddit.springredditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);
    List<Post> findByUser(User user);
}
