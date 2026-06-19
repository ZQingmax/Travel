#!/usr/bin/env bash
set -euo pipefail

if [ "$(id -u)" -ne 0 ]; then
  echo "Please run as root: sudo bash deploy/scripts/deploy-app.sh"
  exit 1
fi

ENV_FILE=/etc/travel/travel.env
if [ ! -f "$ENV_FILE" ]; then
  echo "Missing $ENV_FILE. Create it from deploy/env.example first."
  exit 1
fi

set -a
# shellcheck disable=SC1090
. "$ENV_FILE"
set +a

APP_DIR=${APP_DIR:-/opt/travel}
SERVER_PORT=${SERVER_PORT:-9090}
MYSQL_DATABASE=${MYSQL_DATABASE:-db_travel}
MYSQL_USERNAME=${MYSQL_USERNAME:-travel}
MYSQL_PASSWORD=${MYSQL_PASSWORD:-Travel@123456}
FILE_UPLOAD_DIR=${FILE_UPLOAD_DIR:-/opt/travel/files}

echo "[1/8] Checking application directory: $APP_DIR"
if [ ! -d "$APP_DIR/.git" ]; then
  echo "$APP_DIR is not a git checkout. Clone first:"
  echo "  git clone https://github.com/ZQingmax/Travel.git $APP_DIR"
  exit 1
fi

echo "[2/8] Creating upload directory"
mkdir -p "$FILE_UPLOAD_DIR"

echo "[3/8] Preparing MySQL database and user"
MYSQL_ROOT_ARGS=(-uroot)
if [ -n "${MYSQL_ROOT_PASSWORD:-}" ]; then
  MYSQL_ROOT_ARGS=(-uroot "-p${MYSQL_ROOT_PASSWORD}")
fi
mysql "${MYSQL_ROOT_ARGS[@]}" <<SQL
CREATE DATABASE IF NOT EXISTS \`${MYSQL_DATABASE}\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS '${MYSQL_USERNAME}'@'localhost' IDENTIFIED BY '${MYSQL_PASSWORD}';
CREATE USER IF NOT EXISTS '${MYSQL_USERNAME}'@'127.0.0.1' IDENTIFIED BY '${MYSQL_PASSWORD}';
GRANT ALL PRIVILEGES ON \`${MYSQL_DATABASE}\`.* TO '${MYSQL_USERNAME}'@'localhost';
FLUSH PRIVILEGES;
SQL

echo "[4/8] Building backend"
cd "$APP_DIR/springboot"
mvn clean package -DskipTests
if [ ! -f "$APP_DIR/springboot/target/springboot-0.0.1-SNAPSHOT.jar" ]; then
  echo "Expected jar not found: $APP_DIR/springboot/target/springboot-0.0.1-SNAPSHOT.jar"
  ls -lah "$APP_DIR/springboot/target"
  exit 1
fi

echo "[5/8] Building frontend"
cd "$APP_DIR/vue"
npm install
npm run build

echo "[6/8] Installing systemd service"
envsubst '${APP_DIR}' < "$APP_DIR/deploy/systemd/travel-backend.service" > /etc/systemd/system/travel-backend.service
systemctl daemon-reload
systemctl enable travel-backend
systemctl restart travel-backend

echo "[7/8] Installing nginx config"
envsubst '${APP_DIR} ${SERVER_NAME} ${SERVER_PORT}' < "$APP_DIR/deploy/nginx/travel.conf.template" > /etc/nginx/sites-available/travel
ln -sfn /etc/nginx/sites-available/travel /etc/nginx/sites-enabled/travel
rm -f /etc/nginx/sites-enabled/default
nginx -t
systemctl reload nginx

echo "[8/8] Status"
systemctl --no-pager --full status travel-backend || true
systemctl --no-pager --full status nginx || true

echo "Deployment complete. Open: http://${SERVER_NAME}"
