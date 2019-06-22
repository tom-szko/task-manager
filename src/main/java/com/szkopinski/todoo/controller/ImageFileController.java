package com.szkopinski.todoo.controller;

import com.szkopinski.todoo.controller.ControllerExceptions.FileUploadControllerException;
import com.szkopinski.todoo.controller.Response.UploadFileResponse;
import com.szkopinski.todoo.model.Account;
import com.szkopinski.todoo.model.ImageFile;
import com.szkopinski.todoo.service.AccountService;
import com.szkopinski.todoo.service.Exceptions.FileStorageException;
import com.szkopinski.todoo.service.ImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/images")
public class ImageFileController {

    @Autowired
    private ImageFileService imageFileService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, Principal user) throws FileUploadControllerException {
        ImageFile imageFile;
        try {
            imageFile = imageFileService.saveFile(file);
        } catch (FileStorageException e) {
            throw new FileUploadControllerException("Encountered problem with file upload", e.getCause());
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/downloadFile/")
                .path(String.valueOf(imageFile.getId()))
                .toUriString();

        Optional<Account> currentAccount = accountService.getAccountByUserName(user.getName());
        if (!currentAccount.isPresent()) {
            throw new FileUploadControllerException("Encountered problem connecting image file with account");
        }
        Account account = currentAccount.get();
        account.setAvatar(imageFile);
        accountService.updateAccount(account.getId(), account);

        return new UploadFileResponse(imageFile.getFileName(), fileDownloadUri, imageFile.getFileType(), file.getSize());
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") int fileId) throws FileNotFoundException {
        ImageFile imageFile = imageFileService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageFile.getFileName() + "\"")
                .body(new ByteArrayResource(imageFile.getData()));
    }
}
