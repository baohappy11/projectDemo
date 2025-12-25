<template>
  <div>
<!--    网页头部-->
    <div style="height: 60px;display: flex;">
      <div style="display: flex;align-items: center;padding-left: 10px;width: 220px;background-color: navy">
          <img src="../assets/img/logo.webp" alt="" style="width: 40px;height: 40px;border-radius: 50%;">
          <span style="font-size: 24px;color: white">DevShare</span>
      </div>
      <div style="flex: 1;border-bottom: 1px solid blue;display: flex;align-items: center;padding-left: 10px">
        <span style="padding-left: 20px; cursor: pointer" @click="router.push('home')">首页</span> /
        <span>{{ router.currentRoute.value.name }}</span>
      </div>
      <div style="width: fit-content;padding-right: 20px;display: flex;align-items: center;border-bottom: 1px solid blue">
        <el-dropdown>
          <div style="display: flex;align-items: center;">
            <img v-if="data.user?.avatar"  style="width: 40px;height: 40px;border-radius: 50%;" :src="data.user?.avatar" alt="">
            <img v-else style="width: 40px;height: 40px;border-radius: 50%;" src="@/assets/img/admin.jpg" alt="">
            <span style="margin-right: 5px">{{ data.user?.name }}</span>
          </div>
          <template #dropdown>

            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item command="c" @click="logout">退出登录</el-dropdown-item>
              <el-dropdown-item command="c">附加功能</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

    </div>

<!--    网页主体-->
    <div style="display: flex">
<!--      导航部分-->
      <div style="width: 220px">
        <el-menu router default-active="2" :default-openeds="['1','2']"  style="min-height: calc(100vh - 60px)"> <!--设置哪个index高亮 导航高度-->

          <el-menu-item index="home" >
            <el-icon><HomeFilled /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="reservation" >
            <el-icon><Calendar /></el-icon>
            <span>预约管理</span>
          </el-menu-item>

          <el-sub-menu index="1">
            <template #title>
              <el-icon><Monitor /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="notice" v-if="data.user.role==='ADMIN'">公告管理</el-menu-item>
            <el-menu-item index="notice" v-else>系统公告</el-menu-item>
            <el-menu-item index="knowledge" >知识分享</el-menu-item>
            <el-menu-item index="category" v-if="data.user.role==='ADMIN'">分类管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2" v-if="data.user.role==='ADMIN'">
            <template #title>
              <el-icon><location /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="admin">管理员信息</el-menu-item>
            <el-menu-item index="user">普通用户信息</el-menu-item>
            <el-menu-item index="about">关于</el-menu-item>
            <el-menu-item index="1-1">其他页面</el-menu-item>
            <el-menu-item index="1-2">退出系统</el-menu-item>
          </el-sub-menu>
        </el-menu>

      </div>
<!--      子页面-->
      <div style="padding: 10px;background-color: #dfe9f8;flex:1;width: 0">
        <RouterView @updateUser="updateUser1"/>
      </div>
    </div>

  </div>
</template>

<script setup>

import router from "@/router/index.js";
import {reactive} from "vue";

const data = reactive({
  user:JSON.parse(localStorage.getItem('code_user'))
})

const logout =() => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}

const updateUser1 = () =>{
  data.user = JSON.parse(localStorage.getItem("code_user" || '{}')) }
</script>

<style>
.el-tooltip__trigger {
  outline: none;
}

.el-dropdown {
  cursor: pointer;
}

.el-menu {
  background-color: navy;
  border: none;
}
.el-menu-item {
  color: #ffffff;
}
.el-sub-menu__title {
  color: #ffffff;
  background-color: navy;
}

.el-menu-item:not(.is-active):hover {
  background-color: #7a7af4;
  color:navy;
}
.el-sub-menu__title:hover {
  background-color: navy;
}

.el-menu .is-active {
  background-color: #3c3cea;
  color: white;
}
</style>