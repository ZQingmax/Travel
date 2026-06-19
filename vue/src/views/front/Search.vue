<template>
  <div class="page-container">
    <!-- 头部背景 -->
    <div class="header-bg">
      <div class="overlay">
        <h1 class="header-title">发现热门旅游产品</h1>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="content-wrapper">
      <!-- 标题和排序 -->
      <div style="margin-bottom: 20px">
        <el-input size="large" clearable style="width: 500px; margin-right: 5px" v-model="data.name" placeholder="请输入目的地名称搜索"></el-input>
        <el-button size="large"  type="primary" @click="load">搜索</el-button>
      </div>
      <!-- 产品列表 -->
      <el-row :gutter="20" class="product-grid">
        <el-col
            :span="6"
            v-for="item in data.tourismList"
            :key="item.id"
            class="product-col"
        >
          <div class="product-card" @click="goPage('/front/tourismDetail?id=' + item.id)">
            <div class="image-wrapper">
              <img :src="toFileUrl(item.img)" alt="" class="product-image">
            </div>
            <div class="product-info">
              <div class="product-title">{{ item.name }}</div>
              <div class="price-sales">
                <span class="price">￥{{ item.price }}</span>
                <span class="sales">已售 {{ item.orderNum }}</span>
              </div>
            </div>
          </div>
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
import router from "@/router/index.js";

const data = reactive({
  tourismList: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  name: router.currentRoute.value.query.name,
})

const goPage = (path) => {
  location.href = path
}

const load = () => {
  request.get('/tourism/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    if (res.code === '200') {
      data.tourismList = res.data?.list || []
      data.total = res.data?.total
    }
  })
}
load()
</script>

<style scoped>
.page-container {
  background: #f5f6f5;
  min-height: 100vh;
}

/* 头部背景 */
.header-bg {
  height: 400px;
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url("@/assets/imgs/jdlw.jpg") no-repeat center;
  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay {
  text-align: center;
}

.header-title {
  color: white;
  font-size: 40px;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  animation: fadeIn 1s ease-in;
}

/* 内容区域 */
.content-wrapper {
  width: 80%;
  max-width: 1200px;
  margin: 40px auto;
  padding: 20px;
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.05);
}

/* 标题和排序 */
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 0 10px;
}

.section-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  padding-left: 15px;
  border-left: 4px solid #189500;
}

.sort-options {
  font-size: 14px;
  font-weight: 500;
  color: #666;
}

.sort-options span {
  cursor: pointer;
  padding: 5px 10px;
  transition: all 0.3s ease;
}

.sort-options .active {
  color: #189500;
  font-weight: 600;
}

.sort-options span:hover {
  color: #189500;
}

.divider {
  color: #ddd;
  margin: 0 5px;
}

/* 产品卡片 */
.product-grid {
  padding: 0 10px;
}

.product-col {
  margin-bottom: 25px;
}

.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
}

.image-wrapper {
  position: relative;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 150px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.product-info {
  padding: 10px;
}

.product-title {
  font-size: 16px;
  color: #333;
  line-height: 1.4;
  height: 44px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.3s ease;
}

.product-card:hover .product-title {
  color: #189500;
}

.price-sales {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.price {
  color: red;
  font-size: 18px;
  font-weight: 600;
}

.sales {
  color: #888;
  font-size: 12px;
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

/* 响应式 */
@media (max-width: 768px) {
  .content-wrapper {
    width: 90%;
  }

  .product-col {
    flex: 0 0 50%;
    max-width: 50%;
  }
}
</style>