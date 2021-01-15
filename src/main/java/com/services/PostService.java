package com.services;

import com.data_base.entities.Post;

import java.util.List;

public interface PostService {
    Post findPostById(long postId);

    List<Post> findAllPosts();
}
