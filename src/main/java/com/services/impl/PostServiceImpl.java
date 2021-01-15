package com.services.impl;

import com.data_base.entities.Post;
import com.data_base.repositories.PostRepository;
import com.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;

    @Override
    public Post findPostById(long postId){
        return this.postRepository.getOne(postId);
    }

    @Override
    @ResponseBody
    public List<Post> findAllPosts(){
        return this.postRepository.findAll();
    }
}
