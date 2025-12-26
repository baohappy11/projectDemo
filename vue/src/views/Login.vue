<template>
  <div class="bg">
    <div style="width: 350px;background: white;border-radius: 5%;box-shadow: 0 0 10px black;padding: 40px 20px;opacity: 0.9" >
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="text-align: center; font-weight: bold;font-size: 24px">欢 迎 登 录</div>
        <div style="margin-bottom: 40px;text-align: center; font-weight: bold;font-size: 14px">（DevShare）</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" v-model="data.form.password" show-password autocomplete="off" prefix-icon="Lock" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="data.form.role" style="width: 100%">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="普通用户" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button type="primary" style="width: 100%" size="large" @click="login">登录</el-button>
        </div>
        <div style="text-align: right">
          还没有账号？请 <a href="/register" color="blue">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router/index.js";

const formRef = ref()

const data = reactive({
  form:{ role: 'ADMIN'},
  rules:{
    username: [
      {required: true , message: '请输入账号', trigger: 'blur'},
      {min:3 , message: '账号最少3位' , trigger: 'blur'},
    ],
    password: [
      {required: true , message: '请输入密码', trigger: 'blur'},
    ]
  }
})

const login = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.post('login',data.form).then(res => {
        if(res.code === '200'){
          localStorage.setItem("code_user",JSON.stringify(res.data || {}))
          ElMessage.success('登录成功')
          router.push('/')
        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>

<style scope>
.bg{
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/img/bg.jpg");
  background-size: cover;
}
</style>