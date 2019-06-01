package com.szkopinski.todoo.repository;

import com.szkopinski.todoo.model.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

}
