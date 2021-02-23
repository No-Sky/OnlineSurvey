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
      <el-card class="box-card" v-for="(item,index) in detail" :key="index">
        <div slot: header class="clearfix">

          <div class="questionTitle">
            <!--显示必填标识-->
            <span style="color: #F56C6C;">
              <span v-if="item.must">*</span>
              <span v-else>&nbsp;</span>
            </span>
            {{(index+1)+'.'+item.title}}
          </div>
        </div>

        <!--单选题展示-->
        <div v-if="item.type=='radio'">
            <div class="text item" v-for="(optionItem, index) in item.options" :key="index">
                <el-radio v-model="item.radioValue" :label="optionItem.id" style="margin: 5px;">{{ optionItem.title }}</el-radio>
            </div>
        </div>
        

        <!--多选题展示-->
        <el-checkbox-group v-if="item.type=='checkbox'" v-model="item.checkboxValue">
          <div class="text item"  v-for="(optionItem, index) in item.options" :key="index">
            <el-checkbox :label="optionItem.id" style="margin: 5px;">{{ optionItem.title }}</el-checkbox>
          </div>
        </el-checkbox-group>

        <!--填空题展示-->
        <el-input
          v-if="item.type=='text'"
          type="textarea"
          :rows="item.row"
          v-model="item.textValue"
          resize="none">
        </el-input>

      </el-card>
       <el-button type="primary" style="margin: 5px;" @click="submitBtn" :loading="submitLoading">{{submitText}}</el-button>

      <div class="bottom">
        <el-link type="info" href="/">问卷喵&nbsp;提供技术支持</el-link>
      </div>
    </div>
  </div>
</template>
<script>
import { onMounted, reactive, ref } from "vue";
import { getQuestionnaireById, addAnswer } from "./api";
import { ElMessage } from "element-plus";
import { useRoute, useRouter } from "vue-router";
export default {
  name: "Display",
  setup() {
    const dialogShow = ref(false);
    const dialogTitle = ref("");
    const dialogType = ref(1); //1添加 2修改
    const oldItem = ref(null); //编辑中问题的对象
    const willAddQuestion = ref({
      type: "",
      title: "",
      options: [""],
      text: "",
      row: 1,
    });
    const allType = reactive([
      {
        value: "radio",
        label: "单选题",
      },
      {
        value: "checkbox",
        label: "多选题",
      },
      {
        value: "text",
        label: "填空题",
      },
    ]);
    const wjId = ref(0);
    const title = ref("");
    const desc = ref("");
    let detail = ref([]);
    const startTimestamp = ref(0); //填写问卷开始时间戳 毫秒
    const submitLoading = ref(false); //提交按钮 加载中状态
    const submitText = ref("提交"); //提交按钮文字
    const route = useRoute();
    const router = useRouter();

    onMounted(() => {
      wjId.value = route.params.id;
      console.log(wjId.value)
      getQuestionnaireById({
        opera_type: "get_info",
        wjId: wjId,
        username: "test", //增加登录验证后不需传递（后端从session获取）
      }).then((data) => {
        console.log(data);
        if (data.code == 0) {
          title.value = data.title;
          desc.value = data.description;
          detail.value = data.detail;
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
        (new Date().getTime() - this.startTimestamp) / 1000
      ); //填写问卷用时 秒
      addAnswer({
        opera_type: "submit_wj",
        wjId: wjId.value,
        useTime: useTime,
        detail: detail,
      }).then((data) => {
        console.log(data);
        if (data.code == 0) {
          //提交成功
          submitLoading.value = false;
          submitText.value = "提交";
          router.push({ path: "/thankyou" }); //跳到欢迎页
        } else {
          submitLoading.value = false;
          submitText.value = "提交";
          ElMessage.error(data.description);
        }
      });
    };

    return {
      dialogShow,
      dialogTitle,
      dialogType,
      oldItem,
      willAddQuestion,
      allType,
      title,
      desc,
      detail,
      startTimestamp,
      submitLoading,
      submitText,
      submitBtn,
    };
  },
  // data(){
  //   return{
  //     dialogShow:false,
  //     dialogTitle:'',
  //     dialogType:1,//1添加 2修改
  //     oldItem:null,//编辑中问题的对象
  //     willAddQuestion:{
  //       type:'',
  //       title:'',
  //       options:[''],
  //       text:'',
  //       row:1,
  //     },
  //     allType:[
  //       {
  //         value:'radio',
  //         label:'单选题',
  //       },
  //       {
  //         value:'checkbox',
  //         label:'多选题',
  //       },
  //       {
  //         value:'text',
  //         label:'填空题',
  //       },
  //     ],
  //     title:'',
  //     desc:'',
  //     detail:[],
  //     startTimestamp:0,//填写问卷开始时间戳 毫秒
  //     submitLoading:false,//提交按钮 加载中状态
  //     submitText:'提交',//提交按钮文字
  //   }
  // },
  // mounted(){
  //   var wjId=this.$route.params.id;
  //   answerOpera({
  //     opera_type:'get_info',
  //     wjId:wjId,
  //     username:'test'//增加登录验证后不需传递（后端从session获取）
  //   })
  //     .then(data=>{
  //       console.log(data);
  //       if(data.code==0){
  //         this.title=data.title;
  //         this.desc=data.desc;
  //         this.detail=data.detail;
  //         document.title=data.title;
  //       }
  //       else{
  //         this.$message({
  //           type: 'error',
  //           message: data.msg
  //         });
  //       }
  //     })
  //   this.startTimestamp=new Date().getTime();//时间戳 毫秒
  // },
  // methods:{
  //
  // }
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
