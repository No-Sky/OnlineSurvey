/**
 * 程序名：前端路由配置
 * 功能：配置前端路由
 */

import { createRouter, createWebHistory, useRoute } from 'vue-router'
import { useStore } from 'vuex'
import { ElMessage } from 'element-plus'
import Header from '@/components/Header.vue'
import Index from '@/components/Index.vue'
import Login from '@/components/Login.vue'
import Register from '@/components/Register.vue'
import Manage from '@/components/Manage.vue'
import Profile from '@/components/Profile.vue'
import ResetPass from '@/components/ResetPass.vue'
import Display from '@/components/Display.vue'
import ThankYou from '@/components/ThankYou.vue'

import Admin from '@/components/admin/Admin.vue'


const routes = [
  {
    path: '/',
    name: 'Base',
    component: Header,
    children: [
      {
        path: '/',
        name: 'Index',
        component: Index,
        meta: {
          title: '调研汪'
        }
      },
      {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
          isLogin: false,
          title: "登录————调研汪"
        }
      },
      {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: {
          isLogin: false,
          title: '注册————调研汪'
        }
      },
      {
        path: '/reset',
        name: 'ResetPass',
        component: ResetPass,
        meta: {
          title: '重置密码————调研汪'
        }
      },
      {
        path: '/manage',
        name: 'Manage',
        component: Manage,
        meta: {
          isLogin: true,
          title: "问卷管理————调研汪"
        }
      },
      {
        path: '/profile',
        name: 'Profile',
        component: Profile,
        meta: {
          isLogin: false,
          title: "个人信息————调研汪"
        }
      },
    ]
  },
  {
    path: '/display/:id',
    name: 'Display',
    component: Display,
    meta: {
      title: '问卷填写————调研汪'
    }
  },
  {
    path: '/thankyou',
    name: 'ThankYou',
    component: ThankYou,
    meta: {
      title: '感谢填写————调研汪'
    }
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Header,
    children: [
      {
        path: '/admin',
        component: Admin,
        meta: {
          isLogin: true,
          title: "后台管理"
        }
      },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


router.beforeEach((to, from, next) => {

  // 路由发生变化修改页面title
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  
  //获取用户登录成功后储存的登录标志
  let getFlag = sessionStorage.getItem("Flag");
  let getUser = sessionStorage.getItem("User_Data");
  //如果登录标志存在且为isLogin，即用户已登录
  if (getFlag === "isLogin" && getUser !== null) {
    // 如果已登录，还想进入登录注册界面，则定向回首页
    if (to.meta.isLogin != null && !to.meta.isLogin) {
      ElMessage.warning("您已经登录，无需再登录")
      router.push("/")
    }
    //如果登录标志不存在，即未登录
  } else {

    //用户想进入需要登录的页面，则定向回登录界面
    if (to.meta.isLogin) {
      ElMessage.warning("请先登录网站！")
      router.push("/login")
      //用户进入无需登录的界面，则跳转继续
    } 
  }

  next()

});

router.afterEach(() => {
  window.scroll(0, 0);
});


export default router
