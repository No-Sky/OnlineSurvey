<!--
程序名：后台管理主页面
-->
<template>
  <div id="admin">
    <el-tabs :tab-position="tabPosition">
      <el-tab-pane label="用户管理">
        <user-admin />
      </el-tab-pane>
      <el-tab-pane label="问卷管理">
        <questionnaire-admin />
      </el-tab-pane>
      <!-- <el-tab-pane label="标签管理">
      </el-tab-pane> -->
    </el-tabs>
  </div>
</template>

<script>
import { ref } from "vue";
import UserAdmin from './UserAdmin.vue'
import QuestionnaireAdmin from './QuestionnaireAdmin.vue'
export default{
  components: {
    UserAdmin,
    QuestionnaireAdmin
  },
  setup () {
    const tabPosition = ref("left");
    return {
      tabPosition,
    }
  }
};
</script>

<style>
#admin {
  margin: 3%;
}
#admin .el-tabs__nav-wrap {
  display: inline-block;
}
</style>






