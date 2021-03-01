<!--
程序名：问卷填写页面
功能：用户打开问卷链接对问卷进行填写
-->
<template>
  <div class="display">
    <div class="content">
      <h3>{{title}}</h3>
      <div class="top" v-if="desc!=''">
        {{desc}}
      </div>
      <el-card class="box-card" v-for="(item,index) in detail.questions" :key="index">
        <div slot: header class="clearfix">

          <div class="questionTitle">
            <!--显示必填标识-->
            <span style="color: #F56C6C;">
              <span v-if="item.required == 1 ? true : false">*</span>
              <span v-else>&nbsp;</span>
            </span>
            {{(index+1)+'.'+item.questionTitle}}
          </div>
        </div>

        <!--单选题展示-->
        <div v-if="item.questionType==1">
            <div class="text item" v-for="(optionItem, index) in item.options" :key="index">
                <el-radio v-model="item.radioValue" :label="optionItem.optionId" style="margin: 5px;">{{ optionItem.content }}</el-radio>
            </div>
        </div>
        

        <!--多选题展示-->
        <el-checkbox-group v-if="item.questionType==2" v-model="item.checkboxValue">
          <div class="text item"  v-for="(optionItem, index) in item.options" :key="index">
            <el-checkbox :label="optionItem.optionId" style="margin: 5px;">{{ optionItem.content }}</el-checkbox>
          </div>
        </el-checkbox-group>

        <!--填空题展示-->
        <el-input
          v-if="item.questionType==3"
          type="textarea"
          :rows="item.row"
          v-model="item.textValue"
          resize="none">
        </el-input>

      </el-card>
       <el-button type="primary" style="margin: 5px;" @click="submitBtn" :loading="submitLoading">{{submitText}}</el-button>

      <div class="bottom">
        <el-link type="info" href="/">调研汪&nbsp;提供技术支持</el-link>
      </div>
    </div>
  </div>
</template>
<script>
import { onMounted, reactive, ref } from "vue";
import { getQuestionnaireById, addAnswer, getQuestionList } from "./api";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
import qs from 'qs'
export default {
  name: "Display",
  setup() {
    const dialogShow = ref(false);
    const dialogTitle = ref("");
    const dialogType = ref(1); //1添加 2修改
    const oldItem = ref(null); //编辑中问题的对象
    const wjId = ref(0);
    const title = ref("");
    const desc = ref("");
    const detail = reactive({questions: []});
    const startTimestamp = ref(0); //填写问卷开始时间戳 毫秒
    const submitLoading = ref(false); //提交按钮 加载中状态
    const submitText = ref("提交"); //提交按钮文字
    const checkboxValue =  ([])
    const route = useRoute();
    const router = useRouter();

    onMounted(() => {
      wjId.value = route.params.id;
      getQuestionnaireById({
        params: {
          "questionnaireId": wjId.value,
        }}).then((res) => {
          let data = res.data;
          // console.log(data);
          if (data.code == 1) {
            title.value = data.data.title;
            desc.value = data.data.description;
             getQuestionList({ params: { questionnaireId: wjId.value} }).then((res) => {
                let data = res.data;
                console.log(data);
                data.data.forEach(question => {
                  if (question.questionType == 2) {
                    question.checkboxValue= []
                  }
                  detail.questions.push(question)
                })
              })
              .catch((error) => {
                if (error) {
                  ElMessage.error("网络错误，请重试！");
                }
              });
          } else {
            ElMessage.error(data.description);
          }
      });
      startTimestamp.value = new Date().getTime(); //时间戳 毫秒
    });

    //提交问卷
    const submitBtn = () => {
      submitLoading.value = true;
      submitText.value = "提交中";
      let useTime = parseInt(
        (new Date().getTime() - startTimestamp.value) / 1000
      ); //填写问卷用时 秒

      for (let index = 0; index < detail.questions.length; index++) {
        let question = detail.questions[index];
        if (question.required  == 1) {
          if (question.radioValue == null || question.checkboxValue == null || question.textValue == "")
            ElMessage.warning("还有必填项目未填写！");
            submitLoading.value = false;
            return false;
        }
      }

      let answerData = {
        "questionnaireId": parseInt(wjId.value),
        "questions": detail.questions,
        "useTime": useTime,
      }
      addAnswer({
        url: "http://localhost:10001/answer",
        method: "POST",
        data: JSON.stringify(answerData),
        dataType: "json",
        headers: {
          "Content-Type": "application/json;charset=UTF-8",
        },
      }).then((res) => {
        let data = res.data;
        // console.log(data);
        if (data.code == 1) {
          //提交成功
          submitLoading.value = false;
          submitText.value = "提交";
          router.push({ path: "/thankyou" }); //跳到欢迎页
        } else {
          submitLoading.value = false;
          submitText.value = "提交";
          ElMessage.error(data.description);
        }
      })
    };

    return {
      dialogShow,
      dialogTitle,
      dialogType,
      oldItem,
      title,
      desc,
      checkboxValue,
      detail,
      startTimestamp,
      submitLoading,
      submitText,
      submitBtn,
    };
  },
};
</script>
<style scoped>
.display {
  text-align: center;
  padding: 20px;
}
.display .top {
  color: #606266;
  padding: 0 10px 10px 10px;
  border-bottom: 3px solid #409eff;
  font-size: 15px;
  line-height: 22px;
  text-align: left;
}
.display .content {
  width: 100%;
  max-width: 800px;
  display: inline-block;
  text-align: center;
}
.display .box-card {
  text-align: left;
  width: 100%;
  margin: 10px 0 10px 0;
}
.display .bottom {
  margin: 20px 10px 20px 10px;
  color: #909399;
}
.display a:link,
a:visited,
a:active {
  color: #909399;
  text-decoration: none;
}
</style>
