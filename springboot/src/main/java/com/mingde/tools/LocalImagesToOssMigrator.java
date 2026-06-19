package com.mingde.tools;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class LocalImagesToOssMigrator {

    private static final List<String> IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".avif");

    public static void main(String[] args) throws Exception {
        Config config = Config.fromEnv();
        List<Path> images = findImages(config.sourceDir());
        System.out.println("Source directory: " + config.sourceDir().toAbsolutePath().normalize());
        System.out.println("OSS bucket: " + config.bucketName());
        System.out.println("Images found: " + images.size());
        System.out.println("Dry run: " + config.dryRun());

        if (images.isEmpty()) {
            return;
        }

        List<String> manifest = new ArrayList<>();
        manifest.add("local_path,oss_object_key,status");

        OssUploader uploader = config.dryRun() ? null : new OssUploader(config);
        int uploaded = 0;
        int skipped = 0;
        int failed = 0;
        try {
            for (Path image : images) {
                String objectKey = toObjectKey(config.sourceDir(), image, config.objectPrefix());
                try {
                    if (!config.overwrite() && uploader != null && uploader.exists(objectKey)) {
                        skipped++;
                        manifest.add(csv(image) + "," + csv(objectKey) + ",skipped_exists");
                        continue;
                    }
                    if (uploader != null) {
                        uploader.upload(objectKey, image);
                    }
                    uploaded++;
                    manifest.add(csv(image) + "," + csv(objectKey) + "," + (config.dryRun() ? "dry_run" : "uploaded"));
                    System.out.println((config.dryRun() ? "DRY " : "OK  ") + objectKey);
                } catch (Exception e) {
                    failed++;
                    manifest.add(csv(image) + "," + csv(objectKey) + ",failed:" + csv(e.getMessage()));
                    System.err.println("FAIL " + objectKey + " -> " + e.getMessage());
                }
            }
        } finally {
            if (uploader != null) {
                uploader.close();
            }
        }

        Path manifestPath = Paths.get("target", "oss-migration-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + ".csv");
        Files.createDirectories(manifestPath.getParent());
        Files.write(manifestPath, manifest, StandardCharsets.UTF_8);

        System.out.println("Done. uploaded=" + uploaded + ", skipped=" + skipped + ", failed=" + failed);
        System.out.println("Manifest: " + manifestPath.toAbsolutePath().normalize());
    }

    private static List<Path> findImages(Path sourceDir) throws IOException {
        if (!Files.exists(sourceDir) || !Files.isDirectory(sourceDir)) {
            throw new IllegalArgumentException("Source directory does not exist: " + sourceDir.toAbsolutePath().normalize());
        }
        try (Stream<Path> stream = Files.walk(sourceDir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(LocalImagesToOssMigrator::isImage)
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        }
    }

    private static boolean isImage(Path path) {
        String name = path.getFileName().toString().toLowerCase(Locale.ROOT);
        return IMAGE_EXTENSIONS.stream().anyMatch(name::endsWith);
    }

    private static String toObjectKey(Path sourceDir, Path file, String objectPrefix) {
        String relative = sourceDir.toAbsolutePath().normalize().relativize(file.toAbsolutePath().normalize())
                .toString()
                .replace('\\', '/');
        String prefix = normalizePrefix(objectPrefix);
        String objectKey = prefix.isEmpty() ? relative : prefix + "/" + relative;
        if (objectKey.contains("..") || objectKey.startsWith("/")) {
            throw new IllegalArgumentException("Unsafe object key: " + objectKey);
        }
        return objectKey;
    }

    private static String normalizePrefix(String prefix) {
        if (prefix == null || prefix.isBlank()) {
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

    private static String csv(Object value) {
        String text = String.valueOf(value).replace("\"", "\"\"");
        return "\"" + text + "\"";
    }

    private record Config(
            Path sourceDir,
            String endpoint,
            String region,
            String bucketName,
            String accessKeyId,
            String accessKeySecret,
            String objectPrefix,
            boolean overwrite,
            boolean dryRun
    ) {
        static Config fromEnv() {
            Path sourceDir = resolveSourceDir(env("OSS_MIGRATION_SOURCE_DIR", ""));
            String objectPrefix = env("OSS_MIGRATION_OBJECT_PREFIX", "");
            boolean overwrite = Boolean.parseBoolean(env("OSS_MIGRATION_OVERWRITE", "false"));
            boolean dryRun = Boolean.parseBoolean(env("OSS_MIGRATION_DRY_RUN", "false"));
            String endpoint = dryRun ? env("OSS_ENDPOINT", "") : required("OSS_ENDPOINT");
            String region = dryRun ? env("OSS_REGION", "") : required("OSS_REGION");
            String bucketName = dryRun ? env("OSS_BUCKET_NAME", "dry-run") : required("OSS_BUCKET_NAME");
            String accessKeyId = dryRun ? env("OSS_ACCESS_KEY_ID", "") : required("OSS_ACCESS_KEY_ID");
            String accessKeySecret = dryRun ? env("OSS_ACCESS_KEY_SECRET", "") : required("OSS_ACCESS_KEY_SECRET");
            return new Config(sourceDir, endpoint, region, bucketName, accessKeyId, accessKeySecret, objectPrefix, overwrite, dryRun);
        }

        private static Path resolveSourceDir(String configured) {
            if (configured != null && !configured.isBlank()) {
                return Paths.get(configured).toAbsolutePath().normalize();
            }
            Path parentFiles = Paths.get("..", "files").toAbsolutePath().normalize();
            if (Files.isDirectory(parentFiles)) {
                return parentFiles;
            }
            return Paths.get("files").toAbsolutePath().normalize();
        }

        private static String required(String name) {
            String value = env(name, "");
            if (value.isBlank()) {
                throw new IllegalArgumentException("Missing environment variable: " + name);
            }
            return value;
        }

        private static String env(String name, String defaultValue) {
            String value = System.getenv(name);
            return value == null ? defaultValue : value;
        }
    }

    private static final class OssUploader implements AutoCloseable {
        private final String bucketName;
        private final com.aliyun.oss.OSS ossClient;

        private OssUploader(Config config) {
            this.bucketName = config.bucketName();
            com.aliyun.oss.ClientBuilderConfiguration configuration = new com.aliyun.oss.ClientBuilderConfiguration();
            configuration.setProtocol(com.aliyun.oss.common.comm.Protocol.HTTPS);
            configuration.setSignatureVersion(com.aliyun.oss.common.comm.SignVersion.V4);
            com.aliyun.oss.common.auth.DefaultCredentialProvider provider =
                    new com.aliyun.oss.common.auth.DefaultCredentialProvider(config.accessKeyId(), config.accessKeySecret());
            this.ossClient = com.aliyun.oss.OSSClientBuilder.create()
                    .endpoint(config.endpoint())
                    .region(config.region())
                    .credentialsProvider(provider)
                    .clientConfiguration(configuration)
                    .build();
        }

        private boolean exists(String objectKey) {
            return ossClient.doesObjectExist(bucketName, objectKey);
        }

        private void upload(String objectKey, Path image) throws IOException {
            com.aliyun.oss.model.ObjectMetadata metadata = new com.aliyun.oss.model.ObjectMetadata();
            metadata.setContentLength(Files.size(image));
            String contentType = Files.probeContentType(image);
            metadata.setContentType(contentType == null ? "application/octet-stream" : contentType);
            try (InputStream inputStream = Files.newInputStream(image)) {
                ossClient.putObject(bucketName, objectKey, inputStream, metadata);
            }
        }

        @Override
        public void close() {
            ossClient.shutdown();
        }
    }
}