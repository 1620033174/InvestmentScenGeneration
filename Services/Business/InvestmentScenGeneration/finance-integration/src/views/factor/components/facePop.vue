<template>
  <div id="app">
    <!--为echarts准备一个具备大小的容器dom-->
    <div id="main" style="width: 850px; height: 400px"></div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "",
  data() {
    return {
      charts: ""
    };
  },
  methods: {
    drawLine(id) {
      this.charts = echarts.init(document.getElementById(id));
      this.charts.setOption({
      title: {
        text: ' '
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['中证500（000905）', '最小分位数', '二分位数', '三分位数','四分位数','最大分位数']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      toolbox: {
        feature: {  
        saveAsImage: {}
      }
      },
      xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
      type: 'value'
      },
      series: [
      {
        name: '中证500（000905）',
        type: 'line',
        stack: 'Total',
        data: [120, 132, 101, 134, 90, 230, 210],
        smooth: true
      },
      {
        name: '最小分位数',
        type: 'line',
        stack: 'Total',
        data: [220, 182, 191, 234, 290, 330, 310]
      },
      {
        name: '二分位数',
        type: 'line',
        stack: 'Total',
        data: [150, 232, 201, 154, 190, 330, 410],
        smooth: true
      },
      {
        name: '三分位数',
        type: 'line',
        stack: 'Total',
        data: [320, 332, 301, 334, 390, 330, 320],
        smooth: true
      },
      {
        name: '四分位数',
        type: 'line',
        stack: 'Total',
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        smooth: true
      },
      {
        name: '最大分位数',
        type: 'line',
        stack: 'Total',
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        smooth: true
      }
      ]
      });
      },
    },
    //调用
    mounted() {
      this.$nextTick(function () {
      this.drawLine("main");
    });
  },
};
</script>


<style scoped>
</style>