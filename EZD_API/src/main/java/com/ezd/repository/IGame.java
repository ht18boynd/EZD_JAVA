package com.ezd.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ezd.models.Game;
@Repository
public interface IGame extends JpaRepository<Game,Integer>{

}
