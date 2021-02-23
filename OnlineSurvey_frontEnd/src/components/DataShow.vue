<!--
程序名：数据分析页面
功能：对问卷调查结果的数据进行分析并用图表可视化展示
-->
<template>
  <div
    id="pdfDom"
    class="Count"
    v-loading="loading"
    element-loading-text="生成中..."
  >
    <div v-if="!(detail.length == 0)" class="opera-buttons">
      <el-button
        type="primary"
        size="mini"
        @click="analysisExportExcel"
        :loading="exportExcelLoading"
        >导出excel</el-button
      >
      <el-button type="success" size="mini" @click="exportPdf"
        >导出PDF</el-button
      >
    </div>
    <div v-if="detail.length == 0">暂时没有数据</div>
    <el-card class="question" v-for="(item, index) in detail" :key="index">
      <div slot:header class="clearfix">
        <span>{{ index + 1 + "." + item.title }}</span>
      </div>
      <!--如果数据库中的问题类型为单项选择或者多项选择-->
      <!--则将数据库中的数据以表格、柱状图、饼状图、圆环图、条形图的方式进行展示-->
      <div v-if="item.type == 'radio' || item.type == 'checkbox'">
        <el-table
          size="small"
          :data="item.result"
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
      <div v-if="item.type == 'text'">
        <el-button
          size="mini"
          type="primary"
          plain
          @click="lookTextDetail(item.questionId)"
          >详细内容</el-button
        >
        <el-button
          size="mini"
          type="primary"
          plain
          @click="answerText2Excel(item.questionId)"
          :loading="item.questionId == answerText2ExcelQeustionId"
          >导出excel</el-button
        >
      </div>
    </el-card>
    <el-dialog title="详细内容" v-model="dialogTableVisible">
      <el-table :data="tableData">
        <el-table-column property="context" label="答案"></el-table-column>
      </el-table>
      <el-pagination
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page.="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-dialog>
  </div>
</template>
<script>
import echarts from "echarts";
import { getDataAnalysis, getAnswerText } from "./api";
import { reactive, ref } from "vue";

export default {
  name: "DataShow",
  setup() {
    const dialogTableVisible = ref(false);
    let visible = reactive([]);
    const loading = ref(false);
    let detail = reactive([]);
    const currentPage = ref(1);
    const pageSize = ref(10);
    const total = ref(0);
    let tableData = reactive([]);
    const questionId = ref(0);
    const wjId = ref(0);
    const exportExcelLoading = ref(false);
    const answerText2ExcelQeustionId = ref(0);

    // 请求后端数据
    const dataAnalysis = (id) => {
      loading.value = true;
      detail = [];
      wjId.value = id;
      console.log("wjid===" + wjId.value);
      getDataAnalysis({
        opera_type: "dataAnalysis",
        username: "test",
        wjId: id,
      }).then((res) => {
        let data = res.data;
        console.log(data);
        console.log(data.detail);
        detail = data.detail;
        visible = [];
        loading.value = false;
      });
      //   dialogShow.value = false;
    };

    //柱状图
    const setImg = (id) => {
      console.log(id);
      console.log(detail[id].result);
      let myChart = echarts.init(document.getElementById("img" + id));
      let option = {
        tooltip: {},
        legend: {
          data: ["数量"],
        },
        dataset: {
          dimensions: ["option", "count", "percent"],
          source: detail[id].result,
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
          source: detail[id].result,
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
          source: detail[id].result,
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
          source: detail[id].result,
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
      this.$set(visible, num, value);
      console.log("num=" + num);
      console.log("value=" + value);
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
        resule: detail[id].result,
      };
    };

    // 获取表格数据
    const getTableData = () => {
      getAnswerText({
        opera_type: "get_text_answer_detail",
        questionId: this.questionId,
        currentPage: this.currentPage,
        pageSize: this.pageSize,
      }).then((res) => {
        let data = res.data;
        console.log(data);
        tableData = data.detail;
        total.value = data.total;
      });
    };

    //查看文本回答详情
    const lookTextDetail = (questionId_) => {
      tableData = [];
      pageSize.value = 10;
      total.value = 0;
      currentPage.value = 1;
      dialogTableVisible.value = true;
      questionId.value = questionId_;
      getTableData();
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
      exportExcelLoading,
      answerText2ExcelQeustionId,
      dataAnalysis,
      setImg,
      setPar,
      setRing,
      setTz,
      changeValue,
      setText,
      getTableData,
      lookTextDetail,
    };
  },
  // data() {
  //   return {
  //     dialogTableVisible: false,
  //     visible: [],
  //     loading: false,
  //     detail: [],
  //     currentPage: 1,
  //     pageSize: 10,
  //     total: 0,
  //     tableData: [],
  //     questionId: 0,
  //     wjId: 0,
  //     exportExcelLoading: false,
  //     answerText2ExcelQeustionId: 0
  //   };
  // },
  // mounted() {
  //   //      this.dataAnalysis()
  //   //      console.log(this.visible);
  // },
  // methods: {
  //   answerText2Excel(questionId) {
  //     this.answerText2ExcelQeustionId = questionId;
  //     designOpera({
  //       opera_type: "answer_text_to_excel",
  //       questionId: questionId
  //     }).then(data => {
  //       this.doDownload(data.b64data, data.filename, "excel");
  //       this.answerText2ExcelQeustionId = 0;
  //     });
  //   },
  //   // 导出pdf
  //   exportPdf() {
  //     this.$alert("正在开发...", "提示");
  //   },
  //   // 导出excel
  //   analysisExportExcel() {
  //     this.exportExcelLoading = true;
  //     designOpera({
  //       opera_type: "analysis_export_excel",
  //       wjId: this.wjId
  //     }).then(data => {
  //       this.doDownload(data.b64data, data.filename, "excel");
  //       this.exportExcelLoading = false;
  //     });
  //   },
  //   doDownload(data, filename, type) {
  //     var b64data = data; //base64数据
  //     // b64data = b64data.replace("data:" + type + ";base64,", "");
  //     var bdata = this.dataURLtoBlob(b64data);
  //     if (!b64data) {
  //       return;
  //     }
  //     let url = window.URL.createObjectURL(new Blob([bdata]));
  //     let link = document.createElement("a");
  //     link.style.display = "none";
  //     link.href = url;
  //     //        link.download = 'ea7c0cf24153e0cd62bc8b64841fd84d.jpg'; //下载后文件名
  //     link.setAttribute("download", filename);

  //     document.body.appendChild(link);
  //     link.click();
  //   },
  //   dataURLtoBlob(dataurl) {
  //     //          dataurl=dataurl.replace('data:application/json;base64,','')
  //     console.log(dataurl);
  //     var bstr = atob(dataurl),
  //       n = bstr.length,
  //       u8arr = new Uint8Array(n);
  //     while (n--) {
  //       u8arr[n] = bstr.charCodeAt(n);
  //     }
  //     return u8arr;
  //   },
  //
  //   sizeChange() {
  //     this.getTableData();
  //   },
  //   currentChange() {
  //     this.getTableData();
  //   },
  //
  //
  //

  //
  //
  //
  //
  //
  // }
};
</script>
<style scoped>
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
</style>
