<template>
  <div id="profile-main">
    <el-card id="main-left" shadow="always">
      <el-avatar :size="100" :src="userInfo.attrs.avatar"></el-avatar>
      <div id="username">
        <span>{{ userInfo.attrs.username }}</span>
      </div>
      <div class="tab">
        <div
          @click="changeView('base-info')"
          :class="changeId == 0 ? 'item is-active' : 'item'"
        >
          <span>基本信息</span>
        </div>
        <div
          @click="changeView('tags-info')"
          :class="changeId == 1 ? 'item is-active' : 'item'"
        >
          <span>标签信息</span>
        </div>
        <div
          @click="changeView('score-info')"
          :class="changeId == 2 ? 'item is-active' : 'item'"
        >
          <span>积分信息</span>
        </div>
        <div
          @click="changeView('questionnaire-info')"
          :class="changeId == 3 ? 'item is-active' : 'item'"
        >
          <span>我参与的问卷</span>
        </div>
      </div>
    </el-card>
    <el-card id="main-right" shadow="always">
      <div id="base-info">
        <div class="main-right-header">基本信息</div>
        <div class="main-right-content">
          <div class="base-info-item">
            <span class="base-info-lable">头像</span>
            <div>
              <el-avatar :size="80" :src="baseInfo.attrs.avatar"></el-avatar>
              <div id="avatars">
                <el-avatar
                  v-for="(item, index) in avatars.attrs"
                  :key="index"
                  class="avatars-item"
                  :size="40"
                  :src="item.avatar"
                  @click="changeAvatar(item.avatar, index)"
                ></el-avatar>
              </div>
              <el-button size="small" @click="alterAvatarBtn"
                >确认修改头像</el-button
              >
            </div>
          </div>
          <div class="base-info-item">
            <span class="base-info-lable">邮箱</span>
            <div>
              <span>{{ baseInfo.attrs.email }}</span>
            </div>
          </div>
          <div class="base-info-item">
            <div>
              <span class="base-info-lable">密码</span>
              <el-form>
                <el-form-item>
                  <el-input
                    style="width: 200px; display: block"
                    v-model="baseInfo.attrs.password"
                    type="password"
                  ></el-input>
                  <el-button @click="changePasswordBtn" size="small"
                    >修改密码</el-button
                  >
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>
      <div id="tags-info">
        <div class="main-right-header">标签信息</div>
        <div v-show="userTags.attrs.length <= 0">
          暂无标签
        </div>
        <div v-show="userTags.attrs.length">
          <div ref="tagsChartRef" style="width: 500px; height: 400px"></div>
        <div>
          <el-tag
            :key="tag"
            v-for="tag in userTags.attrs"
            closable
            :disable-transitions="false"
            @close="handleClose(tag.utId)"
          >
            {{ tag.name }}
          </el-tag>
          <!-- <el-input
            class="input-new-tag"
            v-if="inputVisible"
            v-model="inputValue"
            ref="saveTagInput"
            size="small"
            style="width: 90px; margin-left: 10px"
            @keyup.enter.stop="handleInputConfirm"
            @blur="handleInputCancel"
          >
          </el-input>
          <el-button
            v-else
            class="button-new-tag"
            size="small"
            @click="showInput"
            >+ New Tag</el-button
          > -->
        </div>
        </div>
      </div>
      <div id="score-info">
        <div class="main-right-header">积分信息</div>
        <div>
          <div style="margin-bottom: 4%; font-size: 18px">
            <span style="color: #409eff; margin-right: 5%">个人积分</span>
            <span>{{ baseInfo.attrs.score }}</span>
          </div>
          <div>
            <span style="font-size: 18px; color: #409eff">积分记录</span>
            <el-table :data="scoreRecords.attrs" stripe style="width: 100%">
              <el-table-column type="index" label="序号"></el-table-column>
              <el-table-column
                prop="optionTime"
                label="日期"
                :formatter="optionTimeFormat"
              >
              </el-table-column>
              <el-table-column prop="record" label="记录"> </el-table-column>
              <el-table-column prop="desc" label="操作描述"> </el-table-column>
            </el-table>
            <el-pagination
              :hide-on-single-page="pages == 1"
              background
              :current-page="currentPage"
              @current-change="changePage"
              layout="prev, pager, next"
              :total="total"
            >
            </el-pagination>
          </div>
        </div>
      </div>
      <div id="questionnaire-info">
        <div class="main-right-header">我参与的问卷</div>
        <el-table :data="questionnaireRecords.attrs" stripe style="width: 100%">
          <el-table-column type="index" label="序号"></el-table-column>
          <el-table-column
            prop="submitTime"
            label="回答时间"
          >
          </el-table-column>
          <el-table-column prop="questionnaire.title" label="问卷">
          </el-table-column>
          <el-table-column fixed="right">
            <template #header> 操作 </template>
            <template #default="scope">
              <el-button
                size="mini"
                @click="handleQuestionnaireAnswerDetail(scope.$index, scope.row)"
                >查看回答</el-button
              >
              <el-button
                size="mini"
                @click="handleToPublicQuestoinnaire(scope.$index, scope.row)"
                >查看公开问卷分析</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          :hide-on-single-page="pages1 == 1"
          background
          :current-page="currentPage1"
          @current-change="changePage1"
          layout="prev, pager, next"
          :total="total1"
        >
        </el-pagination>
      </div>
    </el-card>
  </div>

  <el-dialog
  :title="questionnaireAnswerDetail.questionnaire.title"
  v-model="centerDialogVisible"
  width="80%"
  center>
      <div>
        <el-card>
          <div class="top" v-if="questionnaireAnswerDetail.questionnaire.description!=null">
            {{questionnaireAnswerDetail.questionnaire.description}}
          </div>
        </el-card>
       <el-card class="box-card" v-for="(item,index) in questionnaireAnswerDetail.attrs" :key="index">
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
                <el-radio :disabled="true" v-model="item.radioValue" :label="optionItem.optionId" style="margin: 5px;">{{ optionItem.content }}</el-radio>
            </div>
        </div>
        

        <!--多选题展示-->
        <el-checkbox-group readonly v-if="item.questionType==2" v-model="item.checkboxValue">
          <div class="text item"  v-for="(optionItem, index) in item.options" :key="index">
            <el-checkbox :disabled="true" :label="optionItem.optionId" style="margin: 5px;">{{ optionItem.content }}</el-checkbox>
          </div>
        </el-checkbox-group>

        <!--填空题展示-->
        <el-input
          v-if="item.questionType==3"
          type="textarea"
          :readonly="true"
          :rows="item.row"
          v-model="item.textValue"
          resize="none">
        </el-input>

      </el-card>
      </div>
     
  <template #footer>
    <span class="dialog-footer">
      <el-button @click="centerDialogVisible = false">关闭</el-button>
    </span>
  </template>

