package com.mingde.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.ObjectMetadata;
import com.mingde.common.Result;
import com.mingde.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    @Value("${file.storage:local}")
    private String storage;

    @Value("${file.uploadDir:${user.dir}/files}")
    private String uploadDir;

    @Value("${file.allowedImageTypes:image/jpeg,image/png,image/gif,image/webp,image/avif}")
    private String allowedImageTypes;

    @Value("${fileBaseUrl:}")
    private String fileBaseUrl;

    @Value("${file.oss.endpoint:}")
    private String ossEndpoint;

    @Value("${file.oss.region:}")
    private String ossRegion;

    @Value("${file.oss.bucketName:}")
    private String ossBucketName;

    @Value("${file.oss.accessKeyId:}")
    private String ossAccessKeyId;

    @Value("${file.oss.accessKeySecret:}")
    private String ossAccessKeySecret;

    @Value("${file.oss.objectPrefix:images}")
    private String ossObjectPrefix;

    @Value("${file.oss.publicBaseUrl:}")
    private String ossPublicBaseUrl;

    @Value("${file.oss.signedUrlExpirationSeconds:3600}")
    private long signedUrlExpirationSeconds;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String fileName = storeImage(file);
        return Result.success(fileName);
    }

    @GetMapping({"/download/{fileName}", "/download/**"})
    public void download(@PathVariable(required = false) String fileName,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        String objectName = extractDownloadName(fileName, request);
        if (useOss() && !localFileExists(objectName)) {
            redirectToOss(objectName, response);
            return;
        }
        downloadLocal(objectName, response);
    }

    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String fileName = storeImage(file);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", buildDownloadUrl(fileName))));
        return resMap;
    }

    private String storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException("400", "文件不能为空");
        }
        validateImageType(file);
        String extension = getExtension(file.getOriginalFilename());
        String fileName = createObjectName(extension);
        if (useOss()) {
            uploadToOss(fileName, file);
            return fileName;
        }
        return storeLocal(fileName, file);
    }

    private String storeLocal(String fileName, MultipartFile file) {
        try {
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(root);
            Path target = root.resolve(fileName).normalize();
            if (!target.startsWith(root)) {
                throw new CustomException("400", "非法文件名");
            }
            Files.createDirectories(target.getParent());
            file.transferTo(target);
            return fileName;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new CustomException("500", "上传失败");
        }
    }

    private void uploadToOss(String objectName, MultipartFile file) {
        OSS ossClient = createOssClient();
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            ossClient.putObject(ossBucketName, objectName, file.getInputStream(), metadata);
        } catch (OSSException e) {
            log.error("OSS服务端拒绝上传: code={}, requestId={}", e.getErrorCode(), e.getRequestId(), e);
            throw new CustomException("500", "OSS上传失败");
        } catch (ClientException | IOException e) {
            log.error("OSS上传失败", e);
            throw new CustomException("500", "OSS上传失败");
        } finally {
            ossClient.shutdown();
        }
    }

    private void downloadLocal(String fileName, HttpServletResponse response) {
        try {
            Path target = resolveFile(fileName);
            if (!Files.exists(target) || !Files.isRegularFile(target)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String contentType = Files.probeContentType(target);
            response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(target.getFileName().toString(), StandardCharsets.UTF_8));
            response.setContentType(contentType == null ? "application/octet-stream" : contentType);
            try (OutputStream os = response.getOutputStream()) {
                Files.copy(target, os);
                os.flush();
            }
        } catch (Exception e) {
            log.warn("文件下载失败: {}", fileName, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void redirectToOss(String objectName, HttpServletResponse response) {
        try {
            response.sendRedirect(resolveOssUrl(objectName));
        } catch (Exception e) {
            log.warn("OSS文件访问失败: {}", objectName, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String resolveOssUrl(String objectName) {
        String safeName = sanitizeObjectName(objectName);
        if (hasText(ossPublicBaseUrl)) {
            return trimTrailingSlash(ossPublicBaseUrl) + "/" + encodeObjectPath(safeName);
        }
        OSS ossClient = createOssClient();
        try {
            Date expiration = new Date(System.currentTimeMillis() + signedUrlExpirationSeconds * 1000L);
            URL url = ossClient.generatePresignedUrl(ossBucketName, safeName, expiration);
            return url.toString();
        } finally {
            ossClient.shutdown();
        }
    }

    private String buildDownloadUrl(String fileName) {
        if (hasText(fileBaseUrl)) {
            return trimTrailingSlash(fileBaseUrl) + "/files/download/" + encodeObjectPath(fileName);
        }
        return "/files/download/" + encodeObjectPath(fileName);
    }

    private void validateImageType(MultipartFile file) {
        Set<String> allowedTypes = Arrays.stream(allowedImageTypes.split(","))
                .map(String::trim)
                .filter(type -> !type.isEmpty())
                .collect(Collectors.toSet());
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType)) {
            throw new CustomException("400", "仅支持图片文件");
        }
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null) {
            return "";
        }
        String name = Paths.get(originalFilename).getFileName().toString();
        int index = name.lastIndexOf('.');
        if (index < 0) {
            return "";
        }
        String extension = name.substring(index).toLowerCase();
        if (!extension.matches("\\.(jpg|jpeg|png|gif|webp|avif)")) {
            throw new CustomException("400", "不支持的文件扩展名");
        }
        return extension;
    }

    private String createObjectName(String extension) {
        String fileName = UUID.randomUUID() + extension;
        String prefix = normalizeObjectPrefix(ossObjectPrefix);
        String datePath = LocalDate.now().format(DATE_FORMATTER);
        String objectName = prefix.isEmpty() ? datePath + "/" + fileName : prefix + "/" + datePath + "/" + fileName;
        return sanitizeObjectName(objectName);
    }

    private String normalizeObjectPrefix(String prefix) {
        if (!hasText(prefix)) {
            return "";
        }
        String normalized = prefix.trim().replace('\\', '/');
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private String extractDownloadName(String fileName, HttpServletRequest request) {
        if (hasText(fileName)) {
            return sanitizeObjectName(decodeObjectPath(fileName));
        }
        String uri = request.getRequestURI();
        String prefix = request.getContextPath() + "/files/download/";
        int index = uri.indexOf(prefix);
        if (index < 0) {
            throw new CustomException("400", "非法文件名");
        }
        return sanitizeObjectName(decodeObjectPath(uri.substring(index + prefix.length())));
    }

    private String sanitizeObjectName(String objectName) {
        if (!hasText(objectName)) {
            throw new CustomException("400", "文件名不能为空");
        }
        String normalized = objectName.trim().replace('\\', '/');
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        if (normalized.contains("..") || normalized.contains("//")) {
            throw new CustomException("400", "非法文件名");
        }
        return normalized;
    }

    private Path resolveFile(String fileName) {
        String safeName = sanitizeObjectName(fileName);
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path target = root.resolve(safeName).normalize();
        if (!target.startsWith(root)) {
            throw new CustomException("400", "非法文件名");
        }
        return target;
    }

    private boolean localFileExists(String fileName) {
        try {
            Path target = resolveFile(fileName);
            return Files.exists(target) && Files.isRegularFile(target);
        } catch (Exception e) {
            return false;
        }
    }

    private String decodeObjectPath(String objectName) {
        return UriUtils.decode(objectName, StandardCharsets.UTF_8);
    }

    private String encodeObjectPath(String objectName) {
        return Arrays.stream(sanitizeObjectName(objectName).split("/"))
                .map(segment -> UriUtils.encodePathSegment(segment, StandardCharsets.UTF_8))
                .collect(Collectors.joining("/"));
    }
    private OSS createOssClient() {
        validateOssConfig();
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        configuration.setProtocol(Protocol.HTTPS);
        configuration.setSignatureVersion(SignVersion.V4);
        DefaultCredentialProvider provider = new DefaultCredentialProvider(ossAccessKeyId, ossAccessKeySecret);
        return OSSClientBuilder.create()
                .endpoint(ossEndpoint)
                .region(ossRegion)
                .credentialsProvider(provider)
                .clientConfiguration(configuration)
                .build();
    }

    private void validateOssConfig() {
        if (!hasText(ossEndpoint) || !hasText(ossRegion) || !hasText(ossBucketName)
                || !hasText(ossAccessKeyId) || !hasText(ossAccessKeySecret)) {
            throw new CustomException("500", "OSS配置不完整");
        }
    }

    private boolean useOss() {
        return "oss".equalsIgnoreCase(storage);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String trimTrailingSlash(String value) {
        String trimmed = value.trim();
        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}