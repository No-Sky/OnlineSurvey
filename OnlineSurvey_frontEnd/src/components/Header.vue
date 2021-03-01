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
              {{ username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item v-if="isAdmin" command="c"
                  >后台管理</el-dropdown-item
                >
                <el-dropdown-item v-if="!isAdmin" command="a">问卷管理</el-dropdown-item>
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
    const showname = ref(false);
    const username = ref("test");
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
      } else if (command == 'c') {
        router.push("/admin");
      }
    };
    //判断session中是否存在数据，存在将showname置为true，否则false
    const state = () => {
      let user_session = JSON.parse(sessionStorage.getItem("User_Data"));
      console.log("state:" + user_session);
      if (user_session != null) {
        showname.value = true;
        username.value = user_session.username;
        if (user_session.userId == 1) isAdmin.value = true;
      } else {
        showname.value = false;
      }
    };

    const logincheck = () => {
      loginCheck().then((res) => {
        let data = res.data;
        if (data.code == 0) {
          exit();
        } else {
          sessionStorage.setItem("User_Data", JSON.stringify(data.data));
          sessionStorage.setItem("Flag", "isLogin");
          store.dispatch("userStatus", true);
          state();
        }
      });
    };

    onMounted(() => {
      state();
    });

    return {
      showname,
      username,
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
</style>
