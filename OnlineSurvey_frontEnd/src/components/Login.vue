<!--
程序名：网站登录页面
功能：进入网站的入口
-->
<template>
  <div class="login">
    <div class="main_login">
      <div class="title">登 录</div>
      <el-row type="flex" justify="center">
        <!-- 登录表单 -->
        <el-form ref="loginFormRef" :rules="rules" :model="loginForm">
            <el-form-item prop="email">
                <el-input @keyup.enter="login('loginForm')" icon="el-icon-search" placeholder="请输入邮箱" v-model="loginForm.email">
                  <i class="el-icon-user" slot: prefix> </i>
                </el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input @keyup.enter="login('loginForm')" placeholder="请输入密码" v-model="loginForm.password" show-password>
                  <i class="el-icon-lock" slot: prefix> </i>
                </el-input>
            </el-form-item>
            <!-- 登录按钮 -->
            <el-form-item>
              <el-button type="primary" @click="loginBtn('loginForm')" style="text-align: center;width: 150px">登  录</el-button>
            </el-form-item>
          </el-form>
      </el-row>
      <!-- 注册和忘记密码链接 -->
      <div class="link">
        <el-link type="primary" :underline="false" href="/register">注册新账号</el-link>
        <el-link type="primary" :underline="false" href="/reset">忘记密码</el-link>
      </div>
    </div>
  </div>
</template>
<script>
import { reactive, ref, toRef } from "vue";
import { login } from "./api";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import qs from 'qs';

export default {
  name: "Login",
  setup() {
    const router = useRouter();
    const store = useStore();
    const loginFormRef = ref(null);
    const loginForm = reactive({
      email: "", //邮箱
      password: "", //密码
    });
    const rules = ref({
      //表单验证（邮箱验证规则）
      email: [
        { required: true, message: "邮箱不能为空", trigger: "blur" },
        {
          pattern: /^[0-9a-zA-Z_\\.-]+[@][0-9a-zA-Z_\\.-]+([\\.][a-zA-Z]+){1,2}$/,
          message: "不是正确的邮箱",
          trigger: "blur",
        },
      ],
      //表单验证（密码验证规则）
      password: [
        { required: true, message: "密码不能为空", trigger: "blur" },
        { min: 6, message: "密码长度最少为6位", trigger: "blur" },
      ],
    });
    const loginBtn = (formName) => {
      let loginData = {
        email: loginForm.email,
        password: loginForm.password,
      };
      loginFormRef.value.validate((valid) => {
        if (valid) {
          login(qs.stringify(loginData)).then((res) => {
              let data = res.data;
              console.log(data)
              if (data.code == 0) {
                ElMessage.warning(data.description);
              } else {
                //登陆成功后设置了，localStorage 保存登陆状态和userid
                ElMessage.success("恭喜你登录成功，将自动跳转首页！");
                sessionStorage.setItem("User_Data", JSON.stringify(data.data));
                sessionStorage.setItem("Flag", "isLogin");
                store.dispatch("userStatus", true);
                router.push("/");
              }
            }).catch(function (error) {
              if (error) {
                console.log("登录请求失败");
                ElMessage.error("登录请求失败，请检查网络");
              }
            });
        }
      });
    };
    return {
      loginForm,
      loginFormRef,
      rules,
      loginBtn,
    };
  },
};
</script>

<style scoped>
/* 主页面样式 */
.login {
  position: fixed;
  width: 100%;
  height: 100%;
  background-color: #e4e7ed;
}
/* 标题样式 */
.title {
  font-size: large; /*字体大小*/
  font-weight: bolder; /*字体加粗*/
  text-align: center;
  color: black;
}
/* 登录部分div样式 */
.main_login {
  position: absolute;
  left: 48%;
  top: 40%;
  width: 320px;
  height: 250px;
  margin: -190px 0 0 -190px;
  padding: 40px;
  border-radius: 5px; /*圆角边框*/
  background: #f2f6fc;
}
/* 表单样式 */
.el-form {
  padding-top: 5%;
  padding-left: 10%;
  padding-right: 10%;
  width: 280px;
}
/* .el-row标签样式 */
.el-row {
  height: 100%;
  width: 100%;
}
/* 文字链接div样式 */
.link {
  margin-top: -13%;
  text-align: center; /* 文本居中 */
  margin-left: -5%;
}
/* 文字链接样式 */
.el-link {
  margin-left: 8px;
  line-height: 20px;
  font-size: 8px;
}
</style>
