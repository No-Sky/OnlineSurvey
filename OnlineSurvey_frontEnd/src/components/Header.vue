<!--
程序名：网站导航栏
功能：网站顶部导航栏
-->
<template>
  <el-container>
    <el-header>
      <div class="logo" @click="toIndex">
        <img src="../assets/images/logo.png" class="logoImg" />
        <span style="color: #303133">调研汪</span>
        <span style="font-size: 13px; margin-left: 5px; color: #606266"
          >——免费的在线问卷调查系统</span
        >
      </div>
      <div class="menu">
        <el-menu
          :default-active="activeIndex"
          active-text-color="#409eff"
          class="el-menu-demo"
          mode="horizontal"
          @select="handleSelect"
        >
          <el-menu-item index="1">问卷广场</el-menu-item>
          <el-menu-item index="2">问卷管理</el-menu-item>
          <el-menu-item index="3">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div style="float: right; margin-right: 50px; line-height: 60px">
        <!-- 未登录时显示 -->
        <template v-if="!showname">
          <el-button
            type="primary"
            plain
            style="font-size: 15px"
            @click="toLogin"
            >登录</el-button
          >
          <el-button plain style="font-size: 15px" @click="toRegiste"
            >注册</el-button
          >
        </template>

        <!-- 登录时显示 -->
        <template v-else>
          <el-dropdown trigger="click" @command="handleCommand">
            <span class="el-dropdown-link">
              <el-avatar :size="20" :src="avatar"></el-avatar>
              {{ username }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="isAdmin" command="c"
                  >后台管理</el-dropdown-item
                >
                <el-dropdown-item command="b">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </div>
    </el-header>
    <el-main style="padding: 0">
      <router-view />
    </el-main>
  </el-container>
</template>
<script>
import { useRouter } from "vue-router";
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
import { loginCheck } from "./api";
export default {
  name: "Base",
  setup() {
    const activeIndex = ref("0");
    const showname = ref(false);
    const username = ref("");
    const avatar = ref("");
    const router = useRouter();
    const store = useStore();
    const isAdmin = ref(false);
    const toIndex = () => {
      router.push("/");
    };
    const toLogin = () => {
      router.push("/login");
    };
    const toRegiste = () => {
      router.push("/register");
    };
    const toManage = () => {
      router.push("/manage");
    };
    const toProfile = () => {
      router.push("/profile");
    };
    const exit = () => {
      sessionStorage.removeItem("Flag");
      sessionStorage.removeItem("User_Data");
      store.dispatch("userStatus", false);
      window.location.reload();
      router.push("/");
    };
    const handleCommand = (command) => {
      if (command == "a") {
        toManage();
      } else if (command == "b") {
        exit();
      } else if (command == "c") {
        router.push("/admin");
      } else if (command == "d") {
        router.push("/profile");
      }
    };

    const handleSelect = (key, keyPath) => {
      if (key == 1) {
        router.push("/square");
      } else if (key == 2) {
        toManage();
      } else if (key == 3) {
        router.push("/profile");
      }
    };

    //判断session中是否存在数据，存在将showname置为true，否则false
    const state = () => {
      let user_session = JSON.parse(sessionStorage.getItem("User_Data"));
      console.log("state:" + user_session);
      if (user_session != null) {
        showname.value = true;
        username.value = user_session.username;
        avatar.value = require("@assets/images/" + user_session.avatar);
        if (user_session.userId == 1) isAdmin.value = true;
      } else {
        showname.value = false;
      }
    };

    const logincheck = () => {
      loginCheck().then((res) => {
        let data = res.data;
        // console.log(data);
        if (data.code == 0) {
          sessionStorage.removeItem("Flag");
          sessionStorage.removeItem("User_Data");
          store.dispatch("userStatus", false);
        }
      });
    };

    onMounted(() => {
      state();
      logincheck();
    });

    return {
      activeIndex,
      handleSelect,
      showname,
      username,
      avatar,
      isAdmin,
      toIndex,
      toLogin,
      toRegiste,
      handleCommand,
    };
  },
};
</script>
<style scoped>
/* .main{
    position: absolute;
    width: 100%;
    height: 100%;
  } */
/* logo图片样式 */
.logoImg {
  width: 30px;
  vertical-align: middle;
}
/* logo框样式 */
.logo {
  height: 60px;
  display: inline-block;
  line-height: 60px;
  font-size: 20px;
  position: absolute;
  left: 100px;
  color: #303133;
  cursor: pointer;
}
.el-header {
  border-bottom: 2px solid #409eff;
  background-color: white;
}
.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
}
.el-icon-arrow-down {
  font-size: 12px;
}
.demonstration {
  display: block;
  color: #8492a6;
  font-size: 14px;
  margin-bottom: 20px;
}
.menu {
  float: left;
  width: 300px;
  margin-left: 36%;
}
.el-menu {
  height: 57px;
}
.el-menu.el-menu--horizontal {
  border-bottom: none;
}
.el-menu--horizontal > .el-menu-item {
  border-bottom: none;
}
.el-menu--horizontal > .el-menu-item.is-active {
  border-bottom: none;
  height: 57px;
  color: #409eff;
}
.el-menu-item {
  transition: none;
}
</style>
