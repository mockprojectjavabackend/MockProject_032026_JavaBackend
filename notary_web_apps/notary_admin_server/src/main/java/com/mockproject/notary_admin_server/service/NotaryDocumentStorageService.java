package com.mockproject.notary_admin_server.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import com.mockproject.notary_admin_server.exception.AppException;
import com.mockproject.notary_admin_server.exception.errorCode.DocumentErrorCode;



/**
 * NotaryDocumentServiceImpl
 *
 * @version 1.0
 *
 *          Modification Logs:
 *          DATE AUTHOR DESCRIPTION
 *          -----------------------------------------------
 *          29-03-2026 AGENT create
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotaryDocumentStorageService {

    @Value("${app.upload.document.dir}")
    private String uploadDir;

    @Value("${app.upload.base-url:http://localhost:8080/server}")
    private String baseUrl;

    @Value("${app.upload.document.max-size-bytes:10485760}")
    private long maxFileSizeBytes;

    private static final List<String> ALLOWED_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "webp",
            "pdf", "doc", "docx", "xlsx", "xls"
    );

    private static final String ALLOWED_LIST = String.join(", ", ALLOWED_EXTENSIONS);

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            log.info("Upload directory ready: {}", Paths.get(uploadDir).toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory!", e);
        }
    }

    public String uploadFile(MultipartFile file, String subDir) {
        validateFile(file);

        String extension  = extractExtension(file.getOriginalFilename());
        String storedName = generateUniqueFilename(extension);
        Path destination  = resolveDestination(subDir, storedName);
        writeFile(file, destination);

        String fileUrl = baseUrl + "/" + subDir + "/" + storedName;
        log.info("Uploaded file [{}] to [{}]", storedName, subDir);
        return fileUrl;
    }

    public String uploadFile(MultipartFile file) {
        return uploadFile(file, uploadDir);
    }

    /* ==================== private helpers ==================== */

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AppException(DocumentErrorCode.UPLOAD_EMPTY_FILE);
        }

        if (file.getSize() > maxFileSizeBytes) {
            throw new AppException(DocumentErrorCode.UPLOAD_INVALID_FILE, Map.of(
                    "reason", "file size " + (file.getSize() / 1024 / 1024)
                            + " MB exceeds limit of " + (maxFileSizeBytes / 1024 / 1024) + " MB"
            ));
        }

        String extension = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new AppException(DocumentErrorCode.UPLOAD_INVALID_FILE, Map.of(
                    "reason", "extension '." + extension + "' is not allowed. Accepted: " + ALLOWED_LIST
            ));
        }
    }

    private String extractExtension(String filename) {
        if (!StringUtils.hasText(filename) || !filename.contains(".")) {
            throw new AppException(DocumentErrorCode.UPLOAD_INVALID_FILE, Map.of(
                    "reason", "filename '" + (filename != null ? filename : "null")
                            + "' has no valid extension. Accepted: " + ALLOWED_LIST
            ));
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private String generateUniqueFilename(String extension) {
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8) + "." + extension;
    }

    private Path resolveDestination(String subDir, String storedName) {
        try {
            Path dir = Paths.get(subDir);
            Files.createDirectories(dir);
            return dir.resolve(storedName);
        } catch (IOException e) {
            log.error("Could not create upload directory [{}]", subDir);
            throw new AppException(DocumentErrorCode.UPLOAD_FAILED, e);
        }
    }

    private void writeFile(MultipartFile file, Path destination) {
        try {
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("File write failed to [{}]: {}", destination, e.getMessage());
            throw new AppException(DocumentErrorCode.UPLOAD_FAILED, e);
        }
    }
}