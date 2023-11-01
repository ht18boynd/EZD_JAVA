package com.ezd.controllers;

import com.ezd.models.Blog;
import com.ezd.repository.BlogRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogRepository blogRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public BlogController(BlogRepository blogRepository, Cloudinary cloudinary) {
        this.blogRepository = blogRepository;
        this.cloudinary = cloudinary;
    }

    @GetMapping("/")
    public Iterable<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @PostMapping("/add")
    public ResponseEntity<Blog> createBlog(@RequestParam("name") String name,
            @RequestParam("title") String title, @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image) {
        try {
            if (name.isEmpty() || title.isEmpty() || content.isEmpty() || image == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            Blog blog = new Blog();
            blog.setName(name);
            blog.setTitle(title);
            blog.setContent(content);
            blog.setImage(imageUrl);

            Blog savedBlog = blogRepository.save(blog);
            return new ResponseEntity<>(savedBlog, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Blog> editBlog(@PathVariable Long id, @RequestParam("name") String name,
            @RequestParam("title") String title, @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        blog.setName(name);
        blog.setTitle(title);
        blog.setContent(content);

        try {
            if (image != null) {
                Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = (String) uploadResult.get("url");
                blog.setImage(imageUrl);
            }

            Blog updatedBlog = blogRepository.save(blog);
            return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        if (blogRepository.existsById(id)) {
            blogRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
