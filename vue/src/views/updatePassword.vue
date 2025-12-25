<template>
  <div class="card" style="width: 50%">
    <div style="font-size: 16px;">修改密码</div>
    <el-form ref="formRef" :rules = "data.rules" :model="data.user" label-width="80px" style="padding: 20px 30px 10px 0px">
      <el-form-item label="原密码" prop="password">
        <el-input show-password  v-model="data.user.password" autocomplete="off" prefix="Lock" placeholder="请输入原密码"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input show-password  v-model="data.user.newPassword" autocomplete="off" prefix="Lock" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="newConfirmPassword">
        <el-input show-password  v-model="data.user.newConfirmPassword" autocomplete="off" prefix="Lock" placeholder="请确认密码"></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" style="padding: 20px 30px" @click="updatePassword">保存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  rules:{
    password:[
      {required:true , message: '请填写原密码' , trigger: 'blur'}
    ],
    newPassword:[
      {required:true , message: '请填写新密码' , trigger: 'blur'}
    ],
    newConfirmPassword:[
      {required:true , message: '请再次确认密码' , trigger: 'blur'}
    ],
  }
})

const formRef = ref()

const updatePassword = () => {
  formRef.value.validate(valid => {
    if(valid){
      request.put('/updatePassword' , data.user).then(res => {
        if(res.code === '200'){
          ElMessage.success('修改成功')
          setInterval(()=>{
            localStorage.removeItem('code_user')
            location.href = '/login'
          } , 500)
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>