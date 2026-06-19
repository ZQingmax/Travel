# Ubuntu 轻量服务器部署说明

本目录用于课程演示部署。目标环境是阿里云 Ubuntu 轻量服务器，规格 `2 vCPU / 2GiB / 40GiB`。

部署方式：

- Nginx 托管 `vue/dist`
- Nginx 将 `/api/` 和 `/files/` 反向代理到 Spring Boot `9090`
- Spring Boot 用 systemd 管理
- MySQL、Redis 与应用部署在同一台服务器

## 1. 登录服务器

```bash
ssh root@你的服务器公网IP
```

## 2. 拉取项目

```bash
cd /opt
git clone https://github.com/ZQingmax/Travel.git travel
cd /opt/travel
```

如果已经拉取过：

```bash
cd /opt/travel
git pull
```

## 3. 初始化服务器

这一步会安装 JDK 21、Maven、Node.js 20、Nginx、MySQL、Redis，并添加 2G swap。

```bash
sudo bash deploy/scripts/init-server.sh
```

## 4. 配置环境变量

```bash
mkdir -p /etc/travel
cp deploy/env.example /etc/travel/travel.env
nano /etc/travel/travel.env
```

至少修改这些值：

```env
SERVER_NAME=你的服务器公网IP
MYSQL_PASSWORD=你的数据库密码
FILE_BASE_URL=http://你的服务器公网IP
CORS_ALLOWED_ORIGINS=http://你的服务器公网IP
ALIPAY_RETURN_URL=http://你的服务器公网IP/front/orders
```

课程演示可先不用配置支付宝密钥。

## 5. 导入数据库

如果本地已有数据库，先在本地导出：

```bash
mysqldump -uroot -p db_travel > db_travel.sql
```

上传到服务器 `/opt/travel/db_travel.sql` 后执行：

```bash
mysql -utravel -p db_travel < /opt/travel/db_travel.sql
```

如果没有导入表结构，后端启动后访问接口会报数据库表不存在。

## 6. 部署应用

```bash
cd /opt/travel
sudo bash deploy/scripts/deploy-app.sh
```

脚本会完成：

- 创建数据库和用户
- 构建后端 jar
- 构建前端 dist
- 安装并启动 `travel-backend`
- 安装并重载 Nginx 配置

## 7. 阿里云安全组

放行：

```text
22
80
```

不要公网放行：

```text
3306
6379
9090
```

## 8. 验证

浏览器访问：

```text
http://你的服务器公网IP
```

服务器上验证接口：

```bash
curl http://127.0.0.1:9090/notice/selectAll
curl http://你的服务器公网IP/api/notice/selectAll
```

## 9. 常用排错命令

```bash
systemctl status travel-backend
journalctl -u travel-backend -n 100 --no-pager
journalctl -u travel-backend -f

systemctl status nginx
nginx -t
tail -n 100 /var/log/nginx/error.log

free -h
df -h
```

## 10. 更新应用

以后推送新版本后，在服务器执行：

```bash
cd /opt/travel
git pull
sudo bash deploy/scripts/deploy-app.sh
```
