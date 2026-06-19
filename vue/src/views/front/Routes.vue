<template>
  <div class="page-container">
    <!-- 头部背景 -->
    <div class="header-bg">
      <div class="overlay">
        <h1 class="header-title">探索精彩旅游路线</h1>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="content-wrapper">
      <div class="section-title">
        <span>推荐旅游路线</span>
      </div>

      <!-- 路线卡片列表 -->
      <el-row :gutter="20" class="routes-grid">
        <el-col
            :span="8"
            v-for="item in data.routesList"
            :key="item.id"
            class="route-col"
        >
          <a :href="'/front/routesDetail?id=' + item.id" class="route-link">
            <div class="route-card">
              <div class="image-wrapper">
                <img :src="toFileUrl(item.img)" alt="" class="route-image">
                <div class="image-overlay">
                  <span class="view-detail">查看详情</span>
                </div>
              </div>
              <div class="route-title">{{ item.name }}</div>
            </div>
          </a>
        </el-col>
      </el-row>

      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="data.total">
        <el-pagination
            size="small"
            @current-change="load"
            background
            layout="total, prev, pager, next"
            :page-size="data.pageSize"
            v-model:current-page="data.pageNum"
            :total="data.total"
            class="custom-pagination"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { toFileUrl } from "@/utils/file.js";

const data = reactive({
  routesList: [],
  pageNum: 1,
  pageSize: 6,
  total: 0,
})

const load = () => {
  request.get('/routes/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.routesList = res.data?.list || []
      data.total = res.data?.total
    }
  })
}
load()
</script>

<style scoped>
/* 全局容器 */
.page-container {
  background: #f5f6f5;
  min-height: 100vh;
}

/* 头部背景 */
.header-bg {
  height: 450px;
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url("@/assets/imgs/lxbg.jpg") no-repeat center;
  background-size: cover;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay {
  text-align: center;
}

.header-title {
  color: white;
  font-size: 48px;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  animation: fadeIn 1.5s ease-in;
}

/* 内容区域 */
.content-wrapper {
  width: 80%;
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background: white;
  border-radius: 15px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

/* 标题 */
.section-title {
  padding-left: 15px;
  margin-bottom: 30px;
  position: relative;
}

.section-title span {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  position: relative;
  z-index: 1;
}

.section-title:before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  width: 5px;
  height: 30px;
  background: #189500;
  transform: translateY(-50%);
}

/* 路线卡片 */
.routes-grid {
  padding: 0 10px;
}

.route-col {
  margin-bottom: 30px;
}

.route-link {
  text-decoration: none;
  display: block;
}

.route-card {
  background: white;
  border-radius: 10px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.route-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.image-wrapper {
  position: relative;
  overflow: hidden;
}

.route-image {
  width: 100%;
  height: 500px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.route-card:hover .route-image {
  transform: scale(1.05);
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  opacity: 0;
  transition: opacity 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.route-card:hover .image-overlay {
  opacity: 1;
}

.view-detail {
  color: white;
  font-size: 16px;
  font-weight: 500;
  padding: 8px 20px;
  background: #189500;
  border-radius: 20px;
}

.route-title {
  padding: 15px;
  font-size: 18px;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
  height: 70px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 分页 */
.pagination-wrapper {
  text-align: center;
  padding: 20px 0;
}

.custom-pagination :deep(.el-pager li) {
  border-radius: 50%;
  margin: 0 5px;
}

.custom-pagination :deep(.el-pager li.active) {
  background: #189500;
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