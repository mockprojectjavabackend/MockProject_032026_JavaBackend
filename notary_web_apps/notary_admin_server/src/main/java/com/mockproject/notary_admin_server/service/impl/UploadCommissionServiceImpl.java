package com.mockproject.notary_admin_server.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mockproject.notary_admin_server.service.UploadCommissionService;

/**
 * UploadCommissionServiceImpl
 *
 * @version 1.0
 * @date 29-03-2026
 *       <p>
 *       Modification Logs:
 *       DATE AUTHOR DESCRIPTION
 *       -----------------------------------------------
 *       29-03-2026 HuyenThuong handle logic upload notary commission
 */
@Service
public class UploadCommissionServiceImpl implements UploadCommissionService {
    @Value("${thuong.upload-file.base-uri}")
    private String baseURI;

    public String store(MultipartFile file) throws IOException {

        Path basePath = Paths.get(baseURI);
        Files.createDirectories(basePath);

        String fileName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        Path filePath = basePath.resolve(fileName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }
}