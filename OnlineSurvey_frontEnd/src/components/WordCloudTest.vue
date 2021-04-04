<template>
  <div class="cloud-wrap">
    <div class="cloud-box" ref="cloudEl" />
    <div id="myChart"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";
import wordcloud from "echarts-wordcloud";
export default {
  props: {},
  data() {
    return {
      wordcloud: wordcloud,
      cloudData: [
        { value: 100, name: "文字" },
        { value: 400, name: "图片" },
        { value: 333, name: "参考" },
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
    };
  },
  mounted() {
    this.drawCloud(this.$refs.cloudEl, this.cloudData);
    // this.wordTest();
  },
  methods: {
    wordTest() {
        let myChart = echarts.init(document.getElementById("myChart"));
        myChart.setOption({
        title: { text: "总用户量" },
        tooltip: {},
        xAxis: {
            data: ["12-3", "12-4", "12-5", "12-6", "12-7", "12-8"],
        },
        yAxis: {},
        series: [
            {
            name: "用户量",
            type: "line",
            data: [5, 20, 36, 10, 10, 20],
            },
        ],
        });

    },
    drawCloud(wrapEl, data) {
      let myChart = echarts.init(wrapEl);
      var option = {
        tooltip: {
          show: true,
        },
        series: [
          {
            name: "热词",
            type: "wordCloud",
            sizeRange: [10, 30],
            rotationRange: [-20, 20],
            shape: "circle",
            left: "center",
            top: "center",
            width: "100%",
            height: "80%",
            gridSize: 3,
            textPadding: 0,
            autoSize: {
              enable: true,
              minSize: 6,
            },
            textStyle: {
              normal: {
                color: function () {
                  return (
                    "rgb(" +
                    [
                      Math.round(Math.random() * 250),
                      Math.round(Math.random() * 250),
                      Math.round(Math.random() * 250),
                    ].join(",") +
                    ")"
                  );
                },
              },
              emphasis: {
                shadowBlur: 10,
                shadowColor: "#333",
              },
            },
            data: data,
          },
        ],
      };
      myChart.setOption(option);
    },
  },
};
</script>
<style scoped>
.cloud-wrap {
  width: 490px;
  height: 366px;
  text-align: left;
}
.cloud-box {
    width: 100%;
    height: 100%;
}
</style>