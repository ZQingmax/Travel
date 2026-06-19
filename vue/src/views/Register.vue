<template>
  <div class="register-container">
    <div class="register-box">
      <!-- 标题 -->
      <div class="title">欢迎注册</div>

      <!-- 表单 -->
      <el-form
          ref="formRef"
          :model="data.form"
          :rules="data.rules"
          class="register-form"
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
        <el-form-item prop="confirmPassword">
          <el-input
              v-model="data.form.confirmPassword"
              placeholder="请确认密码"
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
              @click="register"
              class="register-button"
          >注册</el-button>
        </el-form-item>
        <div class="links">
          已有账号？<a href="/login" class="login-link">登录</a>
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

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== data.form.password) {
    callback(new Error("确认密码与原密码不一致!"))
  } else {
    callback()
  }
}

const data = reactive({
  form: {},
  rules: {
    username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
    confirmPassword: [{ validator: validatePass, trigger: 'blur' }]
  }
})

const formRef = ref()

const register = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('注册成功')
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3)), url('@/assets/imgs/registerbg.png') no-repeat center;
  background-size: cover;
}

/* 注册框 */
.register-box {
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
  color: #bc840b;
  text-align: center;
  margin-bottom: 30px;
  letter-spacing: 2px;
}

/* 表单 */
.register-form :deep(.el-form-item) {
  margin-bottom: 25px;
}

.input-field :deep(.el-input__inner) {
  border-radius: 8px;
  border-color: #dcdfe6;
  transition: border-color 0.3s ease;
}

.input-field :deep(.el-input__inner:focus) {
  border-color: #bc840b;
}

.input-field :deep(.el-input__prefix) {
  color: #bc840b;
}

/* 注册按钮 */
.register-button {
  width: 100%;
  background: #bc840b;
  border-color: #bc840b;
  color: white;
  padding: 12px 0;
  border-radius: 25px;
  font-size: 16px;
  transition: background 0.3s ease, transform 0.2s ease;
}

.register-button:hover {
  background: #a37109;
  border-color: #a37109;
  transform: translateY(-2px);
}

/* 链接 */
.links {
  text-align: right;
  font-size: 13px;
  color: #666;
}

.login-link {
  color: #bc840b;
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s ease;
}

.login-link:hover {
  color: #a37109;
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
  .register-box {
    width: 90%;
    padding: 20px;
  }

  .title {
    font-size: 24px;
  }

  .register-button {
    padding: 10px 0;
    font-size: 14px;
  }
}
</style>