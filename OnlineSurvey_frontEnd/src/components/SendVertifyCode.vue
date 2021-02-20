<template>
  <div class="login-email">
    <el-form :model="form" :rules="formRules" ref="formRef">
      <el-form-item prop="email" v-if="isShowemail">
        <el-input
          v-model="form.email"
          class="input-height"
          clearable
          :prefix-icon="hasIcon && icon.email"
          placeholder="请输入邮箱"
          type="text"
        />
      </el-form-item>
      <el-form-item prop="code" class="form-item">
        <el-input
          v-model="form.code"
          :prefix-icon="hasIcon && icon.code"
          type="text"
          clearable
          class="input-height"
          placeholder="请输入验证码"
          max="6"
        />
        <el-button
          @click="sendCodeFunc"
          :loading="buttonLoading"
          :disabled="form.codeTime !== 0 && sendButtonIsDisabled"
        >
          {{ getButtonTxt() }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { ElMessage } from "element-plus";
import { defineComponent, reactive, ref, toRefs, watch } from "vue";
const emailRegular =  /^[0-9a-zA-Z_\\.-]+[@][0-9a-zA-Z_\\.-]+([\\.][a-zA-Z]+){1,2}$/;

export default defineComponent({
  components: {},
  props: {
    //是否显示icon
    hasIcon: {
      type: Boolean,
      default: false,
    },
    //图标
    icon: {
      type: Object,
      default: () => ({
        email: "el-icon-email-outline",
        code: "el-icon-message",
      }),
    },
    tips: {
      type: Object,
      default: () => ({
        // 成功
        success: "验证码发送成功！",
        // 失败
        fail: "验证码发送失败！",
        // 冷却
        coolingTime: "请耐心等待验证码！",
      }),
    },
    //发送验证码期间是否禁用
    sendButtonIsDisabled: {
      type: Boolean,
      default: false,
    },
    //是否显示邮箱
    isShowemail: {
      type: Boolean,
      default: false,
    },
    //发送验证码方法
    sendCode: {
      type: Function,
      default: () => {
        // 默认方法
      },
    },
    //邮箱
    email: {
      type: String,
      default: "",
    },
  },
  emit: ["sendFail"],
  setup(props, { emit }) {
    //表单数据
    const form = reactive({
      email: "",
      code: "",
      isSendCode: false,
      codeTime: 0,
    });
    //表单ref
    const formRef = ref(null);
    // 表单校验
    const formRules = {
      email: [
        {
          required: true,
          message: "请输入邮箱",
        },
        {
          pattern: emailRegular,
          message: "请输入正确的邮箱",
        },
      ],
      code: [
        {
          validator: (rule, value, callback) => {
            rule;
            const { isSendCode } = form;
            if (value === "") {
              callback(new Error("请输入验证码"));
            } else if (!isSendCode) {
              callback(new Error("请先发送验证码"));
            } else {
              callback();
            }
          },
        },
      ],
    };

    //监听
    const { code, email } = toRefs(form);
    watch(
      [code, email],
      (newData) => {
        emit("update:modelValue", {
          code: newData[0],
          email: props.isShowemail ? newData[1] : props.email,
        });
      },
      {
        deep: true,
      }
    );
    //校验是否发送成功
    const formValidateField = (id) => {
      return new Promise((resolve) => {
        formRef.value.validateField(id, (check) => {
          if (check === "") {
            resolve(true);
          } else {
            resolve(false);
          }
        });
      });
    };
    //按钮加载中
    const buttonLoading = ref(false);
    // 发送验证码
    const sendCodeFunc = async () => {
      const { fail, success, coolingTime } = props.tips;
      //如果时间还没到
      if (form.codeTime !== 0) {
        ElMessage.info(coolingTime);
        return false;
      }
      //邮箱
      let emailNumber = "";
      buttonLoading.value = true;
      //显示邮箱
      if (props.isShowemail) {
        const formValuidateValue = await formValidateField("email");
        if (formValuidateValue) {
          emailNumber = form.email;
          // 校验成功
        } else {
          // 校验未成功
          buttonLoading.value = false;
          return false;
        }
      }
      console.log(props.email)
      try {
        //发送验证码
        await props.sendCode(emailNumber || props.email);
        //发送成功
        ElMessage.success(success);
        //发送验证码
        form.isSendCode = true;
        //时间开始
        form.codeTime = 60;
        //定时器
        const time = () =>
          setTimeout(() => {
            form.codeTime -= 1;
            if (form.codeTime !== 0) {
              time();
            }
          }, 1000);
        //执行定时器
        time();
        //发送完毕
        buttonLoading.value = false;
        //刷新code的校验
        if (form.code) {
          formRef.value.validateField("code");
        }
      } catch (error) {
        emit("sendFail");
        ElMessage.error(fail);
        buttonLoading.value = false;
      }
    };

    //获取button的txt
    const getButtonTxt = () => {
      if (form.codeTime) {
        return form.codeTime;
      } else if (buttonLoading.value) {
        return "发送中";
      } else if (form.isSendCode) {
        return "重新发送";
      } else {
        return "发送验证码";
      }
    };

    //校验
    const formValidate = (key) => {
      if (key) {
        return formRef.value.validateField(key);
      } else {
        return formRef.value.validate();
      }
    };
    return {
      form,
      formRules,
      sendCodeFunc,
      formRef,
      buttonLoading,
      getButtonTxt,
      formValidate,
    };
  },
});
</script>

<style>
.login-email {
  width: 100%;
  height: auto;
  flex-shrink: 0;
}
.login-email .form-item .el-form-item {
    margin-bottom: 22px;
}
.login-email .form-item > .el-form-item__content {
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-start;
    
}
.login-email .form-item .el-form-item > .el-button {
     width: 110px;
     margin-left: 10px;
     flex-shrink: 0;
}
</style>