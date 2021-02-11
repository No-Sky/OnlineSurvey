/**
 * 程序名：前端路由配置
 * 功能：配置前端路由
 */

import { createRouter, createWebHistory } from 'vue-router'
import Header from '@/components/Header.vue'
import Index from '@/components/Index.vue'
import Login from '@/components/Login.vue'
import Register from '@/components/Register.vue'
import Manage from '@/components/Manage.vue'
import ResetPass from '@/components/ResetPass.vue'
import Display from '@/components/Display.vue'
import ThankYou from '@/components/ThankYou.vue'

const routes = [
  {
    path: '/',
    name: 'Base',
    component: Header,
    children:[
      {
        path: '/',
        name: 'Index',
        component: Index
      },
      // {
      //   path: '/index',
      //   name: 'Index',
      //   component: Index
      // },
      {
        path: 'login',
        name: 'Login',
        component: Login
      },
      {
        path: 'register',
        name: 'Register',
        component: Register
      },
      {
        path: 'reset',
        name: 'ResetPass',
        component: ResetPass
      },
      {
        path: '/manage',
        name: 'Manage',
        component: Manage
      },
    ]
  },
  {
    path: '/display/:id',
    name: 'Display',
    component: Display
  },
  {
    path: '/thankyou',
    name: 'ThankYou',
    component: ThankYou
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
