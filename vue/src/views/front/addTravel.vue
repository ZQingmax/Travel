<template>
  <div class="page-container">
    <div class="content-wrapper">
      <!-- 标题和返回 -->
      <div class="header-section">
        <el-button
            class="back-button"
            @click="router.back()"
            plain
            circle
        >
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <h1 class="title">写游记</h1>
      </div>

      <!-- 表单 -->
      <el-form
          ref="form"
          :model="data.form"
          :rules="data.rules"
          label-width="100px"
          class="travel-form"
      >
        <el-form-item prop="title" label="标题">
          <el-input
              v-model="data.form.title"
              placeholder="请输入标题"
              clearable
              class="input-field"
          />
        </el-form-item>

        <el-form-item label="封面">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :headers="{ 'token': data.user.token }"
              list-type="picture-card"
              :on-success="handleFileUpload"
              :limit="1"
              :file-list="data.form.cover ? [{ url: toFileUrl(data.form.cover) }] : []"
          >
            <el-icon><Plus /></el-icon>
            <template #tip>
              <div class="upload-tip">建议上传尺寸为 800x450 的图片</div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item prop="descr" label="简介">
          <el-input
              type="textarea"
              :rows="4"
              v-model="data.form.descr"
              placeholder="请输入简介"
              class="textarea-field"
          />
        </el-form-item>

        <el-form-item prop="startDate" label="出行日期">
          <el-date-picker
              v-model="data.form.startDate"
              type="datetime"
              format="YYYY-MM-DD HH:mm"
              value-format="YYYY-MM-DD HH:mm"
              placeholder="请选择出行日期"
              style="width: 100%"
          />
        </el-form-item>

        <el-form-item prop="money" label="费用">
          <el-input-number
              v-model="data.form.money"
              :min="1"
              placeholder="请输入费用"
              style="width: 200px"
          />
          <span class="unit">元</span>
        </el-form-item>

        <el-form-item prop="location" label="出行地点">
          <el-input
              v-model="data.form.location"
              placeholder="请输入出行地点"
              clearable
              class="input-field"
          />
        </el-form-item>

        <el-form-item prop="days" label="出行天数">
          <el-input-number
              v-model="data.form.days"
              :min="1"
              placeholder="请输入出行天数"
              style="width: 200px"
          />
          <span class="unit">天</span>
        </el-form-item>

        <el-form-item prop="content" label="内容">
          <div class="editor-wrapper">
            <Toolbar
                style="border-bottom: 1px solid #eee"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
                class="editor"
            />
          </div>
        </el-form-item>

        <div class="submit-wrapper">
          <el-button
              type="primary"
              @click="save"
              class="submit-button"
          >发布游记</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, shallowRef, onBeforeUnmount } from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import { getFileKey, toFileUrl } from "@/utils/file.js";
import { ElMessage } from "element-plus";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue';
import '@wangeditor/editor/dist/css/style.css';
import { ArrowLeft, Plus } from '@element-plus/icons-vue';

const data = reactive({
  id: router.currentRoute.value.query.id,
  user: JSON.parse(localStorage.getItem('xm-user')),
  form: {},
  rules: {
    title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
    descr: [{ required: true, message: '请输入简介', trigger: 'blur' }],
    startDate: [{ required: true, message: '请选择出行日期', trigger: 'blur' }],
    money: [{ required: true, message: '请输入花费金额', trigger: 'blur' }],
    location: [{ required: true, message: '请输入出行地点', trigger: 'blur' }],
    days: [{ required: true, message: '请输入出行天数', trigger: 'blur' }],
  }
})

const baseUrl = import.meta.env.VITE_BASE_URL
const form = ref()

if (data.id) {
  request.get('/travels/selectById/' + data.id).then(res => {
    data.form = res.data
  })
}

const save = () => {
  if (data.id) update()
  else add()
}

const add = () => {
  form.value.validate((valid) => {
    if (valid) {
      if (!data.form.content) {
        ElMessage.warning('请输入内容')
        return
      }
      request.post('/travels/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('发布成功')
          if (res.data.id) router.replace('/front/addTravel?id=' + res.data.id)
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const update = () => {
  form.value.validate((valid) => {
    if (valid) {
      if (!data.form.content) {
        ElMessage.warning('请输入内容')
        return
      }
      request.put('/travels/update', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('发布成功')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const handleFileUpload = (res) => {
  if (res.code === '200') {
    data.form.cover = getFileKey(res.data)
  } else {
    ElMessage.error(res.msg)
  }
}

/* wangEditor5 初始化 */
const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
editorConfig.MENU_CONF['uploadImage'] = {
  headers: { token: data.user.token },
  server: baseUrl + '/files/wang/upload',
  fieldName: 'file'
}
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) editor.destroy()
})
const handleCreated = (editor) => {
  editorRef.value = editor
}
</script>

<style scoped>
.page-container {
  background: #f5f7fa;
  min-height: 100vh;
  padding: 30px 0;
}

.content-wrapper {
  width: 60%;
  max-width: 800px;
  margin: 0 auto;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  padding: 30px;
  animation: fadeIn 0.5s ease-in;
}

/* 标题和返回 */
.header-section {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30px;
  position: relative;
}

.back-button {
  position: absolute;
  left: 0;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #2c3e50;
}

/* 表单 */
.travel-form :deep(.el-form-item__label) {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.travel-form :deep(.el-form-item) {
  margin-bottom: 25px;
}

.input-field :deep(.el-input__inner),
.travel-form :deep(.el-date-editor.el-input__inner) {
  border-radius: 8px;
  transition: border-color 0.3s ease;
}

.input-field :deep(.el-input__inner:focus),
.travel-form :deep(.el-date-editor.el-input__inner:focus) {
  border-color: #59df3b;
}

.textarea-field :deep(.el-textarea__inner) {
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.6;
  resize: vertical;
  transition: border-color 0.3s ease;
}

.textarea-field :deep(.el-textarea__inner:focus) {
  border-color: #59df3b;
}

.unit {
  margin-left: 10px;
  color: #666;
  font-size: 14px;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

/* 编辑器 */
.editor-wrapper {
  height: 550px;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.editor {
  height: 500px;
  overflow-y: auto;
}

/* 提交按钮 */
.submit-wrapper {
  text-align: center;
}

.submit-button {
  background: #59df3b;
  border-color: #59df3b;
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
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
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
  }
}
</style>