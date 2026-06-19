<template>
  <div class="page-container">
    <!-- 头部背景 -->
    <div class="header-bg">
      <div class="overlay">
        <h1 class="header-title">旅游攻略精选</h1>
        <p class="header-subtitle">探索全球旅行灵感，发现你的下一站</p>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="content-wrapper">
      <div class="section-header">
        <div class="section-title">推荐攻略</div>
      </div>

      <!-- 攻略卡片 -->
      <div class="card-container">
        <div v-for="(item, index) in data.tableData" :key="item.id" class="card-item">
          <a :href="'/front/articleDetail?id=' + item.id" target="_blank" class="article-link">
            <div class="article-content" :class="{ 'reverse': index % 2 !== 0 }">
              <div class="image-wrapper">
                <img :src="toFileUrl(item.cover)" alt="" class="cover-image">
              </div>
              <div class="text-wrapper">
                <div class="article-title">{{ item.title }}</div>
                <div class="description">{{ item.descr }}</div>
                <div class="meta-info">
                  <span class="date"><i class="el-icon-time"></i>发布日期： {{ item.date }}</span>
                  <span class="views"><i class="el-icon-view"></i>阅读量： {{ item.readCount }}</span>
                </div>
              </div>
            </div>
          </a>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper" v-if="data.total">
        <el-pagination
            @current-change="load"
            background
            layout="prev, pager, next, total"
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
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
});

const load = () => {
  request.get('/article/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    }
  });
};
load();
</script>

<style scoped>
.page-container {
  background: #f5f7fa;
  min-height: 100vh;
}

/* 头部背景 */
.header-bg {
  height: 450px;
  background: linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)), url('@/assets/imgs/lygl.png') no-repeat center;
  background-size: cover;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlay {
  text-align: center;
  color: white;
}

.header-title {
  font-size: 42px;
  font-weight: 700;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
  animation: fadeIn 1s ease-in;
}

.header-subtitle {
  font-size: 18px;
  margin-top: 10px;
  font-weight: 300;
}

/* 内容区域 */
.content-wrapper {
  width: 80%;
  max-width: 1000px;
  margin: 40px auto;
  padding: 20px;
}

/* 标题 */
.section-header {
  margin-bottom: 30px;
}

.section-title {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  padding-left: 15px;
  border-left: 4px solid #189500;
  position: relative;
}

/* 卡片容器 */
.card-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.card-item {
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.3s ease;
}

.card-item:hover {
  background: #f9f9f9;
}

.article-link {
  text-decoration: none;
  display: block;
}

.article-content {
  display: flex;
  gap: 20px;
  align-items: center;
}

.article-content.reverse {
  flex-direction: row-reverse;
}

.image-wrapper {
  flex-shrink: 0;
  overflow: hidden;
  border-radius: 8px;
}

.cover-image {
  width: 220px;
  height: 140px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.card-item:hover .cover-image {
  transform: scale(1.05);
}

.text-wrapper {
  flex: 1;
}

.article-title {
  font-size: 20px;
  font-weight: 500;
  color: #333;
  margin-bottom: 10px;
  line-height: 1.4;
  transition: color 0.3s ease;
}

.card-item:hover .article-title {
  color: #189500;
}

.description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  height: 64px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.meta-info {
  margin-top: 10px;
  font-size: 13px;
  color: #999;
  display: flex;
  gap: 20px;
}

.meta-info i {
  margin-right: 5px;
  color: #189500;
}

/* 分页 */
.pagination-wrapper {
  margin-top: 30px;
  text-align: right;
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
    padding: 15px;
  }

  .article-content {
    flex-direction: column;
    text-align: center;
  }

  .article-content.reverse {
    flex-direction: column;
  }

  .cover-image {
    width: 100%;
    height: auto;
    max-height: 200px;
  }

  .description {
    height: auto;
    -webkit-line-clamp: 4;
  }

  .meta-info {
    justify-content: center;
  }
}
</style>
