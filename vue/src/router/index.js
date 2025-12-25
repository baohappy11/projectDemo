import { createRouter, createWebHistory } from 'vue-router'
  import HomeView from '../views/Home.vue'

  const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
      {path: '/', redirect: '/manager/home'},
      {path: '/manager', name: '管理页', component: () => import('../views/Manager.vue'),
        children:[
          {path: 'home', name: '首页', component: () => import('../views/Home.vue'),},
          {path: 'admin', name: '管理员信息', component: () => import('../views/Admin.vue'),},
          {path: 'user', name: '普通用户信息', component: () => import('../views/User.vue'),},
          {path: 'about', name: '关于', component: () => import('../views/Admin.vue'),},
          {path: 'person', name: '个人中心', component: () => import('../views/Person.vue'),},
          {path: 'updatePassword', name: '修改密码', component: () => import('../views/updatePassword.vue'),},
          {path: 'notice', name: '公告管理', component: () => import('../views/Notice.vue'),},
          {path: 'knowledge', name: '知识分享', component: () => import('../views/Knowledge.vue'),},
          {path: 'category', name: '分类管理', component: () => import('../views/Category.vue'),},
          {path: 'reservation', name: '预约管理', component: () => import('../views/Reservation.vue'),},
        ]},
      {path: '/login', name: '登录', component: () => import('../views/Login.vue'),},
      {path: '/register', name: '注册', component: () => import('../views/Register.vue'),},
      {path: '/notFound', name: '404', component: () => import('../views/404.vue'),},
      {path: '/:pathMatch(.*)',  redirect:'/notFound',},
    ],
  })

  // 路由守卫：验证用户登录状态
  router.beforeEach((to, from, next) => {
    // 获取本地存储的用户信息
    const userStr = localStorage.getItem('code_user')

    // 定义不需要登录就能访问的白名单路由
    const whiteList = ['/login', '/register', '/notFound']

    // 如果访问的是白名单路由，直接放行
    if (whiteList.includes(to.path)) {
      next()
      return
    }

    // 如果用户未登录（没有用户信息），重定向到登录页
    if (!userStr) {
      next('/login')
      return
    }

    // 尝试解析用户信息
    try {
      const user = JSON.parse(userStr)
      // 验证用户信息是否有效（至少要有 token）
      if (!user || !user.token) {
        localStorage.removeItem('code_user')
        next('/login')
        return
      }
      // 用户已登录且信息有效，放行
      next()
    } catch (e) {
      // 用户信息格式错误，清除并重定向到登录页
      localStorage.removeItem('code_user')
      next('/login')
    }
  })

  export default router