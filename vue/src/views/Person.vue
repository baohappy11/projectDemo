<template>
  <div class="card" style="width: 50%">
    <div style="font-size: 15px">个人中心</div>
    <el-form :model="data.user" label-width="80px" style="padding:20px 30px 10px 0">
      <el-form-item>
        <el-upload
            action="http://localhost:9090/files/upload"
            :headers="{token:data.user.token}"
            :on-success="handleFileSuccess"
            list-type="picture">
          <el-button size="small" type="primary">点击上传头像</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="账号" prop="username" >
        <el-input v-model="data.user.username" autocomplete="off" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="data.user.name" autocomplete="off" />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="data.user.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="data.user.email" autocomplete="off" />
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button style="padding: 20px 35px" type="primary" @click="update">保存</el-button>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user : JSON.parse(localStorage.getItem('code_user' )|| '{}')
})

const emit = defineEmits(['updateUser'])//定义updateUser事件，并通过emit函数触发

const handleFileSuccess = (res) => {
  data.user.avatar = res.data
}

const update = () =>{
  let url
  if(data.user.role === 'ADMIN'){
    request.put('admin/update' , data.user).then(res =>{
      if(res.code === '200'){
        ElMessage.success('更新成功')
        localStorage.setItem("code_user" , JSON.stringify(data.user))
        emit('updateUser')//emit函数触发‘updateUser’事件
      }
   })
  }
  if(data.user.role === 'USER'){
    request.put('user/update' , data.user).then(res =>{
      if(res.code === '200'){
        ElMessage.success('更新成功')
        localStorage.setItem("code_user" , JSON.stringify(data.user))
        emit('updateUser')//emit函数触发‘updateUser’事件
      }
    })
  }

}

</script>