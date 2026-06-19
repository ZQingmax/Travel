<template>
  <div class="page-container">
    <div class="content-wrapper">
      <!-- 标题 -->
      <div class="title-section">
        <h1 class="title">😊 期待您的声音</h1>
        <p class="subtitle">您的反馈是我们改进的动力</p>
      </div>

      <!-- 表单 -->
      <el-form
          ref="formRef"
          :model="data.form"
          :rules="data.rules"
          label-width="100px"
          class="feedback-form"
      >
        <el-form-item prop="title" label="反馈标题">
          <el-input
              v-model="data.form.title"
              placeholder="请输入反馈标题"
              clearable
              class="input-field"
          />
        </el-form-item>
        <el-form-item prop="content" label="反馈内容">
          <el-input
              type="textarea"
              :rows="6"
              v-model="data.form.content"
              placeholder="请输入反馈内容"
              class="textarea-field"
          />
        </el-form-item>
        <div class="button-wrapper">
          <el-button
              type="primary"
              @click="add"
              class="submit-button"
          >立即发布</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  form: {},
  rules: {
    title: [
      { required: true, message: '请输入标题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请输入内容', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const add = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/feedback/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('操作成功')
          data.form = {}
          formRef.value.resetFields()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.page-container {
  background: #f5f7fa;
  min-height: 100vh;
  padding: 40px 0;
}

.content-wrapper {
  width: 40%;
  max-width: 600px;
  margin: 0 auto;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  padding: 30px;
  animation: fadeIn 0.5s ease-in;
}

/* 标题 */
.title-section {
  text-align: center;
  margin-bottom: 30px;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 14px;
  color: #888;
}

/* 表单 */
.feedback-form {
  margin: 0;
}

.feedback-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.feedback-form :deep(.el-form-item) {
  margin-bottom: 25px;
}

.input-field :deep(.el-input__inner) {
  border-radius: 8px;
  border-color: #dcdfe6;
  transition: border-color 0.3s ease;
}

.input-field :deep(.el-input__inner:focus) {
  border-color: #189500;
}

.textarea-field :deep(.el-textarea__inner) {
  border-radius: 8px;
  border-color: #dcdfe6;
  font-size: 14px;
  line-height: 1.6;
  resize: vertical;
  transition: border-color 0.3s ease;
}

.textarea-field :deep(.el-textarea__inner:focus) {
  border-color: #189500;
}

/* 按钮 */
.button-wrapper {
  text-align: center;
}

.submit-button {
  background: #189500;
  border-color: #189500;
  color: white;
  padding: 12px 40px;
  border-radius: 25px;
  font-size: 16px;
  transition: background 0.3s ease, transform 0.2s ease;
}

.submit-button:hover {
  background: #137d00;
  border-color: #137d00;
  transform: translateY(-2px);
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
    padding: 20px;
  }

  .title {
    font-size: 24px;
  }

  .submit-button {
    padding: 10px 30px;
    font-size: 14px;
  }
}
</style>