<!--
程序名：数据分析页面
功能：对问卷调查结果的数据进行分析并用图表可视化展示
-->
<template>
  <div class="Count" v-loading="loading" element-loading-text="生成中...">
    <div>
      <a
        :href="/display/ + questionnaire.attrs.questionnaireId"
        title="去填写问卷"
      >
        <h2>{{ questionnaire.attrs.title }}</h2>
      </a>
      <div class="desc" v-if="questionnaire.attrs.description != ''">
        {{ questionnaire.attrs.description }}
      </div>
    </div>

    <!-- <div v-if="detail.attrs == undefined || detail.attrs.length <= 0">
      暂时没有数据
    </div> -->
    <el-card
      class="question"
      v-for="(item, index) in detail.attrs"
      :key="index"
    >
      <div slot:header class="clearfix">
        <span>{{ index + 1 + "." + item.questionTitle }}</span>
      </div>
      <!--如果数据库中的问题类型为单项选择或者多项选择-->
      <!--则将数据库中的数据以表格、柱状图、饼状图、圆环图、条形图的方式进行展示-->
      <div v-if="item.questionType == 1 || item.questionType == 2">
        <el-table
          size="small"
          :data="item.analysisResult"
          style="width: 100%"
          stripe
          class="table"
        >
          <el-table-column prop="option" label="选项"></el-table-column>
          <el-table-column
            prop="count"
            label="数量"
            width="180"
          ></el-table-column>
          <el-table-column
            prop="percent"
            label="占比"
            width="180"
          ></el-table-column>
        </el-table>
        <br />

        <el-button
          size="mini"
          type="primary"
          plain
          @click="changeValue(index, 1)"
          >柱状图</el-button
        >
        <el-button
          size="mini"
          type="primary"
          plain
          @click="changeValue(index, 2)"
          >饼状图</el-button
        >
        <el-button
          size="mini"
          type="primary"
          plain
          @click="changeValue(index, 3)"
          >圆环图</el-button
        >
        <el-button
          size="mini"
          type="primary"
          plain
          @click="changeValue(index, 4)"
          >条形图</el-button
        >
        <el-button
          size="mini"
          type="primary"
          plain
          @click="changeValue(index, 0)"
          >隐藏图表</el-button
        >

        <div :id="'img' + index" class="img" v-show="visible[index] == 1"></div>
        <div
          :id="'bing' + index"
          class="bing"
          v-show="visible[index] == 2"
        ></div>
        <div
          :id="'ring' + index"
          class="ring"
          v-show="visible[index] == 3"
        ></div>
        <div :id="'tz' + index" class="tz" v-show="visible[index] == 4"></div>
      </div>
      <!--如果数据库中的问题类型为text类型则将数据以弹窗表格的形式进行显示-->
      <div v-if="item.questionType == 3">
        <el-button
          size="mini"
          type="primary"
          plain
          @click="lookTextDetail(item.questionId)"
          >详细内容</el-button
        >
      </div>
    </el-card>
    <el-dialog title="详细内容" v-model="dialogTableVisible">
      <el-table :data="tableData.attrs">
        <el-table-column property="answerText" label="答案"></el-table-column>
      </el-table>
      <el-pagination
        @current-change="currentChange"
        :current-page.="currentPage"
        layout="total, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-dialog>
  </div>
</template>
<script>
import * as echarts from "echarts";
import {
  getQuestionnaireById,
  getDataAnalysis,
  getAnswerText,
  addUserTag,
} from "./api";
import { onMounted, reactive, ref } from "vue";
import { ElMessageBox } from "element-plus";
import { useRoute } from "vue-router";
import qs from 'qs';

