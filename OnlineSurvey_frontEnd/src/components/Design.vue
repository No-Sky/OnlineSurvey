<!--
程序名：问题设计页面
功能：对问卷中问题的添加、编辑、删除
-->
<template>
  <div class="Design" v-loading="loading" element-loading-text="加载中...">
    <h3>{{wjTitle}}</h3>
      <div class="top" v-if="wjDesc!=''">
        {{wjDesc}}
      </div>
    <el-card class="box-card" v-for="(item,index) in detail" :key="index" style="margin: 10px;">
        <div slo: header class="clearfix">
          <div class="questionTitle">
            <!--显示必填标识-->
            <span style="color: #F56C6C;">
              <span v-if="item.must">*</span>
              <span v-else>&nbsp;</span>
            </span>
            <span style="color: black;margin-right: 3px;">{{(index+1)+'.'}}</span>
            {{item.title}}
          </div>
          <div style="float: right;">
            <el-button style="padding: 2px" type="text" @click="editorQuestionBtn(item)">编辑</el-button>
            <el-button style="padding: 2px;color: #F56C6C" type="text" @click="deleteQuestionBtn(index)">删除</el-button>
          </div>
        </div>

        <!--单选题展示-->
        <div  v-if="item.type=='radio'">
            <div class="text item" v-for="(option,index) in item.options" :key="index">
          <el-radio v-model="item.radioValue" :label="index" style="margin: 5px;">{{ option.title }}</el-radio>
        </div>
        </div>

        <!--多选题展示-->
        <el-checkbox-group v-if="item.type=='checkbox'" v-model="item.checkboxValue">
          <div class="text item"  v-for="(option,index) in item.options" :key="index">
            <el-checkbox :label="index" style="margin: 5px;">{{ option.title }}</el-checkbox>
          </div>
        </el-checkbox-group>

        <!--填空题展示-->
        <el-input
          v-if="item.type=='text'"
          type="textarea"
          :rows="item.row"
          resize="none"
          v-model="item.textValue">
        </el-input>

      </el-card>

      <el-button  icon="el-icon-circle-plus" @click="addQuestionBtn" style="margin-top: 10px;">添加题目</el-button>

<br><br><br><br><br>

    <!--添加题目弹窗-->
    <el-dialog :title="dialogTitle" v-model=dialogShow :close-on-click-modal="false">
      <el-form ref="form" :model="willAddQuestion" label-width="80px">
        <el-form-item label="题目类型" style="width: 100%;">
          <el-select v-model="willAddQuestion.type" placeholder="请选择题目类型" @change="typeChange">
          <el-option
            v-for="item in allType"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        </el-form-item>
        <el-form-item label="是否必填" style="width: 100%;">
          <el-checkbox v-model="willAddQuestion.must">必填</el-checkbox>
        </el-form-item>
        <el-form-item label="题目标题" style="width: 100%;">
          <el-input v-model="willAddQuestion.title" placeholder="请输入标题" ></el-input>
        </el-form-item>

        <template v-if="willAddQuestion.type=='radio'||willAddQuestion.type=='checkbox'">
          <el-form-item :label="'选项'+(index+1)" v-for="(item,index) in willAddQuestion.options" :key="index" >
            <el-row>
              <el-col :span="16">
                <el-input  v-model="item.title" placeholder="请输入选项名" style="width: 90%;"></el-input>
              </el-col>
            <el-col :span="8">
              <el-button type="danger" plain class="" @click="deleteOptionBtn(index)" >删除选项</el-button>
            </el-col>
            </el-row>

          </el-form-item>
          <el-button type="primary" plain class="addOptionButton" @click="addOptionBtn">新增选项</el-button>
        </template>
        <template v-if="willAddQuestion.type=='text'">
          <el-form-item label="填空">
            <el-input type="textarea"
  :rows="willAddQuestion.row" style="width: 80%" resize="none"></el-input>
          </el-form-item>
          <el-form-item label="行数">
            <el-input-number v-model="willAddQuestion.row" :min="1" :max="10" label="描述文字"></el-input-number>
          </el-form-item>

        </template>
      </el-form>
      <br>
      <div style="width: 100%;text-align: right">
        <el-button style="margin-left: 10px;" @click="dialogShow=false">取消</el-button>
        <el-button type="primary" style="margin-left: 10px;" @click="checkAddQuestion">完成</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { reactive, ref } from "vue"
