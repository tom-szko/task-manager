package com.szkopinski.todoo.repository;

import com.szkopinski.todoo.model.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, Integer> {

}
