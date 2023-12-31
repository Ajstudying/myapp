package com.kaj.myapp_backend.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.util.*;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    @Autowired
    PostRepository repo;

    @GetMapping
    public List<Post> getPostList() {
        List<Post> list = repo.findAll(Sort.by("no").ascending());
        return list;
    }

    @GetMapping(value = "/paging")
    public Page<Post> getPostsPaging(@RequestParam int page, @RequestParam int size){
        System.out.println(page);
        System.out.println(size);

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findAll(pageRequest);
    }
    @GetMapping(value = "/paging/search")
    public Page<Post> getPostsPagingSearch(@RequestParam int page, @RequestParam int size, @RequestParam String query){
        System.out.println(page + "1");
        System.out.println(size + "1");
        System.out.println(query + "1");

        Sort sort = Sort.by("no").descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return repo.findByPetnameContainsOrTitleContainsOrNicknameContains(query, query, query, pageRequest);
    }



    @PostMapping
    public ResponseEntity addPost(@RequestBody Post post){

        System.out.println(post);
//        System.out.println(user);

        if(post.getTitle() == null || post.getTitle().isEmpty() || post.getContent() == null || post.getContent().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        post.setCreatedTime(new Date().getTime());
        Post savedPost = repo.save(post);

        if(savedPost != null){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
//        List<Profile> profile = user.getProfile();
//        for(int i = 0; i < profile.size(); i++){
//            if(post.getPetname().equals(profile.get(i).getPetname())){
//                Post savedPost = repo.save(post);
//                if(savedPost != null){
//
//                    return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
//                }
//
//            }
//        }


        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{no}")
    public ResponseEntity removePost(@PathVariable long no){
        System.out.println(no);

        Optional<Post> post = repo.findPostByNo(no);

        if(!post.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(post.get().getNo() != no){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        repo.deleteById(no);
        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
