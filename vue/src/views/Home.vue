<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      欢迎使用 DevShare - 开发者知识分享平台！
    </div>
    <div class="card" style="width: 50%;margin-top: 10px">
      <div style="font-size: 20px;padding-bottom: 20px">系统公告</div>
      <el-timeline style="max-width: 600px">
        <el-timeline-item :timestamp="item.time" placement="top" v-for="item in data.noticeData" color="green">
          <el-card>
            <h4>{{ item.title }}</h4>
            <p>{{ item.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user')),
  noticeData:[],
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if(res.code === '200'){
      data.noticeData = res.data
      if(data.noticeData.length > 3){
        data.noticeData = data.noticeData.slice(0,3)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
</script>