</el-dialog>
</template>

<script>
import { onMounted, ref, reactive, nextTick, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import * as echarts from "echarts";
import "echarts-wordcloud";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getUserById,
  editUser,
  getUserTags,
  addUserTag,
  deleteUserTag,
  getUserScoreRecords,
  getUserSubmitInfoRecords,
  getAnswerDetail,
} from "./api.js";
import qs from "qs";

export default {
  setup() {
    const router = useRouter();
    const store = useStore();
    const userInfo = reactive({
      attrs: {
        avatar: "",
        password: "",
      },
    });
    const baseInfo = reactive({
      attrs: {
        avatar: "",
      },
    });
    const changeId = ref(0);
    const pages = ref(0);
    const total = ref(0);
    const currentPage = ref(1);
    const avatarsList = ref([
      "boy.png",
      "girl.png",
      "love.png",
      "plant.png",
      "rain.png",
      "driven.png",
      "group.png",
    ]);
    const avatarIndex = ref(0);
    const avatars = reactive({
      attrs: [
        { avatar: require("@assets/images/boy.png") },
        { avatar: require("@assets/images/girl.png") },
        { avatar: require("@assets/images/love.png") },
        { avatar: require("@assets/images/plant.png") },
        { avatar: require("@assets/images/rain.png") },
        { avatar: require("@assets/images/driven.png") },
        { avatar: require("@assets/images/group.png") },
      ],
    });
    const tagsChartRef = ref(null);
    const userTags = reactive({
      attrs: [],
    });
    const tagsChart = reactive({
      attrs: [],
    });
    const inputVisible = ref(false);
    const inputValue = ref("");
    const saveTagInput = ref(null);

    const pages1 = ref(0);
    const total1 = ref(0);
    const currentPage1 = ref(1);
    const scoreRecords = reactive({
      attrs: [],
    });

    const questionnaireRecords = reactive({
      attrs: [],
    });

    const deepClone = (obj) => {
      let _obj = JSON.stringify(obj);
      let objClone = JSON.parse(_obj);
      return objClone;
    };

    const local_getUserSubmitInfoRecords = (userId) => {
      getUserSubmitInfoRecords({
        params: { userId: userId, page: currentPage1.value },
      }).then((res) => {
        let data = res.data;
        console.log(data);
        if (data.code == 1) {
          questionnaireRecords.attrs = data.data.records;
          pages1.value = data.data.pages;
          total1.value = data.data.total;
        }
      });
    };

    const changePage1 = (value) => {
      //远程获取
      currentPage1.value = value;
      local_getUserSubmitInfoRecords(baseInfo.attrs.userId);
    };

    const getUserInfo = (userId) => {
      getUserById({
        params: {
          userId: userId,
        },
      })
        .then((res) => {
          let data = res.data;
          console.log(data);
          if (data.code == 1) {
            userInfo.attrs = data.data;
            userInfo.attrs.avatar = require("@assets/images/" +
              data.data.avatar);
            baseInfo.attrs = deepClone(userInfo.attrs);
          } else {
            console.log(data.descriptin);
          }
        })
        .catch((error) => {
          if (error) {
            console.log(error);
            ElMessage.error("网络错误，请重试！");
          }
        });
    };

    const local_getUserTags = (userId) => {
      getUserTags({ params: { userId: userId } }).then((res) => {
        let data = res.data;
        // console.log(data);
        if (data.code == 1) {
          userTags.attrs = data.data;
          data.data.forEach((ut) => {
            let tag = {
              name: ut.name,
              value: ut.value,
            };
            tagsChart.attrs.push(tag);
          });
          let optionThird = {
            attrs: {
              tooltip: {
                show: true,
              },
              series: [
                {
                  type: "wordCloud",
                  gridSize: 15,
                  sizeRange: [16, 80],
                  rotationRange: [-120, 120],
                  shape: "pentagon",
                  left: "center",
                  top: "center",
                  width: "100%",
                  height: "100%",
                  data: tagsChart.attrs,
                  autoSize: {
                    enable: true,
                    minSize: 16,
                  },
                  textStyle: {
                    normal: {
                      color: function () {
                        return (
                          "rgb(" +
                          [
                            Math.round(Math.random() * 160),
                            Math.round(Math.random() * 160),
                            Math.round(Math.random() * 160),
                          ].join(",") +
                          ")"
                        );
                      },
                    },
                    drawOutOfBound: true,
                    emphasis: {
                      shadowBlur: 10,
                      shadowColor: "#333",
                    },
                  },
                },
              ],
            },
          };
          let chart = echarts.init(tagsChartRef.value);
          chart.setOption(optionThird.attrs);
        }
      });
    };

    onMounted(() => {
      let user_session = JSON.parse(sessionStorage.getItem("User_Data"));
      let userId = user_session.userId;
      getUserInfo(userId);
      local_getUserTags(userId);
      local_getUserScoreRecords(userId);
      local_getUserSubmitInfoRecords(userId);
    });

    const changeView = (id) => {
      const baseInfo = document.getElementById("base-info");
      const tagsInfo = document.getElementById("tags-info");
      const scoreInfo = document.getElementById("score-info");
      const questionnaireInfo = document.getElementById("questionnaire-info");
      if (id == "base-info") {
        changeId.value = 0;
        questionnaireInfo.style.display = "none";
        baseInfo.style.display = "block";
        tagsInfo.style.display = "none";
        scoreInfo.style.display = "none";
      } else if (id == "tags-info") {
        changeId.value = 1;
        baseInfo.style.display = "none";
        tagsInfo.style.display = "block";
        questionnaireInfo.style.display = "none";
        scoreInfo.style.display = "none";
      } else if (id == "score-info") {
        changeId.value = 2;
        baseInfo.style.display = "none";
        tagsInfo.style.display = "none";
        questionnaireInfo.style.display = "none";
        scoreInfo.style.display = "block";
      } else if (id == "questionnaire-info") {
        changeId.value = 3;
        baseInfo.style.display = "none";
        tagsInfo.style.display = "none";
        scoreInfo.style.display = "none";
        questionnaireInfo.style.display = "block";
      }
    };

    const changePage = (value) => {
      //远程获取
      currentPage.value = value;
      local_getUserScoreRecords(baseInfo.attrs.userId, value);
    };

    const changeAvatar = (avatar, index) => {
      baseInfo.attrs.avatar = avatar;
      avatarIndex.value = index;
      console.log(avatarsList.value[avatarIndex.value]);
    };
    const alterAvatarBtn = () => {
      ElMessageBox.confirm("确认修改头像？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let userData = {
            _method: "PUT",
            userId: userInfo.attrs.userId,
            avatar: avatarsList.value[avatarIndex.value],
          };
          editUser(qs.stringify(userData)).then((res) => {
            let data = res.data;
            // console.log(data);
            if (data.code == 1) {
              ElMessage.success("修改成功！");
              let user_session = JSON.parse(
                sessionStorage.getItem("User_Data")
              );
              user_session.avatar = avatarsList.value[avatarIndex.value];
              sessionStorage.setItem("User_Data", JSON.stringify(user_session));
              location.reload();
            } else {
              ElMessage.success("修改失败！");
            }
          });
        })
        .catch(() => {
          // ElMessage.warning("修改失败！");
        });
    };

    const changePasswordBtn = () => {
      if (baseInfo.attrs.password == userInfo.attrs.password) {
        return;
      }
      ElMessageBox.confirm("确认修改密码？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let userData = {
            _method: "PUT",
            userId: userInfo.attrs.userId,
            password: baseInfo.attrs.password,
          };
          editUser(qs.stringify(userData)).then((res) => {
            let data = res.data;
            console.log(data);
            if (data.code == 1) {
              ElMessage.success("修改成功！已退出请重新登录！");
              sessionStorage.removeItem("Flag");
              sessionStorage.removeItem("User_Data");
              store.dispatch("userStatus", false);
              window.location.reload();
              router.push("/login");
            } else {
              ElMessage.success("修改失败！");
            }
          });
        })
        .catch(() => {
          // ElMessage.warning("修改失败！");
        });
    };

    const handleClose = (utId) => {
      //.attrs.splice(dynamicTags.attrs.indexOf(tag), 1);
      let deleteData = {
        _method: "DELETE",
        params: {
          utId: utId,
        },
      };
      deleteUserTag(deleteData).then((res) => {
        let data = res.data;
        if (data.code == 1) {
          ElMessage.success("删除成功");
          local_getUserTags(baseInfo.attrs.userId);
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
      ElMessageBox.confirm("确认添加标签？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let userTag = {
            userId: baseInfo.attrs.userId,
            name: inputValue_,
          };
          addUserTag(qs.stringify(userTag)).then((res) => {
            let data = res.data;
            if (data.code == 1) {
              inputVisible.value = false;
              inputValue.value = "";
              ElMessage.success("添加成功！");
              local_getUserTags(baseInfo.attrs.userId);
            } else if (data.code == 2) {
              inputVisible.value = false;
              inputValue.value = "";
              ElMessage.warning(data.description);
            } else {
              inputVisible.value = false;
              inputValue.value = "";
              ElMessage.success("添加失败！");
            }
          });
        })
        .catch(() => {
          // ElMessage.warning("添加失败！");
        });
    };

    const local_getUserScoreRecords = (userId) => {
      getUserScoreRecords({
        params: { userId: userId, page: currentPage.value },
      }).then((res) => {
        let data = res.data;
        console.log(data);
        if (data.code == 1) {
          scoreRecords.attrs = data.data.records;
          pages.value = data.data.pages;
          total.value = data.data.total;
        }
      });
    };

    const optionTimeFormat = (row, cellValue) => {
      // console.log(row.optionTime);
      return row.optionTime.substring(
        0,
        row.optionTime.lastIndexOf(":", row.optionTime.lastIndexOf(":") - 1)
      );
    };

    const centerDialogVisible = ref(false);
    const questionnaireAnswerDetail = reactive({
      questionnaire: {},
      attrs: {},
    });
    const handleQuestionnaireAnswerDetail = (index, row) => {
      console.log(index, row);
      let data = {
        params: {
          questionnaireId: row.questionnaireId,
          userId: row.userId,
        },
      };
      getAnswerDetail(data).then((res) => {
        let data = res.data;
        console.log(data);
        if (data.code == 1) {
          questionnaireAnswerDetail.questionnaire = row.questionnaire;
          questionnaireAnswerDetail.attrs = data.data;
          centerDialogVisible.value = true;
        }
      });
    };

    const handleToPublicQuestoinnaire = (index, row) => {
      if (row.questionnaire.isPublic) {
        router.push("/detail/"+row.questionnaireId)
      } else {
        ElMessage.success("该问卷未被公开，无法查看分析！");
      }
      
    }

    return {
      centerDialogVisible,
      questionnaireAnswerDetail,
      handleQuestionnaireAnswerDetail,
      handleToPublicQuestoinnaire,
      userInfo,
      baseInfo,
      changeId,
      pages,
      currentPage,
      total,
      changePage,
      avatars,
      tagsChartRef,
      scoreRecords,
      changeView,
      changeAvatar,
      alterAvatarBtn,
      changePasswordBtn,
      userTags,
      inputVisible,
      inputValue,
      handleClose,
      handleInputConfirm,
      handleInputCancel,
      showInput,
      saveTagInput,
      optionTimeFormat,
      questionnaireRecords,
      pages1,
      currentPage1,
      total1,
      changePage1,
    };
  },
};
</script>

