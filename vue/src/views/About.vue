<template>
  <div class="about-container">
    <!-- 顶部横幅 -->
    <el-card class="header-card" shadow="hover">
      <div class="header-content">
        <el-icon :size="60" color="#409EFF">
          <Notebook />
        </el-icon>
        <div class="header-text">
          <h1>DevShare - 开发者知识分享平台</h1>
          <p class="subtitle">基于 Spring Boot 3 + Vue 3 的前后端分离知识分享平台</p>
        </div>
      </div>
    </el-card>

    <!-- 项目介绍 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><InfoFilled /></el-icon>
              <span>项目简介</span>
            </div>
          </template>
          <div class="content-text">
            <p>DevShare是一个轻量级的知识分享平台，专为开发者设计。</p>
            <p>平台提供了便捷的知识发布、管理和浏览功能，帮助开发者分享技术经验、学习新知识。</p>
            <el-divider />
            <h4>核心特性</h4>
            <ul>
              <li>📝 富文本编辑器，支持图片上传</li>
              <li>🔐 JWT身份认证，安全可靠</li>
              <li>📊 分页查询，高效快速</li>
              <li>🎨 响应式设计，界面美观</li>
              <li>📁 分类管理，内容清晰</li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><PieChart /></el-icon>
              <span>系统统计</span>
            </div>
          </template>
          <div class="stats-content">
            <el-row :gutter="10">
              <el-col :span="12">
                <div class="stat-item">
                  <el-icon :size="30" color="#409EFF"><User /></el-icon>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.userCount }}</div>
                    <div class="stat-label">注册用户</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="stat-item">
                  <el-icon :size="30" color="#67C23A"><Document /></el-icon>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.knowledgeCount }}</div>
                    <div class="stat-label">知识分享</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="12" style="margin-top: 20px">
                <div class="stat-item">
                  <el-icon :size="30" color="#E6A23C"><Folder /></el-icon>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.categoryCount }}</div>
                    <div class="stat-label">知识分类</div>
                  </div>
                </div>
              </el-col>
              <el-col :span="12" style="margin-top: 20px">
                <div class="stat-item">
                  <el-icon :size="30" color="#F56C6C"><Bell /></el-icon>
                  <div class="stat-info">
                    <div class="stat-value">{{ stats.noticeCount }}</div>
                    <div class="stat-label">系统公告</div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 技术栈 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Platform /></el-icon>
              <span>后端技术栈</span>
            </div>
          </template>
          <div class="tech-stack">
            <el-tag v-for="tech in backendTech" :key="tech.name" :type="tech.type" effect="plain" size="large" style="margin: 5px">
              {{ tech.name }}
            </el-tag>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <el-icon><Monitor /></el-icon>
              <span>前端技术栈</span>
            </div>
          </template>
          <div class="tech-stack">
            <el-tag v-for="tech in frontendTech" :key="tech.name" :type="tech.type" effect="plain" size="large" style="margin: 5px">
              {{ tech.name }}
            </el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 开发者信息 -->
    <el-card shadow="hover" style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <el-icon><UserFilled /></el-icon>
          <span>开发者信息</span>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="项目名称" :span="2">DevShare 开发者知识分享平台</el-descriptions-item>
        <el-descriptions-item label="开发者">{{ developer.name }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ developer.date }}</el-descriptions-item>
        <el-descriptions-item label="版本">v1.0.0</el-descriptions-item>
        <el-descriptions-item label="项目类型">课程设计项目</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 页脚 -->
    <div class="footer">
      <el-divider />
      <p>© 2025 DevShare. All rights reserved.</p>
      <p style="font-size: 12px; color: #999">本项目仅用于课程设计学习用途</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
  Notebook, InfoFilled, PieChart, User, Document, Folder, Bell,
  Platform, Monitor, UserFilled
} from '@element-plus/icons-vue'
import request from '@/utils/request'

// 系统统计数据
const stats = reactive({
  userCount: 0,
  knowledgeCount: 0,
  categoryCount: 0,
  noticeCount: 0
})

// 后端技术栈
const backendTech = ref([
  { name: 'Java 17', type: '' },
  { name: 'Spring Boot 3.x', type: 'success' },
  { name: 'MyBatis', type: 'info' },
  { name: 'MySQL 8.0', type: 'warning' },
  { name: 'JWT', type: 'danger' },
  { name: 'PageHelper', type: '' },
  { name: 'Hutool', type: 'success' },
  { name: 'Maven', type: 'info' }
])

// 前端技术栈
const frontendTech = ref([
  { name: 'Vue 3.5', type: 'success' },
  { name: 'Element Plus', type: 'info' },
  { name: 'Vue Router', type: '' },
  { name: 'Axios', type: 'warning' },
  { name: 'Vite', type: 'danger' },
  { name: 'WangEditor', type: 'success' },
  { name: 'JavaScript', type: '' },
  { name: 'CSS3', type: 'info' }
])

// 开发者信息
const developer = reactive({
  name: '宝happy',
  date: '2025年12月'
})

// 加载统计数据
const loadStats = () => {
  // 加载用户数量
  request.get('/user/selectPage', { params: { pageNum: 1, pageSize: 1 } }).then(res => {
    if (res.code === '200') {
      stats.userCount = res.data?.total || 0
    }
  }).catch(() => {
    stats.userCount = '- -'
  })

  // 加载知识数量
  request.get('/knowledge/selectPage', { params: { pageNum: 1, pageSize: 1 } }).then(res => {
    if (res.code === '200') {
      stats.knowledgeCount = res.data?.total || 0
    }
  }).catch(() => {
    stats.knowledgeCount = '- -'
  })

  // 加载分类数量
  request.get('/category/selectPage', { params: { pageNum: 1, pageSize: 1 } }).then(res => {
    if (res.code === '200') {
      stats.categoryCount = res.data?.total || 0
    }
  }).catch(() => {
    stats.categoryCount = '- -'
  })

  // 加载公告数量
  request.get('/notice/selectPage', { params: { pageNum: 1, pageSize: 1 } }).then(res => {
    if (res.code === '200') {
      stats.noticeCount = res.data?.total || 0
    }
  }).catch(() => {
    stats.noticeCount = '- -'
  })
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.about-container {
  padding: 20px;
}

.header-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.header-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-content h1 {
  margin: 0;
  font-size: 28px;
  color: white;
}

.header-content .subtitle {
  margin: 5px 0 0 0;
  font-size: 14px;
  opacity: 0.9;
  color: white;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  font-size: 16px;
}

.content-text {
  line-height: 1.8;
}

.content-text p {
  margin: 10px 0;
  color: #666;
}

.content-text h4 {
  margin: 15px 0 10px 0;
  color: #409EFF;
}

.content-text ul {
  list-style: none;
  padding: 0;
}

.content-text ul li {
  padding: 8px 0;
  color: #666;
}

.stats-content {
  padding: 10px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.tech-stack {
  padding: 10px;
  min-height: 120px;
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.footer {
  margin-top: 30px;
  text-align: center;
  color: #909399;
  font-size: 14px;
}

.footer p {
  margin: 5px 0;
}

:deep(.el-card__header) {
  background: #f5f7fa;
  padding: 15px 20px;
}

:deep(.el-descriptions__label) {
  font-weight: bold;
}
</style>
