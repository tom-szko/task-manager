package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.controller.Response.UploadFileResponse;
import com.szkopinski.todoo.model.ImageFile;
import com.szkopinski.todoo.service.Exceptions.FileStorageException;
import com.szkopinski.todoo.service.ImageFileService;
import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/images")
public class ImageFileController {

  @Autowired
  private ImageFileService imageFileService;

  @PostMapping("/uploadFile")
  public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws FileStorageException {
    ImageFile imageFile = imageFileService.saveFile(file);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/downloadFile/")
        .path(imageFile.getId())
        .toUriString();

    return new UploadFileResponse(imageFile.getFileName(), fileDownloadUri, imageFile.getFileType());
  }

  @GetMapping("/downloadFile/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) throws FileNotFoundException {
    ImageFile imageFile = imageFileService.getFile(fileId);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(imageFile.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
        .body(new ByteArrayResource(imageFile.getData()));
  }
}
