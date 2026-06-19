# Travel

旅游爱好者交互平台，包含 Spring Boot 后端和 Vue 3 前端。

## 技术栈

- 后端：Spring Boot 3、MyBatis、MySQL、Redis、JWT
- 前端：Vue 3、Vite、Element Plus
- 文件存储：当前为本地文件目录，后续建议迁移到 MinIO

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
- `FILE_BASE_URL`

前端 API 地址在 `vue/.env.development` 和 `vue/.env.production` 中配置。

## 版本管理

- `main`：稳定主分支
- 提交信息建议使用 `type: summary`，例如 `chore: initialize project version control`
- 发布节点使用 Git tag，例如 `v0.1.0`
