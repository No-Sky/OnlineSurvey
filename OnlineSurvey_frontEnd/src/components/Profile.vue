<template>
  <div id="profile-main">
    <el-card id="main-left" shadow="always">
      <el-avatar :size="100" :src="userInfo.attrs.avatar"></el-avatar>
      <div id="username"><span>username</span></div>
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
                  @click="changeAvatar(item.avatar)"
                ></el-avatar>
              </div>
              <el-button size="small" @click="alterAvatarBtn"
                >确认修改头像</el-button
              >
            </div>
          </div>
          <div class="base-info-item">
            <span class="base-info-lable">邮箱</span>
            <div><span>xfw.nosky@163.com</span></div>
          </div>
          <div class="base-info-item">
            <div>
              <span class="base-info-lable">密码</span>
              <el-form>
                <el-form-item>
                  <el-input
                    style="width: 200px; display: block"
                    v-model="userInfo.attrs.password"
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
        <!-- <el-button @click="wordCloudView">显示标签云</el-button> -->
        <div ref="tagsChartRef" style="width: 500px; height: 400px"></div>
        <div>
          <el-tag
            :key="tag"
            v-for="tag in dynamicTags.attrs"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
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
          >
        </div>
      </div>
      <div id="score-info">
        <div class="main-right-header">积分信息</div>
        <div>
          <div style="margin-bottom: 4%; font-size: 18px">
            <span style="color: #409eff; margin-right: 5%">个人积分</span>
            <span>100</span>
          </div>
          <div>
            <span style="font-size: 18px; color: #409eff">积分记录</span>
            <el-table :data="scoreRecords.attrs" stripe style="width: 100%">
              <el-table-column type="index" label="序号"></el-table-column>
              <el-table-column prop="date" label="日期" width="180">
              </el-table-column>
              <el-table-column prop="record" label="记录" width="180">
              </el-table-column>
              <el-table-column prop="desc" label="操作描述" width="180">
              </el-table-column>
            </el-table>
            <el-pagination
              :hide-on-single-page="pages == 1"
              background
              :current-page="currentPage"
              :current-change="changePage"
              layout="prev, pager, next"
              :total="total"
            >
            </el-pagination>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { onMounted, ref, reactive, nextTick } from "vue";
import * as echarts from "echarts";
import "echarts-wordcloud";
import { ElMessage, ElMessageBox } from "element-plus";

export default {
  setup() {
    const userInfo = reactive({
      attrs: {
        avatar: require("@assets/images/boy.png"),
        password: "123456",
      },
    });
    const baseInfo = reactive({
      attrs: {
        avatar: require("@assets/images/boy.png"),
      },
    });
    const changeId = ref(0);
    const pages = ref(0);
    const total = ref(0);
    const currentPage = ref(1);
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
    const tags = reactive({
      attrs: [
        { name: "标签一", type: "" },
        { name: "标签二", type: "success" },
        { name: "标签三", type: "info" },
        { name: "标签四", type: "warning" },
        { name: "标签五", type: "danger" },
      ],
    });

    const dynamicTags = reactive({ attrs: ["标签一", "标签二", "标签三"] });
    const inputVisible = ref(false);
    const inputValue = ref("");
    const saveTagInput = ref(null);

    const scoreRecords = reactive({
      attrs: [
        {
          date: Date.now(),
          record: 5,
          desc: "回答问卷",
        },
        {
          date: Date.now(),
          record: 5,
          desc: "回答问卷",
        },
      ],
    });

    const optionThird = reactive({
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
            data: [
              { value: 2000, name: "文字" },
              { value: 400, name: "图片" },
              { value: 1000, name: "参考" },
              { value: 855, name: "音视频" },
              { value: 343, name: "新媒体" },
              { value: 343, name: "测试1" },
              { value: 43, name: "测试2" },
              { value: 543, name: "测试3" },
              { value: 333, name: "测试4" },
              { value: 323, name: "测试5" },
              { value: 33, name: "测试6" },
              { value: 13, name: "测试7" },
              { value: 543, name: "测试8" },
              { value: 66, name: "测试9" },
              { value: 666, name: "测试10" },
            ],
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
    });

    onMounted(() => {
      let chart = echarts.init(tagsChartRef.value);
      chart.setOption(optionThird.attrs);
    });

    const changeView = (id) => {
      const baseInfo = document.getElementById("base-info");
      const tagsInfo = document.getElementById("tags-info");
      const scoreInfo = document.getElementById("score-info");
      if (id == "base-info") {
        changeId.value = 0;
        baseInfo.style.display = "block";
        tagsInfo.style.display = "none";
        scoreInfo.style.display = "none";
      } else if (id == "tags-info") {
        changeId.value = 1;
        baseInfo.style.display = "none";
        tagsInfo.style.display = "block";
        scoreInfo.style.display = "none";
      } else if (id == "score-info") {
        changeId.value = 2;
        baseInfo.style.display = "none";
        tagsInfo.style.display = "none";
        scoreInfo.style.display = "block";
      }
    };

    const changePage = () => {
      //远程获取
    };

    const changeAvatar = (avatar) => {
      baseInfo.attrs.avatar = avatar;
    };
    const alterAvatarBtn = () => {
      ElMessageBox.confirm("确认修改头像？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          userInfo.attrs.avatar = baseInfo.attrs.avatar;
          ElMessage.success("修改成功！");
        })
        .catch(() => {
          // ElMessage.warning("修改失败！");
        });
    };

    const changePasswordBtn = () => {
      ElMessageBox.confirm("确认修改密码？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //修改密码操作
          ElMessage.success("修改成功！");
        })
        .catch(() => {
          ElMessage.warning("修改失败！");
        });
    };

    const handleClose = (tag) => {
      dynamicTags.attrs.splice(dynamicTags.attrs.indexOf(tag), 1);
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
          dynamicTags.attrs.push(inputValue_);
          inputVisible.value = false;
          inputValue.value = "";
          ElMessage.success("添加成功！");
        })
        .catch(() => {
          // ElMessage.warning("添加失败！");
        });
    };

    return {
      userInfo,
      baseInfo,
      changeId,
      pages,
      currentPage,
      total,
      changePage,
      avatars,
      tagsChartRef,
      tags,
      scoreRecords,
      changeView,
      changeAvatar,
      alterAvatarBtn,
      changePasswordBtn,
      dynamicTags,
      inputVisible,
      inputValue,
      handleClose,
      handleInputConfirm,
      handleInputCancel,
      showInput,
      saveTagInput,
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
</style>