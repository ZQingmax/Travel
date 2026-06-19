<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 标题 -->
      <div class="title">管理员登录</div>

      <!-- 表单 -->
      <el-form
          ref="formRef"
          :model="data.form"
          :rules="data.rules"
          class="login-form"
      >
        <el-form-item prop="username">
          <el-input
              v-model="data.form.username"
              placeholder="请输入账号"
              :prefix-icon="User"
              size="large"
              clearable
              class="input-field"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              v-model="data.form.password"
              placeholder="请输入密码"
              type="password"
              :prefix-icon="Lock"
              size="large"
              show-password
              class="input-field"
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              @click="login"
              class="login-button"
          >登录</el-button>
        </el-form-item>
        <div class="links">
          <a href="/Login" class="user-link">用户登录</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  dialogVisible: true,
  form: { role: 'ADMIN' },
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
  }
})

const formRef = ref()

const login = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          router.push('/manager/home')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3)), url('@/assets/imgs/adminbg.png') no-repeat center;
  background-size: cover;
}

/* 登录框 */
.login-box {
  width: 400px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  animation: fadeIn 0.5s ease-in;
}

/* 标题 */
.title {
  font-size: 28px;
  font-weight: 600;
  color: #156bd8;
  text-align: center;
  margin-bottom: 30px;
  letter-spacing: 2px;
}

/* 表单 */
.login-form :deep(.el-form-item) {
  margin-bottom: 25px;
}

.input-field :deep(.el-input__inner) {
  border-radius: 8px;
  border-color: #dcdfe6;
  transition: border-color 0.3s ease;
}

.input-field :deep(.el-input__inner:focus) {
  border-color: #156bd8;
}

.input-field :deep(.el-input__prefix) {
  color: #156bd8;
}

/* 登录按钮 */
.login-button {
  width: 100%;
  background: #0d8293;
  border-color: #0d8293;
  color: white;
  padding: 12px 0;
  border-radius: 25px;
  font-size: 16px;
  transition: background 0.3s ease, transform 0.2s ease;
}

.login-button:hover {
  background: #096a77;
  border-color: #096a77;
  transform: translateY(-2px);
}

/* 链接 */
.links {
  text-align: left;
  font-size: 13px;
}

.user-link {
  color: #1fa9bd;
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s ease;
}

.user-link:hover {
  color: #188a9b;
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
@media (max-width: 480px) {
  .login-box {
    width: 90%;
    padding: 20px;
  }

  .title {
    font-size: 24px;
  }

  .login-button {
    padding: 10px 0;
    font-size: 14px;
  }
}
</style>