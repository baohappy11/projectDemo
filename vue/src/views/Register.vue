<template>
  <div class="bg">
    <div style="width: 350px;background: white;border-radius: 5%;box-shadow: 0 0 10px black;padding: 40px 20px;opacity: 0.9">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="text-align: center; font-weight: bold;font-size: 24px">欢 迎 注 册</div>
        <div style="margin-bottom: 40px;text-align: center; font-weight: bold;font-size: 14px">（课堂演示）</div>
        <el-form-item prop="username">
          <el-input size="large" v-model="data.form.username" autocomplete="off" prefix-icon="User" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="large" v-model="data.form.password" show-password autocomplete="off" prefix-icon="Lock" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input size="large" v-model="data.form.confirmPassword" show-password autocomplete="off" prefix-icon="Lock" placeholder="请确认密码"/>
        </el-form-item>
        <div style="margin-bottom: 20px">
          <el-button type="success" style="width: 100%" size="large" @click="register1">注册</el-button>
        </div>
        <div style="text-align: right">
          已有账号？请 <a href="/login" color="blue">登录</a>
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

const validatePass = (rule, value, callback) => {
  //value表示用户输入的确认密码
  if (value === '') {
    callback(new Error('请再次确认密码'))
  } else if (value !== data.form.password) {
    callback(new Error('输入的密码不匹配'))
  } else {
    callback()
  }
}

const data = reactive({
  form: {},
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
      {min: 3, message: '账号最少3位', trigger: 'blur'},
    ],
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
    ],
    confirmPassword:[
      {required: true, message: '请输入确认账号', trigger: 'blur'},
      {validator:validatePass,trigger:'blur'}
    ]
  }
})

const register1 = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('register', data.form).then(res => {
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

<style scope>
.bg {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-image: url("@/assets/img/bg1.jpg");
  background-size: cover;
}
</style>