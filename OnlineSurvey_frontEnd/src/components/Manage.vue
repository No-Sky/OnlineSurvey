<!--
程序名：问卷设计主页面
功能：对问卷进行设计
-->
<template>
  <div class="home">
    <el-row>
      <el-col :span="6">
        <!--操作栏-->
        <div class="opera">
          <el-tooltip class="item" effect="dark" content="创建问卷" placement="bottom">
            <el-button icon="el-icon-plus" type="text" class="rightButton" @click="addQuestionnaireBtn"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="编辑问卷" placement="bottom">
            <el-button  icon="el-icon-edit" type="text" class="rightButton" @click="editWj" :disabled="nowSelect.attrs == undefined "></el-button>
          </el-tooltip>
          <el-tooltip class="item" v-if="nowSelect.attrs != undefined" effect="dark" :content="nowSelect.attrs.statusType==0?'发布问卷':'暂停问卷'" placement="bottom" >
            <el-button :icon="nowSelect.attrs.statusType==0?'el-icon-video-play':'el-icon-video-pause'"  type="text" class="rightButton" @click="pushWj" :disabled="nowSelect.attrs == undefined"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="预览问卷" placement="bottom">
            <el-button icon="el-icon-view" type="text" class="rightButton" @click="previewWj" :disabled="nowSelect.attrs == undefined"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="删除问卷" placement="bottom">
            <el-button icon="el-icon-delete" type="text" class="rightButton" @click="deleteWj" :disabled="nowSelect.attrs == undefined"></el-button>
          </el-tooltip>
          <el-tooltip class="item" effect="dark" content="分享问卷" placement="bottom">
            <el-button icon="el-icon-share" type="text" class="rightButton" @click="shareWj" :disabled="nowSelect.attrs == undefined"></el-button>
          </el-tooltip>
          <!--<el-tooltip class="item" effect="dark" content="添加模板库" placement="bottom">-->
            <!--<el-button icon="el-icon-upload" type="text"class="rightButton" @click="addTemp"></el-button>-->
          <!--</el-tooltip>-->
        </div>

        <!--左侧导航栏-->
        <el-menu :default-active="defaultActive.toString()" v-loading="loading" class="menu">
          <!--问卷列表-->
          <div style="width:100%;text-align: center;font-size: 15px;line-height: 20px;margin-top: 20px;color: #303133" v-if="nowSelect.attrs == undefined">
            点击上方&nbsp;+&nbsp;创建第一个问卷
          </div>
          <el-menu-item v-for="(item,index) in wjList" :index="(index+1).toString()" :key="index" @click="wjClick(item.questionnaireId,index)">
            <i class="el-icon-tickets"></i>
            <span slot: title style="display: inline-block">
              {{item.title}}
              <span style="color: #F56C6C;font-size: 13px;" v-if="item.statusType==0">
                <i class="el-icon-link" style="margin:0;font-size: 13px;color: #F56C6C;width:10px;"></i>
                未发布
              </span>
              <span style="color: #67C23A;font-size: 13px;" v-if="item.statusType==1">
                <i class="el-icon-link" style="margin:0;font-size: 13px;color: #67C23A;width:10px;"></i>
                已发布
              </span>
            </span>
          </el-menu-item>
        </el-menu>
      </el-col>

      <el-col :span="18">
        <!--标签页-->
         <el-tabs type="border-card" v-model="activeName">
            <el-tab-pane label="问卷设计" name="wjsj">
              <!--内容区域-->
              <div class="content">
                <div v-show="nowSelect.attrs == undefined">请先选择问卷</div>
                <design ref="designRef" :questionnaire="nowSelect.attrs" :key="nowSelect.attrs.questionnaireId" v-if="nowSelect.attrs != undefined&&nowSelect.attrs.questionnaireId!=null"></design>
              </div>
            </el-tab-pane>
            <el-tab-pane label="回答统计" name="hdtj">
              <div class="content" ref="pdf">
                <div v-show="nowSelect.attrs == undefined">请先选择问卷</div>
                <data-show ref="dataShowRef" :questionnaire="nowSelect.attrs" :key="nowSelect.attrs.questionnaireId" v-if="nowSelect.attrs != undefined&&nowSelect.attrs.questionnaireId!=null"></data-show>
              </div>
            </el-tab-pane>
          </el-tabs>
      </el-col>
    </el-row>

    <!--添加和编辑问卷弹窗-->
    <el-dialog :title="dialogTitle" v-model="dialogShow" :close-on-click-modal="false" >
      <el-form ref="form" :model="willAddWj" label-width="80px">
        <el-form-item label="问卷标题" style="width: 100%;" required>
          <el-input v-model="willAddWj.title" placeholder="请输入问卷标题" ></el-input>
        </el-form-item>
        <el-form-item label="问卷描述" style="width: 100%;">
          <el-input v-model="willAddWj.description" type="textarea" placeholder="请输入问卷描述" rows="5"></el-input>
        </el-form-item>
        <el-form-item label="截止时间" style="width: 100%;">
          <el-input v-model="willAddWj.stopTime" type="datetime-local"></el-input>
          <span>默认不限制截止时间</span>
        </el-form-item>
        <el-form-item label="分发数量" style="width: 100%;">
          <el-input v-model="willAddWj.distribution" type="number" min="0"></el-input>
          <span>默认为0时不限制分发数量</span>
        </el-form-item>
        <el-form-item label="是否公开" >
          <el-switch v-model="willAddWj.isPublic"></el-switch>
        </el-form-item>
        <el-form-item label="标签" style="width: 100%;">
          <el-tag
            :key="tag.name"
            v-for="tag in willAddWj.tags"
            closable
            :disable-transitions="false"
            @close="handleClose(tag.qtId)">
            {{tag.name}}
          </el-tag>
          <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            @keyup.enter="handleInputConfirm"
            @blur="handleInputCancel"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
        </el-form-item>
      </el-form>
      <div style="width: 100%;text-align: right">
        <!-- <el-button style="margin-left: 10px;" @click="openTemp">从模板库创建</el-button> -->
        <el-button style="margin-left: 10px;" @click="dialogShow=false">取消</el-button>
        <el-button type="primary" style="margin-left: 10px;" @click="addWj">确定</el-button>
      </div>
    </el-dialog>



    <!--模板库弹窗-->
    <el-dialog title="模板库" v-model="tempDialog" :close-on-click-modal="false">
      <el-input placeholder="请输入关键词搜索" prefix-icon="el-icon-search" v-model="tempSearchText"></el-input>
     <el-table :data="tempData" style="width: 100%;" v-loading="tempLoading" element-loading-text="加载中...">
      <el-table-column prop="tempname" label="模板名" width="250"></el-table-column>
      <el-table-column prop="username" label="创建者"></el-table-column>
       <el-table-column label="预览">
       <el-link slot: scope type="primary" @click="lookTempWj(scope.row)">预览</el-link>
     </el-table-column>
     <el-table-column label="操作">
        <el-link slot: scope type="primary" @click="useTemp(scope.row)">使用此模板</el-link>
     </el-table-column>
    </el-table>
      <el-pagination layout="prev, pager, next" :total="tempDataCount" @current-change="changeTempPage" :page-size	="5"></el-pagination>
    </el-dialog>


    <!--分享问卷弹窗-->
    <el-dialog title="分享问卷" v-model="shareDialogShow" :close-on-click-modal="true" @opened="makeQrcode">
      <el-form ref="form" :model="shareInfo" label-width="80px">
        <el-form-item label="问卷链接" style="width: 100%;">
          <el-row>
            <el-col :span="16">
              <el-input v-model="shareInfo.url" readonly></el-input>
            </el-col>
            <el-col :span="8">
              <el-button style="margin-left: 5px;" v-clipboard:copy="shareInfo.url" v-clipboard:success="copySuccess" v-clipboard:error="copyError">复制</el-button>
              <el-button style="margin-left: 5px;" @click="openShareUrl">打开</el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="二维码" style="width: 100%;">
          <canvas id="canvas" style="width: 150px;height: 150px;"></canvas>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>
