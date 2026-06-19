<template>
  <div class="page-container">
    <div class="content-wrapper">
      <!-- 产品头部 -->
      <div class="product-header">
        <div class="image-container">
          <img :src="data.tourism.img" alt="" class="product-image">
        </div>
        <div class="info-container">
          <h1 class="product-title">{{ data.tourism.name }}</h1>
          <div class="special-tags">
            <el-tag
                v-for="item in JSON.parse(data.tourism.specials || '[]')"
                :key="item"
                type="success"
                effect="plain"
                class="tag"
            >{{ item }}</el-tag>
          </div>
          <p class="description">{{ data.tourism.descr }}</p>
          <div class="price-box">
            <span class="price">￥{{ data.tourism.price }}</span>
            <span class="stock">库存：{{ data.tourism.store }}</span>
          </div>
          <p class="notice">预订须知：支付完成后商家最晚会在2个工作小时内（工作日9:00-18:00）确认是否预定成功</p>
          <el-button class="buy-button" @click="addOrder">立即购买</el-button>
        </div>
      </div>

      <!-- 图文介绍 -->
      <div class="content-section">
        <h2 class="section-title">图文介绍</h2>
        <div class="content-detail" v-html="data.tourism.content"></div>
      </div>

      <!-- 订单弹窗 -->
      <el-dialog
          title="确认订单"
          v-model="data.formVisible"
          width="500px"
          destroy-on-close
          class="order-dialog"
      >
        <el-form label-width="80px" :model="data.form" class="order-form">
          <el-form-item label="商品名称">{{ data.tourism.name }}</el-form-item>
          <el-form-item label="商品单价">￥{{ data.tourism.price }}</el-form-item>
          <el-form-item label="购买数量">
            <el-input-number
                :min="1"
                :max="10"
                v-model="data.form.num"
                @change="changeNum"
                size="small"
            />
          </el-form-item>
          <el-form-item label="商品总价">
            <span class="total-price">￥{{ data.form.total }}</span>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.formVisible = false" size="small">取消</el-button>
            <el-button type="primary" @click="saveOrder" size="small">确认下单</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data = reactive({
  id: router.currentRoute.value.query.id,
  tourism: {},
  formVisible: false,
  form: {}
})

const changeNum = () => {
  data.form.total = data.tourism.price * data.form.num
}

const saveOrder = () => {
  // 实现订单保存逻辑
  request.post("/orders/add", data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('下单成功')
          router.push('/front/orders')
        } else {
          ElMessage.error(res.msg)
        }
      }
  )
}
const addOrder = () => {
  data.formVisible = true
  data.form.tourismId = data.tourism.id
  data.form.tourismImg = data.tourism.img  // 修正为 img
  data.form.name = data.tourism.name
  data.form.tourismPrice = data.tourism.price
  data.form.num = 1
  data.form.total = data.tourism.price
}

const load = () => {
  request.get('/tourism/selectById/' + data.id).then(res => {
    if (res.code === '200') {
      data.tourism = res.data
    }
  })
}
load()
</script>

<style scoped>
.page-container {
  background: #f5f7fa;
  min-height: 100vh;
  padding: 20px 0;
}

.content-wrapper {
  width: 80%;
  max-width: 1000px;
  margin: 0 auto;
  background: white;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  padding: 30px;
}

/* 产品头部 */
.product-header {
  display: flex;
  gap: 30px;
  margin-bottom: 40px;
}

.image-container {
  flex: 1;
}

.product-image {
  width: 100%;
  height: 350px;
  object-fit: cover;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
}

.product-image:hover {
  transform: scale(1.02);
}

.info-container {
  flex: 1;
}

.product-title {
  font-size: 24px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 15px;
  line-height: 1.4;
}

.special-tags {
  margin-bottom: 15px;
}

.tag {
  margin-right: 10px;
  margin-bottom: 10px;
  border-color: #189500;
  color: #189500;
  background: #e8f5e9;
}

.description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 20px;
}

.price-box {
  background: #f5f6f5;
  padding: 15px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.price {
  font-size: 28px;
  font-weight: 700;
  color: red;
  margin-right: 30px;
}

.stock {
  font-size: 14px;
  color: #888;
}

.notice {
  font-size: 13px;
  color: #999;
  margin-bottom: 20px;
}

.buy-button {
  background: orange;
  border-color: orange;
  color: white;
  padding: 12px 30px;
  border-radius: 25px;
  font-size: 16px;
  transition: background 0.3s ease;
}

.buy-button:hover {
  background: #ff5722;
  border-color: #ff5722;
}

/* 图文介绍 */
.content-section {
  margin: 20px 0;
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  color: #2c3e50;
  padding-left: 15px;
  border-left: 4px solid #189500;
  margin-bottom: 20px;
}

.content-detail {
  font-size: 15px;
  color: #444;
  line-height: 1.8;
}

.content-detail :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 5px;
}

/* 订单弹窗 */
.order-dialog :deep(.el-dialog) {
  border-radius: 10px;
}

.order-form {
  padding: 0 20px;
}

.order-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #333;
}

.order-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.total-price {
  color: red;
  font-weight: 700;
  font-size: 18px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 响应式 */
@media (max-width: 768px) {
  .content-wrapper {
    width: 90%;
    padding: 20px;
  }

  .product-header {
    flex-direction: column;
    gap: 20px;
  }

  .product-image {
    height: 250px;
  }

  .order-dialog {
    width: 90%;
  }
}
</style>