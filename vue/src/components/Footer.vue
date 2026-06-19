<template>
  <div class="footer-container">
    <div class="footer-wrapper">
      <!-- 网站介绍 -->
      <div class="footer-column">
        <h3 class="footer-title">WanderLens 旅游网</h3>
        <p class="footer-item">旅游爱好者用得更多的旅游网站</p>
        <p class="footer-item">上亿旅行者共同打造的"旅行神器"</p>
        <p class="footer-highlight"><span class="accent">30,000</span> 多个全球旅游目的地</p>
        <p class="footer-highlight"><span class="accent">300,000</span> 个细分目的地新玩法</p>
        <p class="footer-highlight"><span class="accent">660,000,000</span> 次攻略下载</p>
        <p class="footer-highlight"><span class="accent">38,000</span> 家旅游产品供应商</p>
      </div>

      <!-- 关于我们 -->
      <div class="footer-column">
        <h3 class="footer-title">关于我们</h3>
        <div class="footer-link-group">
          <a href="#" class="footer-link">关于我们</a>
          <a href="#" class="footer-link">联系我们</a>
          <a href="#" class="footer-link">隐私政策</a>
          <a href="#" class="footer-link">商标声明</a>
          <a href="#" class="footer-link">服务协议</a>
          <a href="#" class="footer-link">商城平台协议</a>
          <a href="#" class="footer-link highlight">旅游网服务监督员</a>
        </div>
      </div>

      <!-- 旅行服务 -->
      <div class="footer-column">
        <h3 class="footer-title">旅行服务</h3>
        <div class="footer-link-group">
          <div class="service-row">
            <a href="#" class="footer-link">旅游攻略</a>
            <a href="#" class="footer-link">酒店预订</a>
          </div>
          <div class="service-row">
            <a href="#" class="footer-link">旅游特价</a>
            <a href="#" class="footer-link">旅游问答</a>
          </div>
          <div class="service-row">
            <a href="#" class="footer-link">旅游指南</a>
            <a href="#" class="footer-link">旅游资讯</a>
          </div>
          <a href="#" class="footer-link highlight">旅行商城全球入驻</a>
        </div>
      </div>

      <!-- 联系方式 -->
      <div class="footer-contact">
        <img src="@/assets/imgs/logo.png" alt="logo" class="footer-logo">
        <p class="slogan">旅游前，先上<span class="accent">WanderLens</span>！</p>
        <div class="contact-info">
          <el-icon class="phone-icon"><Iphone /></el-icon>
          <span class="phone-number">010-65589988</span>
        </div>
      </div>
    </div>
    <!-- 版权信息 -->
    <div class="footer-bottom">
      <p>© 2025 WanderLens. All Rights Reserved.</p>
    </div>
  </div>
</template>

<style scoped>
.footer-container {
  background: #2a2a2a;
  color: #fff;
  padding-top: 50px;
  border-top: 4px solid #46b61f;
  position: relative;
}

.footer-wrapper {
  width: 85%;
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  gap: 40px;
  padding-bottom: 40px;
}

.footer-column {
  flex: 1;
  min-width: 200px;
}

.footer-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid #46b61f;
  position: relative;
  transition: color 0.3s ease;
}

.footer-title:hover {
  color: #46b61f;
}

.footer-item {
  color: #b0b0b0;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 12px;
  transition: color 0.3s ease;
}

.footer-highlight {
  color: #d0d0d0;
  font-size: 14px;
  margin-bottom: 12px;
}

.accent {
  color: #46b61f;
  font-weight: 700;
  font-size: 1.1em;
}

.footer-link-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-link {
  color: #b0b0b0;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
}

.footer-link:hover {
  color: #46b61f;
  padding-left: 5px;
}

.highlight {
  color: #46b61f;
  font-weight: 500;
}

.service-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}

.footer-contact {
  flex: 1;
  min-width: 200px;
  text-align: center;
}

.footer-logo {
  width: 120px;
  margin-bottom: 20px;
  transition: transform 0.3s ease;
}

.footer-logo:hover {
  transform: scale(1.05);
}

.slogan {
  color: #d0d0d0;
  font-size: 16px;
  margin-bottom: 20px;
  font-weight: 500;
}

.contact-info {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  background: rgba(24, 149, 0, 0.1);
  padding: 10px 20px;
  border-radius: 25px;
  transition: background 0.3s ease;
}

.contact-info:hover {
  background: rgba(24, 149, 0, 0.2);
}

.phone-icon {
  color: #46b61f;
  font-size: 20px;
}

.phone-number {
  color: #fff;
  font-size: 16px;
  font-weight: 500;
}

.footer-bottom {
  width: 100%;
  text-align: center;
  padding: 20px 0;
  background: #222;
  color: #888;
  font-size: 13px;
  border-top: 1px solid #333;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .footer-wrapper {
    width: 90%;
    gap: 30px;
  }

  .footer-column,
  .footer-contact {
    flex: 1 1 45%;
  }
}

@media (max-width: 576px) {
  .footer-wrapper {
    width: 95%;
    flex-direction: column;
    gap: 25px;
  }

  .service-row {
    grid-template-columns: 1fr;
  }

  .footer-column,
  .footer-contact {
    flex: 1 1 100%;
  }
}
</style>

<script setup>
import { Iphone } from '@element-plus/icons-vue'
</script>