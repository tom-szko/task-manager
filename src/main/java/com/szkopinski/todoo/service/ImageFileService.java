package com.szkopinski.todoo.service;

import com.szkopinski.todoo.model.ImageFile;
import com.szkopinski.todoo.repository.ImageFileRepository;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageFileService {

  @Autowired
  private ImageFileRepository imageFileRepository;

  public ImageFile saveFile(MultipartFile file) throws FileStorageException {
    String fileName = StringUtils.cleanPath(file.getName());

    try {
      if (fileName.contains("..")) {
        throw new FileStorageException("File name" + fileName + "contains invalid path sequence.");
      }
      ImageFile imageFile = new ImageFile(fileName, file.getContentType(), file.getBytes());
      return imageFileRepository.save(imageFile);
    } catch (IOException e) {
      throw new FileStorageException("Error saving file: " + fileName, e.getCause());
    }
  }

  public ImageFile getFile(String fileId) throws FileNotFoundException {
    return imageFileRepository.findById(fileId)
        .orElseThrow(() -> new FileNotFoundException("File with id: " + fileId + " could not be found."));
  }
}
