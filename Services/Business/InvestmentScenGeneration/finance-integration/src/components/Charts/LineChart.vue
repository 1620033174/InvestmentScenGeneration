<template>
  <div ref="myChart" class="chart" />
  <!-- TODO: 做一个不带时间轴不带图例的单数据simple version -->
  <!-- TODO: 改成自适应大小 -->
</template>

<script>
import { getLineChartOption, demoChartInput } from "./static";
export default {
  mounted() {
    this.chartInstance = this.$echarts.init(this.$refs.myChart);
    this.renderChart();
  },
  props: {
    chartInput: {
      type: Object,
    },
  },
  data() {
    return {
      chartInstance: null,
    };
  },
  watch: {
    /* 监听父组件中的数据变化，重新触发Echarts */
    chartInput: {
      deep: true,
      handler() {
        this.renderChart();
        console.log("refresh");
      },
    },
  },
  methods: {
    renderChart() {
      // const option = getLineChartOption(demoChartInput);
      this.chartInstance.clear();
      const option = getLineChartOption(this.chartInput);
      this.chartInstance.setOption(option);
      // window.addEventListener("resize", function () {
      //   this.chartInstance.resize(); //页面大小变化后Echarts也更改大小
      // });
    },
  },
};
</script>

<style lang="scss" scoped>
.chart {
  width: 1000px;
  height: 700px;
}
</style>
