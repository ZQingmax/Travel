# Travel

旅游爱好者交互平台，包含 Spring Boot 后端和 Vue 3 前端，支持旅游攻略、游记、路线、旅游商品、订单、评论、收藏、反馈和后台管理等功能。

## 技术栈

- 后端：Spring Boot 3、MyBatis-Plus、MySQL、Redis、JWT
- 前端：Vue 3、Vite、Element Plus
- 文件存储：本地文件目录 / 阿里云 OSS，可通过环境变量切换
- 部署目标：本地开发为主，课程演示可部署到 Ubuntu + Nginx + systemd

## 本地启动

后端：

```bash
cd springboot
mvn spring-boot:run
```

前端：

```bash
cd vue
npm install
npm run dev
```

## 配置说明

后端配置通过环境变量覆盖，避免提交真实密码和第三方密钥：

- `MYSQL_HOST`、`MYSQL_PORT`、`MYSQL_DATABASE`、`MYSQL_USERNAME`、`MYSQL_PASSWORD`
- `REDIS_HOST`、`REDIS_PORT`
- `ALIPAY_APP_ID`、`ALIPAY_APP_PRIVATE_KEY`、`ALIPAY_PUBLIC_KEY`、`ALIPAY_NOTIFY_URL`
- `FILE_STORAGE`：`local` 或 `oss`
- `FILE_UPLOAD_DIR`、`FILE_BASE_URL`
- `OSS_ENDPOINT`、`OSS_REGION`、`OSS_BUCKET_NAME`、`OSS_ACCESS_KEY_ID`、`OSS_ACCESS_KEY_SECRET`、`OSS_OBJECT_PREFIX`、`OSS_PUBLIC_BASE_URL`

前端 API 地址在 `vue/.env.development` 和 `vue/.env.production` 中配置。

## 本次版本更新说明

本次提交主要完成以下优化：

- 将后端分页从 PageHelper 迁移为 MyBatis-Plus 分页，统一返回 `list`、`total`、`pageNum`、`pageSize`、`pages`。
- Mapper 继续保留复杂查询 XML，但分页参数和分页拦截统一由 MyBatis-Plus 处理。
- 增加 MyBatis-Plus 分页配置和通用分页工具，限制最大分页大小，避免一次查询过多数据。
- 文件上传支持本地存储和阿里云 OSS，默认本地模式，OSS 通过环境变量开启。
- 修复图片显示兼容问题：新数据优先保存对象名，前端统一转换为可访问 URL；旧的完整 `/files/download/...` 地址也能继续显示。
- 增加本地图片批量迁移到 OSS 的脚本和说明文档。
- 优化评论树、游记点赞数、旅游商品销量统计等分页查询，减少 N+1 查询和外层 `group by` 压力。
- 清理前端无效 CSS 写法，保证生产构建通过。

验证命令：

```bash
cd springboot
mvn test
mvn package -DskipTests

cd ../vue
npm run build
```

## 版本管理

- `main`：稳定主分支
- 提交信息建议使用 `type: summary`，例如 `feat: optimize pagination and image storage`
- 重要提交建议在 commit body 中说明修改范围、验证命令和注意事项
- 发布节点使用 Git tag，例如 `v0.1.0`

## 相关文档

- OSS 迁移说明：`docs/oss-migration.md`
- 服务器部署说明：`deploy/README.md`
