package com.mockproject.notary_admin_server.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.mockproject.notary_admin_server.exception.BadRequestException;
import com.mockproject.notary_admin_server.service.UploadCommissionService;
import com.mockproject.notary_admin_server.service.UploadFileService;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final Cloudinary cloudinary;
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    public UploadFileServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @SuppressWarnings("unchecked")
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        validateFile(file);
        String fileName = generateFileName(file);

        Map<String, Object> options = ObjectUtils.asMap(
                "folder", "mockproject/" + folder,
                "public_id", fileName);

        Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), options);

        return uploadResult.get("secure_url").toString();
    }

    private String generateFileName(MultipartFile file) {
        String originalName = file.getOriginalFilename();
        String fileName = originalName.substring(0, originalName.lastIndexOf("."));

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return fileName + "_" + timestamp;
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw BadRequestException.file();
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw BadRequestException.fileTooLarge();
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isBlank()) {
            throw BadRequestException.invalidFile();
        }

        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");

        boolean isValid = allowedExtensions.stream()
                .anyMatch(ext -> fileName.toLowerCase().endsWith("." + ext));

        if (!isValid) {
            throw BadRequestException.invalidFile();
        }
    }
}