import { getQuestionList, addQuestion, deleteQuestion } from "./api"
import { ElMessage, ElMessageBox } from "element-plus"
import qs from 'qs'
export default {
  props: {
    questionnaire: {
      type: Object
    } 
  },
  setup(props, proxy) {
    const loading = ref(false);
    const dialogShow = ref(false);
    const dialogTitle = ref("");
    let detail = ref([]);
    const wjId = ref(0);
    const wjTitle = ref("");
    const wjDesc = ref("")
    const willAddQuestion = ref({
      id: 0,
      type: "",
      title: "",
      options: [
        {
          title: "", //选项标题
          id: 0, //选项id
        },
      ],
      row: 1,
      must: false, //是否必填
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

    //获取问题列表(问卷内容)
    const local_getQuestionList = () => {
      detail = [];
      loading.value = true;
      getQuestionList({
        params: {"questionaireId": wjId}
      }).then((res) => {
        let data = res.data;
        console.log(data);
        detail.value = data.data;
        loading.value = false;
      }).catch(error => {
        if(error) {
          ElMessage.error("网络错误，请重试！");
        }
      });
    };

    //初始化问卷所有问题
    const init = () => {
      const { questionnaireId, title, description } = props.questionnaire;
      wjId.value = questionnaireId;
      wjTitle.value = title;
      wjDesc.value = description;
      local_getQuestionList();
    };

    //点击添加问题按钮
    const addQuestionBtn = () => {
      if (wjId.value == 0 || wjId.value == null) {
        ElMessage.error("请先创建问卷！");
        return;
      }
      dialogTitle.value = "添加题目";
      willAddQuestion.value = {
        id: 0,
        type: "",
        title: "",
        options: [
          {
            title: "", //选项标题
            id: 0, //选项id
          },
        ],
        row: 1,
        must: false, //是否必填
      };
      dialogShow.value = true;
    };

    //删除问题
    const deleteQuestionBtn = (index) => {
      ElMessageBox.confirm("确定删除此题目?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        deleteQuestion(qs.stringify({ questionId: detail.value[index].id, })).then((res) => {
          let data = res.data;
          console.log(data);
          if (data.code == 1) {
            detail.value.splice(index, 1);
            ElMessage.success("删除成功");
          } else {
            ElMessage.error(data.description);
          }
        });
      });
    };

    //确认添加/保存题目
    const checkAddQuestion = () => {
      //添加保存问题
      let newItem = {}; //新添加的问题对象
      newItem = {
        type: willAddQuestion.value.type,
        title: willAddQuestion.value.title,
        options: willAddQuestion.value.options,
        row: willAddQuestion.value.row,
        must: willAddQuestion.value.must,
      };
      newItem.radioValue = -1;
      newItem.checkboxValue = [];
      newItem.textValue = "";
      let questionData = {
        questionId: willAddQuestion.value.id,
        questionaireId: wjId,
        questionTitle: willAddQuestion.value.title,
        questionType: willAddQuestion.value.type,
        options: willAddQuestion.value.options,
        row: willAddQuestion.value.row,
        required: willAddQuestion.value.must,
      }
      addQuestion({
        wjId: wjId.value,
        questionId: willAddQuestion.value.id,
        title: willAddQuestion.value.title,
        type: willAddQuestion.value.type,
        options: willAddQuestion.value.options,
        row: willAddQuestion.value.row,
        must: willAddQuestion.value.must,
      }).then((res) => {
        let data = res.data;
        console.log(data);
        newItem.id = data.id;
        if (data.code == 1) {
          dialogShow.value = false;
          ElMessage.success("保存成功");
          local_getQuestionList();
        } else {
          dialogShow.value = false;
          ElMessage.error(data.description);
        }
        willAddQuestion.value = {
          id: 0,
          type: "",
          title: "",
          options: [""],
          row: 1,
          must: false,
        };
      });
    };

    //点击编辑问题按钮
    const editorQuestionBtn = (item) => {
      willAddQuestion.value.title = item.title;
      willAddQuestion.value.type = item.type;
      willAddQuestion.value.options = JSON.parse(JSON.stringify(item.options));
      willAddQuestion.value.text = item.text;
      willAddQuestion.value.row = item.row;
      willAddQuestion.value.must = item.must;
      willAddQuestion.value.id = item.id;
      dialogTitle.value = "编辑问题";
      dialogShow.value = true;
    };

    //添加选项
    const addOptionBtn = () => {
      willAddQuestion.value.options.push({
        title: "",
        id: 0,
      });
    };

    //删除选项
    const deleteOptionBtn = (index) => {
      willAddQuestion.value.options.splice(index, 1);
    };

    //切换问题类型
    const typeChange = (value) => {
      // console.log(value);
      willAddQuestion.value.type = value;
      willAddQuestion.value.text = "";
      willAddQuestion.value.row = 1;
    };

    return {
      loading,
      dialogShow,
      dialogTitle,
      detail,
      wjId,
      wjTitle,
      wjDesc,
      willAddQuestion,
      allType,
      init,
      addQuestionBtn,
      deleteQuestionBtn,
      checkAddQuestion,
      editorQuestionBtn,
      addOptionBtn,
      deleteOptionBtn,
      typeChange,
    };
  },
};
</script>
<style scoped>
.Design {
}
.Design .dialog {
  text-align: left;
}
.Design .questionTitle {
  display: inline-block;
  width: 80%;
  font-size: 16px;
  color: #303133;
}
.Design .addOptionButton {
  display: inline-block;
  margin-left: 80px;
}
.box-card {
  width: 100%;
  text-align: left;
}
.Design .top {
  color: #606266;
  margin-left: 20px;
  padding: 0 10px 10px 10px;
  border-bottom: 3px solid #409eff;
  font-size: 15px;
  line-height: 22px;
  text-align: left;
}
</style>
