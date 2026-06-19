<template>
  <div class="page-container">
    <el-carousel height="550px" motion-blur class="banner-carousel">
      <el-carousel-item v-for="item in data.imgs" :key="item">
        <img :src="item" alt="" class="carousel-image">
      </el-carousel-item>
    </el-carousel>

    <div class="main-content">
      <!-- 左边游记 -->
      <div class="travel-section">
        <div class="section-header">
          <div class="tab-group">
            <div class="tab-item" :class="{ 'active': data.sort === 'hot' }" @click="loadBySort('hot')">热门游记</div>
            <div class="tab-item" :class="{ 'active': data.sort === 'new' }" @click="loadBySort('new')">最新发表</div>
          </div>
          <el-button @click="goPage('/front/addTravel')" type="primary" class="write-button">
            <el-icon><Edit /></el-icon> 写游记
          </el-button>
        </div>

        <div class="travel-list">
          <div v-for="item in data.travelsList" :key="item.id" class="travel-card">
            <div class="travel-content">
              <img :src="item.cover" alt="" class="travel-cover">
              <div class="travel-info">
                <a :href="'/front/travelDetail?id=' + item.id" class="travel-title">{{ item.title }}</a>
                <div class="travel-desc">{{ item.descr }}</div>
                <div class="travel-meta">
                  <span><el-icon><Location/></el-icon>{{ item.location }}</span>
                  <span>作者：{{ item.userName }}</span>
                  <span><el-icon><View/></el-icon>{{ item.readCount }}</span>
                  <span><el-icon><Clock/></el-icon>{{ item.time }}</span>
                  <div class="praise-group">
                    <span class="praise-count">{{ item.praiseCount }}</span>
                    <div class="top" @click="top(item.id)">赞</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="pagination" v-if="data.total">
            <el-pagination
                @current-change="loadTravels"
                size="small"
                background
                layout="prev, pager, next"
                :page-size="data.pageSize"
                v-model:current-page="data.pageNum"
                :total="data.total"
            />
          </div>
        </div>
      </div>

      <!-- 右边攻略 -->
      <div class="sidebar">
        <!-- 攻略 -->
        <div class="strategy-section">
          <div class="section-header">
            <div class="section-title">旅游攻略推荐</div>
            <a href="/front/article" class="more-link">更多>></a>
          </div>
          <div v-for="item in data.articleList" :key="item.id" class="strategy-item">
            <img :src="item.cover" alt="" class="strategy-cover">
            <a :href="'/front/articleDetail?id=' + item.id" class="strategy-title">{{ item.title }}</a>
          </div>
        </div>

        <!-- 公告 -->
        <div class="notice-section">
          <div class="section-header">
            <div class="section-title">旅游网最新公告</div>
          </div>
          <div v-for="item in data.noticeList" :key="item.id" class="notice-item">
            {{ item.content }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import img1 from "@/assets/imgs/1.jpg";
import img2 from "@/assets/imgs/2.jpg";
import img3 from "@/assets/imgs/3.jpg";
import img4 from "@/assets/imgs/4.jpg";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  imgs: [img1, img2, img3, img4],
  sort: 'hot',
  pageNum: 1,
  pageSize: 5,
  travelsList: [],
  articleList: [],
  noticeList: [],
  total: 0
});

const goPage = (url) => {
  location.href = url;
};

const loadBySort = (sort) => {
  data.sort = sort;
  loadTravels();
};

const loadTravels = () => {
  request.get('/travels/selectFrontPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      sort: data.sort
    }
  }).then(res => {
    if (res.code === '200') {
      data.travelsList = res.data?.list || [];
      data.total = res.data?.total;
    }
  });
};

const top = (id) => {
  request.post('/praise/add', { fid: id, userId: data.user.id }).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      loadTravels();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

request.get('/notice/selectAll').then(res => {
  data.noticeList = res.data.splice(0, 3);
});

request.get('/article/selectRecommend').then(res => {
  data.articleList = res.data;
});

loadTravels();
</script>

<style scoped>
.page-container {
  background: #f5f7fa;
  min-height: 100vh;
}

.banner-carousel {
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.carousel-image {
  width: 100%;
  height: 550px;
  object-fit: cover;
}

.main-content {
  width: 80%;
  margin: 40px auto;
  display: flex;
  gap: 40px;
}

/* 左边游记 */
.travel-section {
  flex: 1;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 15px;
  border-bottom: 1px solid #e8ecef;
  margin-bottom: 20px;
}

.tab-group {
  display: flex;
  gap: 30px;
}

.tab-item {
  padding: 10px 0;
  font-size: 18px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-item.active {
  color: #319813;
  border-bottom: 2px solid #319813;
}

.tab-item:hover {
  color: #319813;
}

.write-button {
  background-color: #59df3b;
  border-color: #59df3b;
  padding: 10px 30px;
}

.travel-card {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.travel-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.travel-content {
  display: flex;
  gap: 20px;
}

.travel-cover {
  width: 180px;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.travel-card:hover .travel-cover {
  transform: scale(1.05);
}

.travel-info {
  flex: 1;
}

.travel-title {
  font-size: 18px;
  color: #333;
  text-decoration: none;
  display: block;
  margin-bottom: 15px;
  transition: color 0.3s ease;
}

.travel-title:hover {
  color: #319813;
}

.travel-desc {
  color: #666;
  line-height: 1.6;
  height: 70px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.travel-meta {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 20px;
  color: #999;
  font-size: 14px;
}

.travel-meta .el-icon {
  margin-right: 5px;
  vertical-align: middle;
}

.praise-group {
  display: flex;
  align-items: center;
  gap: 8px;
}

.praise-count {
  color: #ff4d4f;
  font-weight: bold;
  font-size: 16px; /* 稍微增大数字 */
}


.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

:deep(.el-pagination.is-background .el-pager li.is-active) {
  background-color: #319813;
}

/* 右边侧栏 */
.sidebar {
  width: 300px;
}

.strategy-section,
.notice-section {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 18px;
  color: #333;
  font-weight: 500;
}

.more-link {
  color: #666;
  text-decoration: none;
  font-size: 14px;
}

.more-link:hover {
  color: #319813;
}

.strategy-item {
  margin: 20px 0;
}

.strategy-cover {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  transition: transform 0.3s ease;
}

.strategy-item:hover .strategy-cover {
  transform: scale(1.03);
}

.strategy-title {
  font-size: 16px;
  color: #333;
  margin-top: 10px;
  text-decoration: none;
  display: block;
  transition: color 0.3s ease;
}

.strategy-title:hover {
  color: #319813;
}

.notice-item {
  margin: 15px 0;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}
</style>