# 旧图片批量上传到阿里云 OSS

这个工具用于把本地 `files` 目录里的旧图片批量上传到 OSS。默认会保留相对路径，例如：

- 本地 `files/1742457727831-t1.jpeg` 上传为 OSS 对象 `1742457727831-t1.jpeg`
- 本地 `files/images/20260619/a.png` 上传为 OSS 对象 `images/20260619/a.png`

这样旧数据库里的 `/files/download/文件名` 或 `/files/download/images/日期/文件名` 仍然能被后端兼容访问，通常不需要立刻改数据库。

## 1. 先打包后端

```powershell
cd D:\Java项目\trave\springboot
mvn clean package -DskipTests
```

## 2. 先演练，不上传

dry-run 不需要 OSS 密钥，只检查本地旧图片会映射到哪些 OSS 对象路径：

```powershell
cd D:\Java项目\trave\springboot
$env:OSS_MIGRATION_DRY_RUN="true"
java "-Dloader.main=com.mingde.tools.LocalImagesToOssMigrator" -cp target\springboot-0.0.1-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher
```

当前项目默认会扫描：

```text
D:\Java项目\trave\files
```

如果旧图片不在这个目录，手动指定：

```powershell
$env:OSS_MIGRATION_SOURCE_DIR="D:\Java项目\trave\files"
```

## 3. 真正上传

把下面参数换成你自己的 OSS 信息。不要把 AccessKey 写进 `application.yml`，也不要提交到 Git。

```powershell
cd D:\Java项目\trave\springboot
$env:OSS_MIGRATION_DRY_RUN="false"
$env:OSS_ENDPOINT="https://oss-cn-hangzhou.aliyuncs.com"
$env:OSS_REGION="cn-hangzhou"
$env:OSS_BUCKET_NAME="你的bucket名"
$env:OSS_ACCESS_KEY_ID="你的AccessKeyId"
$env:OSS_ACCESS_KEY_SECRET="你的AccessKeySecret"
java "-Dloader.main=com.mingde.tools.LocalImagesToOssMigrator" -cp target\springboot-0.0.1-SNAPSHOT.jar org.springframework.boot.loader.launch.PropertiesLauncher
```

上传完成后会生成清单：

```text
springboot/target/oss-migration-时间.csv
```

## 4. 覆盖已有对象

默认如果 OSS 上已有同名对象，会跳过。需要覆盖时：

```powershell
$env:OSS_MIGRATION_OVERWRITE="true"
```

## 5. 不建议设置迁移前缀

工具支持 `OSS_MIGRATION_OBJECT_PREFIX`，但旧图片迁移时建议留空。否则旧数据库里的 `/files/download/文件名` 会和 OSS 对象路径对不上。

## 6. 启用 OSS 运行后端

迁移完成后，本地运行后端时设置：

```powershell
$env:FILE_STORAGE="oss"
$env:OSS_ENDPOINT="https://oss-cn-hangzhou.aliyuncs.com"
$env:OSS_REGION="cn-hangzhou"
$env:OSS_BUCKET_NAME="你的bucket名"
$env:OSS_ACCESS_KEY_ID="你的AccessKeyId"
$env:OSS_ACCESS_KEY_SECRET="你的AccessKeySecret"
$env:OSS_PUBLIC_BASE_URL="https://你的bucket名.oss-cn-hangzhou.aliyuncs.com"
```

如果 Bucket 是私有读，可以不设置 `OSS_PUBLIC_BASE_URL`，后端会生成临时签名 URL。