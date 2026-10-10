package com.example.demo.serviceimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Path uploadDirectory =
            Paths.get("uploads").toAbsolutePath().normalize();

    public FileServiceImpl() {

        try {
            Files.createDirectories(uploadDirectory);

            log.info("Upload directory: {}", uploadDirectory);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Could not create upload directory", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new IllegalArgumentException(
                    "File cannot be empty");
        }

        String originalFileName =
                file.getOriginalFilename();

        if (originalFileName == null
                || originalFileName.isBlank()) {

            throw new IllegalArgumentException(
                    "Invalid file name");
        }

        String fileName =
                Paths.get(originalFileName)
                        .getFileName()
                        .toString();

        try {

            Path targetPath =
                    uploadDirectory.resolve(fileName)
                            .normalize();

            if (!targetPath.startsWith(uploadDirectory)) {

                throw new IllegalArgumentException(
                        "Invalid file name");
            }

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            log.info(
                    "File uploaded successfully: {}",
                    fileName
            );

            return fileName;

        } catch (IOException e) {

            log.error(
                    "File upload failed: {}",
                    fileName,
                    e
            );

            throw new RuntimeException(
                    "File upload failed", e);
        }
    }

    @Override
    public Resource downloadFile(String fileName) {

        try {

            Path filePath =
                    uploadDirectory
                            .resolve(fileName)
                            .normalize();

            if (!filePath.startsWith(uploadDirectory)) {

                throw new IllegalArgumentException(
                        "Invalid file name");
            }

            Resource resource =
                    new UrlResource(filePath.toUri());

            if (!resource.exists()
                    || !resource.isReadable()) {

                throw new ResourceNotFoundException(
                        "File not found: " + fileName);
            }

            log.info(
                    "Downloading file: {}",
                    fileName
            );

            return resource;

        } catch (IOException e) {

            log.error(
                    "Could not read file: {}",
                    fileName,
                    e
            );

            throw new ResourceNotFoundException(
                    "File not found: " + fileName);
        }
    }
}