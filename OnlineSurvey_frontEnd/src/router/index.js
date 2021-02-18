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

import Header_Copy from '@/components/Header_copy.vue'

const routes = [
  // {
  //   path: '/',
  //   name: 'Base',
  //   component: Header,
  //   children:[
  //     {
  //       path: '/',
  //       name: 'Index',
  //       component: Index
  //     },
  //     {
  //       path: '/login',
  //       name: 'Login',
  //       component: Login
  //     },
  //     {
  //       path: '/register',
  //       name: 'Register',
  //       component: Register
  //     },
  //     {
  //       path: '/reset',
  //       name: 'ResetPass',
  //       component: ResetPass
  //     },
  //     {
  //       path: '/manage',
  //       name: 'Manage',
  //       component: Manage
  //     },
  //   ]
  // },
  {
    path: '/display/:id',
    name: 'Display',
    component: Display
  },
  {
    path: '/thankyou',
    name: 'ThankYou',
    component: ThankYou
  },
  {
    path: '/test',
    name: 'Base',
    component: Header_Copy,
    children:[
      {
        path: '/test',
        name: 'Index',
        component: Index
      },
      {
        path: '/login',
        name: 'Login',
        component: Login
      },
      {
        path: '/register',
        name: 'Register',
        component: Register
      },
      {
        path: '/reset',
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
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


router.beforeEach((to, from, next) => {
 
  //获取用户登录成功后储存的登录标志
  let getFlag = localStorage.getItem("Flag");
  let getid = localStorage.getItem("Userid");
  //如果登录标志存在且为isLogin，即用户已登录
  if (getFlag === "isLogin"&&getid !== null) {

      //设置vuex登录状态为已登录
      store.state.isLogin = true;
      next();

      //如果已登录，还想想进入登录注册界面，则定向回首页
      if (!to.meta.isLogin) {
          //iViewUi友好提示
          // iView.Message.error('请先退出登录');
          next({
              path: '/'
          })
      }

      //如果登录标志不存在，即未登录
  } else {

      //用户想进入需要登录的页面，则定向回登录界面
      if (to.meta.isLogin) {
          next({
              path: '/login',
          })
          //用户进入无需登录的界面，则跳转继续
      } else {
          next()
      }

  }

});

router.afterEach(() => {
  window.scroll(0, 0);
});


export default router