export default {
  name: "Detail",
  setup() {
    const dialogTableVisible = ref(false);
    const visible = reactive([]);
    const loading = ref(false);
    const detail = reactive({ atrrs: [] });
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);
    const tableData = reactive({ atrrs: [] });
    const questionId = ref(0);
    const wjId = ref(0);
    const route = useRoute();
    const questionnaire = reactive({
      attrs: {},
    });

    const getQuesrionnaire = (questionnaireId) => {
      getQuestionnaireById({
        params: { questionnaireId: questionnaireId },
      }).then((res) => {
        let data = res.data;
        if (data.code == 1) {
          questionnaire.attrs = data.data;
          userRelationTags(data.data.tags);
        } else {
          console.log(data.description);
        }
      });
    };

    // 请求后端数据
    const dataAnalysis = (id) => {
      loading.value = true;
      wjId.value = id;
      getDataAnalysis({
        params: {
          questionnaireId: wjId.value,
        },
      }).then((res) => {
        let data = res.data;
        // console.log(data);
        detail.attrs = data.data;
        loading.value = false;
      });
      //   dialogShow.value = false;
    };

    const userRelationTags = (tags) => {
      let user_data = JSON.parse(sessionStorage.getItem("User_Data"));
      if (user_data == null || user_data == undefined) return;
      for (let i = 0; i < tags.length; i++) {
        let data = {
          userId: user_data.userId,
          tagId: tags[i].tagId,
        };
        addUserTag(qs.stringify(data))
          .then((res) => {
            let data = res.data;
            if (data.code == 1) {
              console.log("关联标签成功");
            } else {
              console.log("关联标签失败");
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    };

    onMounted(() => {
      // const { questionnaireId, title, description } = props.questionnaire;
      let questionnaireId = route.params.id;
      getQuesrionnaire(questionnaireId);
      dataAnalysis(questionnaireId);
    });

    //柱状图
    const setImg = (id) => {
      let myChart = echarts.init(document.getElementById("img" + id));
      let option = {
        tooltip: {},
        legend: {
          data: ["数量"],
        },
        dataset: {
          dimensions: ["option", "count", "percent"],
          source: detail.attrs[id].analysisResult,
        },
        xAxis: {
          type: "category",
        },
        yAxis: {},
        series: [
          {
            name: "数量：",
            type: "bar",
            barWidth: 30,
            color: "deepskyblue",
          },
        ],
      };
      myChart.setOption(option);
    };

    // 饼状图
    const setPar = (id) => {
      let myChart = echarts.init(document.getElementById("bing" + id));
      let option = {
        tooltip: {},
        color: ["#409EFF", "#FFB54D", "#FF7466", "#44DB5E"],
        legend: {
          data: ["数量"],
        },
        dataset: {
          dimensions: ["option", "count", "percent"],
          source: detail.attrs[id].analysisResult,
        },
        series: [
          {
            name: "统计结果：",
            type: "pie",
            radius: "55%",
            center: ["50%", "50%"],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)",
              },
            },
          },
        ],
      };
      myChart.setOption(option);
    };

    // 圆环图
    const setRing = (id) => {
      //console.log(id);
      let myChart = echarts.init(document.getElementById("ring" + id));
      let option = {
        tooltip: {},
        legend: {},
        color: ["#409EFF", "#FFB54D", "#FF7466", "#44DB5E"],
        dataset: {
          dimensions: ["option", "count", "percent"],
          source: detail.attrs[id].analysisResult,
        },
        series: [
          {
            name: "数量：",
            type: "pie",
            radius: ["50%", "70%"],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: "center",
              },
              emphasis: {
                show: true,
                textStyle: {
                  fontSize: "30",
                  fontWeight: "bold",
                },
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },
          },
        ],
      };
      myChart.setOption(option);
    };

    //條形图
    const setTz = (id) => {
      //console.log(id);
      let myChart = echarts.init(document.getElementById("tz" + id));
      let option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow",
          },
        },
        dataset: {
          dimensions: ["option", "count", "percent"],
          source: detail.attrs[id].analysisResult,
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: {
          type: "value",
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: "category",
        },
        series: [
          {
            name: "数量：",
            type: "bar",
            barWidth: 30,
            color: "#409EFF",
          },
        ],
      };
      myChart.setOption(option);
    };

    //切换图表
    const changeValue = (num, value) => {
      visible[num] = value;
      if (value == 1) {
        setImg(num);
      } else if (value == 2) {
        setPar(num);
      } else if (value == 3) {
        setRing(num);
      } else if (value == 4) {
        setTz(num);
      }
    };

    //文本内容
    const setText = (id) => {
      return {
        resule: detail.attrs[id].result,
      };
    };

    // 获取表格数据
    const getTableData = (page) => {
      getAnswerText({
        params: {
          questionId: questionId.value,
          currentPage: page,
          pageSize: pageSize.value,
        },
      }).then((res) => {
        let data = res.data;
        // console.log(data);
        tableData.attrs = data.data.texts;
        total.value = data.data.total;
      });
    };

    //查看文本回答详情
    const lookTextDetail = (questionId_) => {
      tableData.attrs = [];
      pageSize.value = 10;
      total.value = 0;
      currentPage.value = 1;
      dialogTableVisible.value = true;
      questionId.value = questionId_;
      getTableData(1);
    };

    const currentChange = (value) => {
      currentPage.value = value;
      getTableData(value);
    };

    return {
      dialogTableVisible,
      visible,
      loading,
      detail,
      currentPage,
      pageSize,
      total,
      tableData,
      questionId,
      wjId,
      dataAnalysis,
      setImg,
      setPar,
      setRing,
      setTz,
      changeValue,
      setText,
      getTableData,
      lookTextDetail,
      currentChange,
      questionnaire,
    };
  },
};
</script>
<style scoped>
a {
  text-decoration: none;
  color: #333333;
}
a:hover {
  color: cornflowerblue;
}
.Count {
}
.Count .question {
  max-width: 800px;
  width: 80%;
  display: inline-block;
  margin: 5px;
  text-align: left;
}
.Count .table {
}
.Count .img {
  width: 500px;
  height: 300px;
}
.Count .bing {
  width: 500px;
  height: 300px;
}
.Count .ring {
  width: 500px;
  height: 300px;
}
.Count .tz {
  width: 500px;
  height: 300px;
}
.opera-buttons {
  padding: 10px;
}
.desc {
  color: #606266;
  padding: 0 10px 10px 10px;
  border-bottom: 1px solid #409eff;
  font-size: 15px;
  width: 500px;
  text-align: center;
  margin: auto;
}
</style>