<style>
#profile-main {
  display: flex;
  flex-direction: row;
  justify-content: center;
  width: 100%;
  height: 100%;
  margin-top: 2%;
}
#main-left {
  display: inline-block;
  width: 300px;
  margin-right: 3%;
}
#main-left .icon-user {
  font-size: 40px;
}
#username {
  margin-top: 5px;
  margin-bottom: 30px;
}
.tab {
}
.tab .is-active {
  color: #409eff;
  background-color: rgba(35, 120, 255, 0.08);
}
.tab .item {
  padding: 0 20px;
  height: 40px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  line-height: 40px;
  display: block;
  list-style: none;
  font-size: 14px;
  font-weight: 500;
  /* color: #303133; */
  position: relative;
  cursor: pointer;
}
/* .tab .item:nth-child(2) {
  border-top: 1px solid #409eff;
  border-bottom: 1px solid #409eff;
} */
/* .tab .item:hover {
  color: #409eff;
  cursor: pointer;
} */
#main-right {
  display: inline-block;
  width: 700px;
  text-align: left;
}
#main-right #base-info {
  display: block;
}
#main-right #tags-info {
  display: none;
}
#main-right #score-info {
  display: none;
}
#main-right #questionnaire-info {
  display: none;
}
.main-right-header {
  font-size: 24px;
  margin-bottom: 2%;
}
.main-right-content {
  margin-top: 5%;
  margin-left: 3%;
}
.base-info-item {
  margin-bottom: 2%;
}
.base-info-lable {
  margin-bottom: 2%;
  display: block;
  font-size: 20px;
}
#base-info-avatar {
  margin-bottom: 2%;
}
#avatars .avatars-item {
  margin: 1%;
  cursor: pointer;
}
.el-tag {
  margin-right: 1%;
  margin-bottom: 1%;
}
/* .el-tag + .el-tag {
  margin-left: 10px;
} */
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
/* .input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
} */
.top {
  color: #606266;
  padding: 0 10px 10px 10px;
  border-bottom: 3px solid #409eff;
  font-size: 15px;
  line-height: 22px;
  text-align: left;
}
.content {
  width: 100%;
  max-width: 800px;
  display: inline-block;
  text-align: center;
}
.box-card {
  text-align: left;
  width: 100%;
  margin: 10px 0 10px 0;
}
</style>