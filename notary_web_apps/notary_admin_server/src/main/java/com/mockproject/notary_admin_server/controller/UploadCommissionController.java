package com.mockproject.notary_admin_server.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mockproject.notary_admin_server.dto.ApiSuccessResponse;
import com.mockproject.notary_admin_server.dto.response.UploadFileResponse;
import com.mockproject.notary_admin_server.exception.BadRequestException;
import com.mockproject.notary_admin_server.exception.errorCode.BadRequestErrorCode;
import com.mockproject.notary_admin_server.service.UploadCommissionService;

/**
 * UploadController
 *
 * @version 1.0
 * @date 29-03-2026
 *       <p>
 *       Modification Logs:
 *       DATE AUTHOR DESCRIPTION
 *       -----------------------------------------------
 *       29-03-2026 HuyenThuong upload notary commission
 */
@RestController
@RequestMapping("/api/notaries")
public class UploadCommissionController {
    private final UploadCommissionService uploadService;

    public UploadCommissionController(UploadCommissionService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/{id}/commissions/upload")
    public ResponseEntity<ApiSuccessResponse<UploadFileResponse>> upload(
            @RequestParam MultipartFile file) throws IOException {

        String url = uploadService.uploadCommission(file);
        UploadFileResponse res = new UploadFileResponse(url, Instant.now());
        return ResponseEntity.ok(ApiSuccessResponse.ok(res));
    }
}