<script>
import {
  getQuestionnaireListbyUser,
  addQuestionnaire,
  editQuestionnaire,
  deleteQuestionnaire,
  getTempPage,
  deleteQuestionnaireTag,
} from "./api";
import Design from "./Design.vue";
import DataShow from "./DataShow.vue";
import { onMounted, reactive, ref, watch, nextTick } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import qs from "qs";
import QRCode from "qrcode";
export default {
  components: {
    Design,
    // QRCode,
    DataShow,
  },
  setup() {
    const designRef = ref();
    const dataShowRef = ref();
    const defaultActive = ref(1); //当前激活菜单
    const activeName = ref("wjsj"); //标签页当前选择项
    const wjList = ref([]); //问卷列表
    const loading = ref(false); //是否显示加载中
    const dialogShow = ref(false); //添加问卷弹窗是否显示
    const dialogTitle = ref("添加问卷");
    const shareDialogShow = ref(false); //分享问卷弹窗是否显示
    const tempDialog = ref(false); //模板库弹窗是否显示
    const tempData = reactive([]);
    const tempDataCount = ref(0);
    const tempLoading = ref(false);
    const tempSearchTest = ref("");
    const willAddWj = ref({
      userId: 0,
      questionnaireId: 0,
      title: "",
      description: "",
      statusType: 0,
      createTime: null,
      stopTime: null,
      distribution: 0,
      isPublic: false,
      tags: [],
    });
    const shareInfo = reactive({
      url: "",
    });
    const nowSelect = reactive({ attrs: {} });
    watch(nowSelect, () => {
      // console.log("nowSelect update");
      let now = wjList.value[defaultActive.value - 1];
      if (wjList.value.length == 0) {
        return {
          attrs: {
            userId: 0,
            questionnaireId: 0,
            title: "",
            description: "",
            statusType: 0,
            createTime: null,
            stopTime: null,
            distribution: 0,
            isPublic: false,
            tags: [],
          },
        };
      }
      return {
        attrs: {
          userId: now.userId,
          questionnaireId: now.questionnaireId,
          title: now.title,
          description: now.description,
          statusType: now.statusType,
          createTime: now.createTime,
          stopTime: now.stopTime,
          distribution: now.distribution,
          isPublic: now.isPublic,
          tags: now.tags,
        },
      };
    });

    const inputVisible = ref(false);
    const inputValue = ref("");
    const saveTagInput = ref(null);

    const handleClose = (qtId) => {
      willAddWj.value.tags.splice(willAddWj.value.tags.indexOf(qtId), 1);
      let deleteData = {
        _method: "DELETE",
        params: {
          qtId: qtId,
        },
      };
      deleteQuestionnaireTag(deleteData).then((res) => {
        let data = res.data;
        if (data.code == 1) {
          ElMessage.success("删除标签成功");
        } else {
          ElMessage.success("删除标签失败");
        }
      });
    };

    const showInput = () => {
      inputVisible.value = true;
      nextTick(() => {
        if (saveTagInput.value) {
          saveTagInput.value.focus();
        }
      });
    };

    const handleInputCancel = () => {
      inputVisible.value = false;
      inputValue.value = "";
    };

    const handleInputConfirm = () => {
      let inputValue_ = inputValue.value;
      if (!inputValue_) {
        inputVisible.value = false;
        inputValue.value = "";
        return;
      }
      let tag = { name: inputValue_ };
      willAddWj.value.tags.push(tag);
      inputVisible.value = false;
      inputValue.value = "";
    };

    const user_session = JSON.parse(sessionStorage.getItem("User_Data"));

    const lookDetail = () => {
      //初始化问卷内容
      if (
        nowSelect.attrs.questionnaireId == 0 ||
        nowSelect.attrs.questionnaireId == null
      )
        return;
      else {
        designRef.value.init(
          nowSelect.attrs.questionnaireId,
          nowSelect.attrs.title,
          nowSelect.attrs.description
        );
        // dataShowRef.value.dataAnalysis(nowSelect.attrs.questionnaireId);
      }
    };

    const getWjList = () => {
      loading.value = true;
      getQuestionnaireListbyUser({
        params: {
          userId: user_session.userId,
        },
      }).then((res) => {
        let data = res.data;
        console.log(data);
        wjList.value = data.data;
        nowSelect.attrs = wjList.value[0];
        loading.value = false;
        //获取当前选中问卷题目
        // lookDetail();
      });
    };

    //展示添加问卷弹窗
    const addQuestionnaireBtn = () => {
      dialogShow.value = true;
      willAddWj.value.userId = user_session.userId;
      willAddWj.value.questionnaireId = 0;
      willAddWj.value.title = "";
      willAddWj.value.description =
        "感谢您能抽时间参与本次问卷，您的意见和建议就是我们前行的动力！";
      willAddWj.value.statusType = 0;
      willAddWj.value.createTime = null;
      willAddWj.value.stopTime = null;
      willAddWj.value.distribution = 0;
      willAddWj.value.isPublic = false;
      willAddWj.value.tags = [];
    };

    const addOrUpdateQuestionnaire = () => {
      if (willAddWj.value.title == "") {
        ElMessage.error("标题不能为空");
        return;
      }
      let questionnaireData = {
        userId: willAddWj.value.userId,
        questionnaireId: willAddWj.value.questionnaireId,
        title: willAddWj.value.title,
        description: willAddWj.value.description,
        statusType: willAddWj.value.statusType,
        createTime: Date.now,
        stopTime: willAddWj.value.stopTime,
        distribution: willAddWj.value.distribution,
        isPublic: willAddWj.value.isPublic,
        tags: willAddWj.value.tags,
      };
      addQuestionnaire({
        url: "http://localhost:10001/questionnaire",
        method: "POST",
        data: JSON.stringify(questionnaireData),
        dataType: "json",
        headers: {
          "Content-Type": "application/json;charset=UTF-8",
        },
      })
        .then((res) => {
          let data = res.data;
          // console.log(data);
          if (data.code == 1) {
            ElMessage.success("保存成功");
            getWjList();
          } else {
            ElMessage.error(data.description);
          }
        })
        .catch((error) => {
          console.log(error);
        });
      dialogShow.value = false;
    };

    //添加问卷
    const addWj = () => {
      if (willAddWj.value.questionnaireId != 0) {
        addOrUpdateQuestionnaire();
      } else {
        ElMessageBox.confirm("创建问卷将扣除2积分，是否继续", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          addOrUpdateQuestionnaire();
        });
      }
    };

    //发布问卷
    const pushWj = () => {
      let status = 1;
      if (nowSelect.attrs.statusType == 1) status = 0;
      let pushData = {
        _method: "PUT",
        questionnaireId: nowSelect.attrs.questionnaireId,
        statusType: status,
      };
      editQuestionnaire(qs.stringify(pushData)).then((res) => {
        let data = res.data;
        console.log(data);
        if (data.code == 1) {
          ElMessage.success(status == 1 ? "问卷发布成功！" : "问卷暂停成功！");
          getWjList();
        } else {
          ElMessage.error(data.description);
        }
      });
    };

    //分享问卷
    const shareWj = () => {
      if (nowSelect.attrs.statusType == 0) {
        //问卷未发布
        ElMessage.error("请先发布问卷能分享！");
        return;
      }
      shareInfo.url =
        window.location.origin + "/display/" + nowSelect.attrs.questionnaireId; //问卷链接
      shareDialogShow.value = true;
    };

    //复制分享链接成功
    const copySuccess = (e) => {
      console.log(e);
      ElMessage.success("复制分享链接成功");
    };
    //复制分享链接失败
    const copyError = (e) => {
      console.log(e);
      ElMessage.error("复制失败");
    };

    //打开分享链接
    const openShareUrl = () => {
      window.open(shareInfo.url);
    };

    const deepClone = (obj) => {
      let _obj = JSON.stringify(obj);
      let objClone = JSON.parse(_obj);
      return objClone;
    };

    // 编辑问卷
    const editWj = () => {
      dialogShow.value = true;
      dialogTitle.value = "编辑问卷";
      willAddWj.value = deepClone(nowSelect.attrs);
    };

    //删除问卷
    const deleteWj = () => {
      ElMessageBox.confirm(
        "确定删除" + nowSelect.attrs.title + "? 删除后不可恢复！",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        loading.value = true;
        let deleteData = {
          _method: "DELETE",
          params: { questionnaireId: nowSelect.attrs.questionnaireId },
        };
        deleteQuestionnaire(deleteData)
          .then((res) => {
            let data = res.data;
            console.log(data);
            if (data.code == 1) {
              ElMessage.success("删除成功");
              loading.value = false;
              getWjList();
              defaultActive.value = 1;
            } else {
              ElMessage.error(data.description);
            }
          })
          .catch((error) => {
            if (error) {
              ElMessage.error("系统错误，请重试！");
            }
          });
      });
    };

    //预览问卷
    const previewWj = () => {
      let url =
        window.location.origin + "/display/" + nowSelect.attrs.questionnaireId; //问卷链接
      window.open(url);
    };

    //生成二维码
    const makeQrcode = () => {
      let canvas = document.getElementById("canvas");
      QRCode.toCanvas(canvas, shareInfo.url);
    };

    //问卷点击
    const wjClick = (id, index) => {
      // defaultActive.value = (index + 1).toString();
      nowSelect.attrs = wjList.value[index];
      lookDetail();
    };

    //改变模板库页码
    const changeTempPage = (page) => {
      this.tempLoading = true;
      getTempPage({
        opera_type: "get_temp_wj_list",
        page: page,
      }).then((res) => {
        let data = res.data;
        console.log(data);
        tempDataCount.value = data.count;
        tempData.value = data.detail;
        tempLoading.value = false;
      });
    };

    //打开问卷库
    const openTemp = () => {
      tempDialog.value = true;
      changeTempPage(1);
      //   this.getTempWjList();
    };

    onMounted(() => {
      getWjList();
    });

    return {
      designRef,
      dataShowRef,
      defaultActive,
      activeName,
      wjList,
      loading,
      dialogShow,
      dialogTitle,
      shareDialogShow,
      shareInfo,
      tempDialog,
      tempData,
      tempDataCount,
      tempLoading,
      tempSearchTest,
      willAddWj,
      nowSelect,
      addQuestionnaireBtn,
      lookDetail,
      getWjList,
      addWj,
      wjClick,
      pushWj,
      shareWj,
      copySuccess,
      copyError,
      openShareUrl,
      editWj,
      previewWj,
      deleteWj,
      makeQrcode,
      openTemp,
      inputVisible,
      inputValue,
      handleClose,
      handleInputConfirm,
      handleInputCancel,
      showInput,
      saveTagInput,
    };
  },

  //     mounted(){
  //       this.logincheck();

  //     },

  //     methods:{
  //       addTemp(){
  //         designOpera({
  //           opera_type:'add_temp',
  //           wjId:107,
  //         })
  //           .then(data=>{
  //             console.log(data);
  //           })
  //       },
  //       //使用问卷
  //       useTemp(item){
  //         this.tempLoading=true;
  //         designOpera({
  //           opera_type:'use_temp',
  //           wjId:item.tempid,
  //         })
  //           .then(data=>{
  //             console.log(data);
  //             this.tempLoading=false;
  //             this.$message({
  //               type: 'success',
  //               message: '使用模板成功！',
  //               showClose: true
  //             });
  //             this.tempDialog=false;
  //             this.dialogShow=false;
  //             this.getWjList();

  //           })
  //       },
  //       /
  //
  //       //预览模板问卷
  //       lookTempWj(item){
  //         let url=window.location.origin+"/tempdisplay/"+item.tempid;//问卷链接
  //         console.log(url);
  //         window.open(url);
  //       },
  //       //检查登录是否过期
  //       logincheck(){
  //           designOpera({
  //           opera_type:'logincheck',
  //         })
  //         .then(data=>{
  //           console.log(data);
  //           if(data.code==404){//如果返回的错误是404，跳转到登录页面
  //             this.$message({
  //               type: 'error',
  //               message: '您还未登录，请登录',
  //               showClose: true
  //             });
  //             this.$router.push({path:'/login'})
  //           }
  //           else{
  //             this.getWjList();
  //           }
  //         })
  //       },
  //
  //     }
};
</script>
<style scoped>
.home {
  position: absolute;
  width: 100%;
  height: calc(100vh - 100px);
  text-align: left;
}
.home .badgeItem {
  margin-top: 40px;
}
.content {
  padding: 20px;
  padding-right: 50px;
  height: calc(100vh - 175px);
  text-align: center;
  overflow-y: scroll;
  overflow-x: hidden;
}
.menu {
  background-color: white;
  height: calc(100vh - 100px);
  overflow-x: scroll;
  overflow-y: scroll;
  left: 0;
}
.home .opera {
  position: relative;
  background-color: #fafafa;
  text-align: center;
  height: 40px;
}
.home .rightButton {
  font-size: 16px;
}
.home .addWjDiv {
  height: 50px;
  line-height: 50px;
  text-align: center;
  border-bottom: 1px solid #eee;
}
.el-form-item {
  margin-bottom: 2px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  /* vertical-align: bottom; */
}
</style>
