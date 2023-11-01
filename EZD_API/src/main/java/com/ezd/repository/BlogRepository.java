package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Blog;

public interface BlogRepository extends JpaRepository<Blog ,Long >{

}
