package com.vn.jobhunter.controller;

import com.vn.jobhunter.domain.Response.File.ResUploadFileDTO;
import com.vn.jobhunter.service.FileService;
import com.vn.jobhunter.util.annotation.APIMessage;
import com.vn.jobhunter.util.error.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
public class FileController {

    private final FileService FileService;

    @Value("${jobhunter.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        FileService = fileService;
    }

    @PostMapping("/files")
    @APIMessage("Upload single file success")
    public ResponseEntity<ResUploadFileDTO> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {

        if (file == null || file.isEmpty())
            throw new StorageException("File is empty. Please upload a new file");

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException("Invalid file extension only alow " + allowedExtensions.toString());
        }
        this.FileService.createDirectory(baseURI + folder);

        String uploadedFile = this.FileService.store(file, folder);

        ResUploadFileDTO res = new ResUploadFileDTO(uploadedFile, Instant.now());

        return ResponseEntity.ok(res);
    }
}
