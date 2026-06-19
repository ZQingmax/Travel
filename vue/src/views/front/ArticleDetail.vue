<template>
  <div class="article-container">
    <div class="article-header">
      <h1 class="article-title">{{ data.article.title }}</h1>
      <el-space class="article-meta" :size="20" alignment="center">
        <div class="meta-item">
          <el-icon :size="16"><Calendar /></el-icon>
          <span>{{ data.article.date }}</span>
        </div>
        <div class="meta-item">
          <el-icon :size="16"><View /></el-icon>
          <span>{{ data.article.readCount }}次阅读</span>
        </div>
        <div class="meta-item">
          <el-tag type="warning">{{ data.article.collectCount }}</el-tag>
          <el-button
              type="warning"
              :icon="StarFilled"
              @click="collect"
              :loading="collectLoading"
          >收藏
          </el-button>
        </div>
      </el-space>
    </div>

    <div class="article-content">
      <el-card shadow="hover">
        <div
            v-html="data.article.content"
            class="content-html"
            v-loading="contentLoading"
        ></div>
      </el-card>
    </div>
    <Comment :module=" 'article' "/>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { StarFilled } from '@element-plus/icons-vue';
import request from '@/utils/request';
import router from "@/router/index.js";
import Comment from "@/components/Comment.vue";
import {ElMessage} from "element-plus";

const data = reactive({
  id: router.currentRoute.value.query.id,
  article: {}
});
const collect = () => {
  request.post('/collect/add', {fid : data.id}).then(res => {
    if (res.code === '200') {
      ElMessage.success('收藏成功');
      load()
    }else{
      ElMessage.error(res.msg);
    }
  });
}
request.put('/article/updateReadCount/' + data.id).then(res => {
  load();
});
// 获取文章数据
const load = () => {
  request.get(`/article/selectById/` + data.id).then(res => {
    data.article = res.data;
  });
};
load()
</script>

<style scoped>
.article-container {
  width: 80%; /* 扩大容器宽度，原值为60% */
  max-width: 1200px; /* 设置最大宽度，避免过宽 */
  margin: 0 auto;
  padding: 40px 20px; /* 增加顶部间距 */
  background: #f5f7fa; /* 添加背景色，与之前页面一致 */
}

.article-header {
  text-align: center; /* 标题和元信息居中 */
  margin-bottom: 40px;
}

.article-title {
  font-size: 32px; /* 增大标题字体 */
  color: #333;
  margin: 0 0 20px; /* 调整标题间距 */
  font-weight: 700;
  line-height: 1.2;
}

.article-meta {
  display: flex;
  justify-content: center; /* 元信息居中 */
  flex-wrap: wrap; /* 防止溢出 */
  padding: 15px 0;
  background: #fff; /* 添加背景 */
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #666;
  font-size: 14px;
}

.meta-item .el-tag {
  margin-right: 10px;
}

.article-content {
  margin-top: 20px; /* 减少与标题的间距 */
}

:deep(.el-card) {
  border-radius: 10px;
  border: none; /* 移除边框 */
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08); /* 增强阴影 */
  padding: 20px; /* 增加内边距 */
  background: #fff;
}

.content-html {
  line-height: 1.8;
  font-size: 16px;
  color: #444; /* 稍微加深文字颜色 */
  min-height: 300px; /* 设置最小高度，避免内容太少时显得空荡 */
}

/* 内容样式增强 */
:deep(h2) {
  color: #319813; /* 使用品牌绿色 */
  margin: 30px 0 20px;
  font-size: 24px;
  font-weight: 600;
}

:deep(h3) {
  color: #333;
  margin: 25px 0 15px;
  font-size: 20px;
}

:deep(p) {
  margin: 15px 0;
  color: #666;
  text-align: justify; /* 两端对齐 */
}

:deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px auto;
  display: block; /* 图片居中 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

:deep(ul), :deep(ol) {
  margin: 15px 0;
  padding-left: 25px;
  color: #666;
}

:deep(a) {
  color: #319813;
  text-decoration: none;
  transition: color 0.3s ease;
}

:deep(a:hover) {
  color: #46b61f;
  text-decoration: underline;
}
</style>