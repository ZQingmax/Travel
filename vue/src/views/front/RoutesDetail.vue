<template>
  <div class="page-container">
    <div class="content-wrapper">
      <!-- 标题 -->
      <div class="title">{{ data.routes.name }}</div>

      <!-- 基本信息 -->
      <div class="info-bar">
        <div class="info-item">
          <i class="el-icon-location"></i>
          <span>旅游地点：<strong>{{ data.routes.location }}</strong></span>
        </div>
        <div class="info-item">
          <i class="el-icon-time"></i>
          <span>旅游天数：<strong>{{ data.routes.days }}天</strong></span>
        </div>
      </div>

      <!-- 小提示 -->
      <div class="tips-card">
        <div class="tips-header">小提示</div>
        <div class="tips-content">{{ data.routes.tips }}</div>
      </div>

      <!-- 图片 -->
      <div class="image-container">
        <img :src="toFileUrl(data.routes.img)" alt="" class="route-image">
      </div>

      <!-- 内容详情 -->
      <div class="content-section">
        <div v-html="data.routes.content" class="route-content"></div>
      </div>

      <!-- 地图 -->
      <div class="map-container">
        <div id="container" class="map"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import { toFileUrl } from "@/utils/file.js";
import router from "@/router/index.js";

const data = reactive({
  id: router.currentRoute.value.query.id,
  routes: {}
})

onMounted(() => {
  request.get('/routes/selectById/' + data.id).then(res => {
    data.routes = res.data

    let map = new AMap.Map('container', {
      center: [data.routes.longitude, data.routes.latitude],
      zoom: 13
    });

    map.plugin(['AMap.MouseTool'], function() {});

    let marker = new AMap.Marker({
      position: [data.routes.longitude, data.routes.latitude],
      title: '景点坐标'
    });
    map.add(marker);

    let infoWindow = new AMap.InfoWindow({
      content: `<div><b>位置</b>：${data.routes.location}</div>`,
      offset: new AMap.Pixel(0, -30),
      size: new AMap.Size(300, 0)
    });

    marker.on('click', function() {
      infoWindow.open(map, marker.getPosition());
    });
  })
})
</script>

<style scoped>
.page-container {
  background: #f8f9fa;
  min-height: 100vh;
  padding: 40px 0;
}

.content-wrapper {
  width: 60%;
  max-width: 900px;
  margin: 0 auto;
  background: white;
  border-radius: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  padding: 30px;
  animation: fadeIn 0.8s ease-in;
}

.title {
  font-size: 32px;
  font-weight: 700;
  color: #2c3e50;
  text-align: center;
  margin: 0 0 20px;
  padding-bottom: 15px;
  position: relative;
}

.title:after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 60px;
  height: 3px;
  background: #189500;
  transform: translateX(-50%);
}

.info-bar {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin: 20px 0;
  padding: 15px;
  background: #f5f6f5;
  border-radius: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 16px;
  color: #555;
}

.info-item i {
  margin-right: 8px;
  color: #189500;
  font-size: 20px;
}

.info-item strong {
  color: #2c3e50;
}

.tips-card {
  margin: 20px 0;
  padding: 15px;
  background: linear-gradient(135deg, #e8f5e9, #f1f8e9);
  border-radius: 10px;
  border-left: 4px solid #189500;
}

.tips-header {
  font-size: 16px;
  font-weight: 600;
  color: #189500;
  margin-bottom: 8px;
}

.tips-content {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.image-container {
  margin: 30px 0;
  text-align: center;
}

.route-image {
  width: 70%;
  max-height: 900px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.route-image:hover {
  transform: scale(1.02);
}

.content-section {
  margin: 30px 0;
  padding: 20px;
  background: #fff;
  border-radius: 10px;
  border: 1px solid #eee;
}

.route-content {
  font-size: 16px;
  color: #444;
  line-height: 1.8;
  text-align: justify;
}

.route-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 5px;
}

.map-container {
  margin: 30px 0;
}

.map {
  width: 100%;
  height: 500px;
  border-radius: 10px;
  border: 1px solid #ddd;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
