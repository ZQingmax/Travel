#!/usr/bin/env bash
set -euo pipefail

if [ "$(id -u)" -ne 0 ]; then
  echo "Please run as root: sudo bash deploy/scripts/init-server.sh"
  exit 1
fi

echo "[1/6] Updating apt packages"
apt update
apt upgrade -y

echo "[2/6] Ensuring 2G swap"
if ! swapon --show | grep -q '/swapfile'; then
  if [ ! -f /swapfile ]; then
    fallocate -l 2G /swapfile
    chmod 600 /swapfile
    mkswap /swapfile
  fi
  swapon /swapfile
fi
if ! grep -q '^/swapfile none swap sw 0 0$' /etc/fstab; then
  echo '/swapfile none swap sw 0 0' >> /etc/fstab
fi

echo "[3/6] Installing base packages"
apt install -y git curl wget unzip nginx mysql-server redis-server maven openjdk-21-jdk gettext-base

echo "[4/6] Installing Node.js 20"
if ! command -v node >/dev/null 2>&1 || ! node -v | grep -q '^v20\.'; then
  curl -fsSL https://deb.nodesource.com/setup_20.x | bash -
  apt install -y nodejs
fi

echo "[5/6] Enabling Redis"
systemctl enable redis-server
systemctl start redis-server

echo "[6/6] Versions"
java -version
node -v
npm -v
mvn -v | head -n 1
mysql --version
redis-server --version
free -h

echo "Server initialization complete."
