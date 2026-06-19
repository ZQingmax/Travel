<template>
  <div class="page-container">
    <!-- 顶部通知栏 -->
    <div class="front-notice">
      <div class="notice-text">
        <i>{{ data.top || "上WanderLens，带你看不一样的世界!" }}</i>
      </div>
      <iframe
          scrolling="no"
          src="https://widget.tianqiapi.com/?style=tg&skin=pitaya"
          frameborder="0"
          width="470"
          height="50"
          allowtransparency="true"
          class="weather-widget"
      ></iframe>
    </div>

    <!-- 头部导航 -->
    <div class="front-header">
      <a href="/front/home" class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="WanderLens Logo" class="logo">
        <div class="title"><strong>WanderLens</strong></div>
      </a>

      <div class="front-header-center">
        <el-menu
            :default-active="router.currentRoute.value.path"
            router
            mode="horizontal"
            class="nav-menu"
        >
          <el-menu-item index="/front/home">
            <el-icon><House /></el-icon> 首页
          </el-menu-item>
          <el-menu-item index="/front/article">
            <el-icon><Notebook /></el-icon> 旅游攻略
          </el-menu-item>
          <el-menu-item index="/front/routes">
            <el-icon><MapLocation /></el-icon> 旅游路线
          </el-menu-item>
          <el-menu-item index="/front/tourism">
            <el-icon><Ticket /></el-icon> 景点玩乐
          </el-menu-item>
          <el-menu-item index="/front/feedback">
            <el-icon><ChatDotRound /></el-icon> 内容反馈
          </el-menu-item>
        </el-menu>
      </div>

      <div class="search-bar">
        <el-input
            placeholder="请输入目的地内容"
            v-model="data.search"
            class="search-input"
            clearable
        />
        <el-button
            type="primary"
            @click="goPage('/front/search?name=' + data.search)"
            class="search-button"
        >搜索</el-button>
      </div>

      <div class="front-header-right">
        <div v-if="!data.user.id" class="auth-buttons">
          <el-button @click="router.push('/login')" plain>登录</el-button>
          <el-button @click="router.push('/register')" type="primary">注册</el-button>
        </div>
        <div v-else class="user-menu">
          <el-dropdown trigger="click">
            <div class="user-info">
              <img :src="data.user.avatar" alt="Avatar" class="avatar">
              <span class="username">{{ data.user.name }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="goPage('/front/person')">个人信息</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/password')">修改密码</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/orders')">我的订单</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/travel')">我的游记</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/collect')">我的收藏</el-dropdown-item>
                <el-dropdown-item @click="goPage('/front/myFeedback')">我的反馈</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>

    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import Footer from "@/components/Footer.vue";
import { House, Notebook, MapLocation, Ticket, ChatDotRound, ArrowDown } from '@element-plus/icons-vue';

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  top: '',
  noticeData: [],
  search: ''
})

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const goPage = (path) => {
  location.href = path
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    data.noticeData = res.data
    let i = 0
    if (data.noticeData && data.noticeData.length) {
      data.top = data.noticeData[0].content
      setInterval(() => {
        data.top = data.noticeData[i].content
        i++
        if (i === data.noticeData.length) i = 0
      }, 2500)
    }
  })
}
loadNotice()
</script>

<style scoped>
.page-container {
  min-height: 100vh;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
}

/* 顶部通知栏 */
.front-notice {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.notice-text {
  flex: 1;
  padding-left: 20px;
}

.notice-text i {
  font-size: 16px;
  color: #59df3b;
  font-style: italic;
}

.weather-widget {
  margin-left: 20px;
}

/* 头部导航 */
.front-header {
  background: white;
  display: flex;
  align-items: center;
  padding: 15px 30px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.front-header-left {
  display: flex;
  align-items: center;
  text-decoration: none;
  transition: transform 0.3s ease;
}

.front-header-left:hover {
  transform: translateY(-2px);
}

.logo {
  width: 45px;
  height: 45px;
  margin-right: 10px;
  transition: transform 0.3s ease;
}

.front-header-left:hover .logo {
  transform: scale(1.1);
}

.title strong {
  font-size: 26px;
  font-weight: 700;
  background: linear-gradient(135deg, #59df3b 0%, #46b61f 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

/* 导航菜单 */
.front-header-center {
  flex: 1;
  margin: 0 20px;
}

.nav-menu {
  border-bottom: none;
}

.nav-menu :deep(.el-menu-item) {
  font-size: 16px;
  color: #666;
  padding: 0 20px;
  transition: all 0.3s ease;
}

.nav-menu :deep(.el-menu-item:hover) {
  color: #59df3b;
  background: #f5f6f5;
}

.nav-menu :deep(.el-menu-item.is-active) {
  color: #59df3b;
  border-bottom: 2px solid #59df3b;
}

/* 搜索栏 */
.search-bar {
  display: flex;
  align-items: center;
  width: 400px;
}

.search-input {
  margin-right: 10px;
}

.search-input :deep(.el-input__inner) {
  border-radius: 20px;
}

.search-button {
  background: #59df3b;
  border-color: #59df3b;
  border-radius: 20px;
  padding: 10px 20px;
}

.search-button:hover {
  background: #137d00;
  border-color: #137d00;
}

/* 用户区域 */
.front-header-right {
  margin-left: 20px;
}

.auth-buttons .el-button {
  margin-left: 10px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 10px;
  transition: background 0.3s ease;
}

.user-info:hover {
  background: #f5f6f5;
  border-radius: 20px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 8px;
}

.username {
  font-size: 14px;
  color: #333;
  margin-right: 5px;
}

/* 主体内容 */
.main-body {
  flex: 1;
  padding: 20px 0;
}
</style>