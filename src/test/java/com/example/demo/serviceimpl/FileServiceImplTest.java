package com.example.demo.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileServiceImpl fileService;

    @Test
    void testUploadFile_Success() throws Exception {

        String fileName = "test.txt";

        when(multipartFile.isEmpty())
                .thenReturn(false);

        when(multipartFile.getOriginalFilename())
                .thenReturn(fileName);

        when(multipartFile.getInputStream())
                .thenReturn(
                        new ByteArrayInputStream(
                                "Hello Test".getBytes()
                        )
                );

        String result =
                fileService.uploadFile(multipartFile);

        assertEquals(fileName, result);

        Path uploadedFile =
                Path.of("uploads").toAbsolutePath()
                        .resolve(fileName);

        Files.deleteIfExists(uploadedFile);
    }
}