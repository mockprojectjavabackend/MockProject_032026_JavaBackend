package com.mockproject.notary_admin_server.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {
    String uploadFile(MultipartFile file, String folder) throws IOException;
}
