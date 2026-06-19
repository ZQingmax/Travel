<template>
  <div class="travel-container">
    <h1 class="travel-title">{{ data.travel.title }}</h1>
    <div class="travel-meta">
      <div class="meta-info">
        <span><el-icon size="14"><User/></el-icon>{{ data.travel.userName }}</span>
        <span><el-icon size="14"><Clock/></el-icon>{{ data.travel.time }}</span>
        <span><el-icon size="14"><View/></el-icon>{{ data.travel.readCount }}</span>
        <div class="praise-group">
          <span class="praise-count">{{ data.travel.praiseCount }}</span>
          <div class="top" @click="top()">赞</div>
        </div>
      </div>
    </div>

    <div class="travel-details">
      <div class="detail-item">
        <el-icon size="20" color="#ff8c00"><Location/></el-icon>
        <span class="label">出行地点</span> <span class="value">{{ data.travel.location }}</span>
      </div>
      <div class="detail-item">
        <el-icon size="20" color="#ff8c00"><Watch/></el-icon>
        <span class="label">出发日期</span> <span class="value">{{ data.travel.startDate }}</span>
      </div>
      <div class="detail-item">
        <el-icon size="20" color="#ff8c00"><Calendar/></el-icon>
        <span class="label">出行天数</span> <span class="value">{{ data.travel.days }}天</span>
      </div>
      <div class="detail-item">
        <el-icon size="20" color="#ff8c00"><Money/></el-icon>
        <span class="label">花费金额</span> <span class="value">{{ data.travel.money }}元</span>
      </div>
    </div>

    <div class="travel-content">
      <div v-html="data.travel.content" class="content-html"></div>
    </div>

    <Comment :module="'travels'" />
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import Comment from "@/components/Comment.vue";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id: router.currentRoute.value.query.id,
  travel: {}
});

request.put('/travels/updateReadCount/' + data.id).then(res => {
  load();
});

const load = () => {
  request.get('/travels/selectById/' + data.id).then(res => {
    data.travel = res.data;
  });
};

const top = () => {
  request.post('/praise/add', { fid: data.id, userId: data.user.id }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};
</script>

<style scoped>
@import '@/assets/css/global.css';
.travel-container {
  width: 80%; /* 扩大宽度，原值为60% */
  max-width: 1200px; /* 设置最大宽度 */
  margin: 40px auto; /* 增加顶部间距 */
  padding: 20px;
  background: #f5f7fa; /* 背景色与之前一致 */
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.travel-title {
  font-size: 32px;
  font-weight: 700;
  color: #333;
  text-align: center;
  margin: 0 0 30px;
  line-height: 1.2;
}

.travel-meta {
  display: flex;
  align-items: center;
  justify-content: center; /* 居中显示 */
  gap: 20px;
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
  margin-bottom: 30px;
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #eee;
}

.meta-info {
  display: flex;
  align-items: center;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

.meta-info span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.praise-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.praise-count {
  color: #ff8c00;
  font-size: 18px;
  font-weight: bold;
}



.travel-details {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  border: 1px dashed #ddd;
  margin-bottom: 30px;
  flex-wrap: wrap; /* 响应式支持 */
}

.detail-item {
  flex: 1;
  min-width: 200px; /* 防止过于压缩 */
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 15px;
}

.detail-item .label {
  color: #333;
  font-weight: 500;
}

.detail-item .value {
  color: #ff8c00;
}

.travel-content {
  background: #fff;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.content-html {
  line-height: 1.8;
  font-size: 16px;
  color: #444;
}

:deep(h2) {
  color: #319813; /* 使用品牌绿色 */
  margin: 30px 0 20px;
  font-size: 24px;
  font-weight: 600;
}

:deep(p) {
  margin: 15px 0;
  color: #666;
  text-align: justify;
}

:deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px auto;
  display: block;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
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