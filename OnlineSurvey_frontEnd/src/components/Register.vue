<!--
程序名：网站注册页面
功能：网站注册
-->
<template>
  <div class="register">
    <div class="main-register">
      <div class="title">注 册</div>
      <!-- 注册表单 -->
      <el-row>
        <el-form
          :model="registerForm"
          status-icon
          :rules="rules"
          size="medium"
          ref="registerFormRef"
          label-width="100px"
          class="demo-registeForm"
        >
          <el-form-item prop="username" label="用户名">
            <el-input
              @keyup.enter="Register('registerForm')"
              v-model="registerForm.username"
              placeholder="请输入用户名"
            ></el-input>
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input
              @keyup.enter="Register('registerForm')"
              v-model="registerForm.email"
              placeholder="请输入邮箱"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-input
              @keyup.enter="Register('registerForm')"
              v-model="registerForm.pass"
              autocomplete="off"
              placeholder="请输入密码(不少于6位)"
              show-password
            >
            </el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkPass">
            <el-input
              @keyup.enter="Register('registerForm')"
              v-model="registerForm.checkPass"
              autocomplete="off"
              placeholder="请再次输入密码"
              show-password
            >
            </el-input>
          </el-form-item>

          <send-vertify-code
            :hasIcon="true"
            :email="registerForm.email"
            :sendCode="sendVertifyBtn"
          />

          <el-form-item style="margin-left: -25%">
            <el-button type="primary" @click="registerBtn('registerForm')"
              >注册</el-button
            >
            <el-button
              @click="resetForm('registerForm')"
              style="margin-right: -5%"
              >重置</el-button
            >
          </el-form-item>
        </el-form>
      </el-row>
      <!-- 登录页面链接 -->
      <div class="link">
        <el-link type="primary" :underline="false" href="login"
          >已有账号?去登录</el-link
        >
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from "vue";
import { register, sendVertify } from "./api";
import SendVertifyCode from "./SendVertifyCode.vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";

export default {
  name: "Register",
  components: {
    SendVertifyCode,
  },
  setup() {
    //邮箱验证
    const validateEmail = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请正确填写邮箱"));
      } else {
        if (value !== "") {
          var reg = /^[0-9a-zA-Z_\\.-]+[@][0-9a-zA-Z_\\.-]+([\\.][a-zA-Z]+){1,2}$/;
          if (!reg.test(value)) {
            callback(new Error("请输入有效的邮箱"));
          }
        }
        callback();
      }
    };

    // 检查密码
    let validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.registerForm.checkPass !== "") {
          registerFormRef.value.validateField("checkPass");
        }
        callback();
      }
    };
    // 确认密码验证
    let validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== registerForm.pass) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };

    //表单验证规则
    const rules = {
      // 用户名验证规则
      username: [
        { required: true, message: "用户名不能为空", trigger: "blur" },
        { max: 20, message: "用户名长度最长20位", trigger: "blur" },
      ],
      // 邮箱验证规则
      email: [{ required: true, validator: validateEmail, trigger: "blur" }],
      // 密码验证规则
      pass: [
        { required: true, validator: validatePass, trigger: "blur" },
        { min: 6, message: "密码长度最少为6位", trigger: "blur" },
        { max: 16, message: "密码长度不能超过16位", trigger: "blur" },
      ],
      // 检查密码验证规则
      checkPass: [
        { required: true, validator: validatePass2, trigger: "blur" },
      ],
    };
    const registerFormRef = ref(null);
    const registerForm = reactive({
      // 表单数据
      username: "", // 用户名
      email: "", //邮箱
      pass: "", // 密码
      vertifyCode: "", //验证码
      checkPass: "", // 检查密码
    });

    //发送验证码
    const sendVertifyBtn = sendVertify;

    //注册
    const router = useRouter();
    const registerBtn = (formName) => {
      const registerData = {
        username: formName.username,
        email: formName.email,
        password: formName.pass,
        vertifyCode: formName.vertifyCode,
      };
      register(registerData)
        .then((data) => {
          console.log(data);
          if (data.code == 0) {
            ElMessage.warning(data.description);
          } else {
            ElMessage.success("恭喜你，注册成功，将自动跳转登录页面!");
            router.push("/login");
          }
        })
        .catch((error) => {
          if (error) {
            console.log("注册请求失败");
          }
        });
    };

    //表单重置
    const resetForm = (formName) => {
      registerFormRef.value.resetFields();
    };

    return {
      registerFormRef,
      registerForm,
      rules,
      sendVertifyBtn,
      registerBtn,
      resetForm,
    };
  },
};
</script>

<style scoped>
/* 注册页面样式 */
.register {
  position: fixed;
  width: 100%;
  height: 100%;
  background-color: #e4e7ed;
}
/* 标题样式 */
.title {
  font-size: large;
  font-weight: bolder;
  text-align: center;
  color: black;
}
/* 注册表单区域样式 */
.main-register {
  position: absolute;
  left: 48%;
  top: 40%;
  width: 350px;
  height: 400px;
  margin: -190px 0 0 -190px;
  padding: 40px;
  border-radius: 5px; /*圆角边框*/
  background: #f2f6fc;
}
/* el-form标签样式 */
.el-form {
  padding-top: 5%;
  padding-right: 10%;
}
/* el-form-item标签样式 */
.el-form-item {
  margin-left: -10%;
}
/* el-row标签样式 */
.el-row {
  height: 100%;
  width: 100%;
}
/* link标签样式 */
.link {
  margin-top: -12%;
  text-align: center;
  margin-left: -5%;
}
/* el-link标签样式 */
.el-link {
  margin-left: 8px;
  line-height: 20px;
  font-size: 8px;
}
</style>
