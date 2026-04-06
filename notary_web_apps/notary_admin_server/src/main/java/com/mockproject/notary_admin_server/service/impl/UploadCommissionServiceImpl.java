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
    private final UploadFileService uploadFileService;

    public UploadCommissionServiceImpl(UploadFileService uploadFileService) {
        this.uploadFileService = uploadFileService;
    }

    @Override
    public String uploadCommission(MultipartFile file) throws IOException {
        return uploadFileService.uploadFile(file, "commissions");
    }
}