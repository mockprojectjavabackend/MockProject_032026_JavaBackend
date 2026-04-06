package com.mockproject.notary_admin_server.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.web.multipart.MultipartFile;

public interface UploadCommissionService {
    String uploadCommission(MultipartFile file) throws IOException;

}
