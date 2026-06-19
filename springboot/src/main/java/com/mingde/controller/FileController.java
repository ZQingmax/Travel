package com.mingde.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.mingde.common.Result;
import com.mingde.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Value("${file.uploadDir:${user.dir}/files}")
    private String uploadDir;

    @Value("${file.allowedImageTypes:image/jpeg,image/png,image/gif,image/webp,image/avif}")
    private String allowedImageTypes;

    @Value("${fileBaseUrl:}")
    private String fileBaseUrl;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String fileName = storeImage(file);
        return Result.success(fileName);
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
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

    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(MultipartFile file) {
        String fileName = storeImage(file);
        String http = fileBaseUrl + "/files/download/";
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("errno", 0);
        resMap.put("data", CollUtil.newArrayList(Dict.create().set("url", http + fileName)));
        return resMap;
    }

    private String storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException("400", "文件不能为空");
        }
        validateImageType(file);
        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + extension;
        try {
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(root);
            Path target = root.resolve(fileName).normalize();
            if (!target.startsWith(root)) {
                throw new CustomException("400", "非法文件名");
            }
            file.transferTo(target);
            return fileName;
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new CustomException("500", "上传失败");
        }
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

    private Path resolveFile(String fileName) {
        String safeName = Paths.get(fileName).getFileName().toString();
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path target = root.resolve(safeName).normalize();
        if (!target.startsWith(root)) {
            throw new CustomException("400", "非法文件名");
        }
        return target;
    }
}
