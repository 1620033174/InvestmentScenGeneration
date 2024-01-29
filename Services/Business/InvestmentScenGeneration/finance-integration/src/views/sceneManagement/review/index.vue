<script>

import * as echarts from "echarts";
import {getSceneById} from "@/api/scene";

export default ({
  data() {
    return {
      chosenPictures: [],
      cur: 0,
      percentage: 97.5,
      pictures: [
        {
          id: 1,
          name: "今日评价",
          checked: false
        },
        {
          id: 2,
          name: "涨跌预测",
          checked: false
        },
        {
          id: 3,
          name: "股价预测",
          checked: false
        },
        {
          id: 4,
          name: "个股综合排名",
          checked: false
        },
        {
          id: 5,
          name: "历史最相似时刻",
          checked: false
        },
        {
          id: 6,
          name: "场景相关相似个股",
          checked: false
        },
        {
          id: 7,
          name: "场景无关相似个股",
          checked: false
        }
      ],
      sharePriceChartStyle: {float: "left", width: "500px", height: "400px"},
      xData: ["最低目标价", "最高目标价", "目标均价", "当前价",], //横坐标
      yData: [23, 24, 18, 22], //数据
      colors: [
        {color: '#EE0000', percentage: 100}
      ],
      intraTableData: [
        {
          id: "1",
          name: "东方财富",
          grade: "95",
          change: "+0.95%",
        },
        {
          id: "2",
          name: "中科创达",
          grade: "92",
          change: "+0.82%",
        },
        {
          id: "3",
          name: "宝信软件",
          grade: "87",
          change: "+0.65%",
        },
      ],
      newsData: [{
        time: '03-04',
        desc: "本公司研发自主独立技术",
      },
        {
          time: '03-04',
          desc: "本公司研发自主独立技术",
        },
        {
          time: '03-04',
          desc: "本公司研发自主独立技术",
        },

      ],
      historyData: [
        {
          name: "平安银行",
          code: "000001",
          similarity: 0.95,
          eps: 1.23,
          cfps: 1.56,
          capf: 0.78,
          undp: 0.45,
          capi: 174.32,
          cmv: 285.67
        },
      ],
      otherData: [
        {
          name: "平安银行",
          code: "000001",
          similarity: 0.95,
          eps: 1.23,
          cfps: 1.56,
          capf: 0.78,
          undp: 0.45,
          capi: 174.32,
          cmv: 285.67
        },
        {
          name: "万科A",
          code: "000002",
          similarity: 0.92,
          eps: 2.34,
          cfps: 2.89,
          capf: 1.23,
          undp: 0.67,
          capi: 113.45,
          cmv: 198.76
        },
        {
          name: "国农科技",
          code: "000004",
          similarity: 0.88,
          eps: -0.12,
          cfps: -0.34,
          capf: -0.56,
          undp: -0.89,
          capi: 12.34,
          cmv: 9.87
        }
      ],
      otherData0: [
        {
          name: "退市中新",
          code: "603996",
          similarity: 0.40,
          eps: -0.42,
          cfps: -0.03,
          capf: 1.86,
          undp: -10.69,
          capi: 3.00,
          cmv: 9.87
        },
        {
          name: "赫美集团",
          code: "002356",
          similarity: 0.39,
          eps: -0.12,
          cfps: 0.02,
          capf: 1.50,
          undp: -6.17,
          capi: 5.27,
          cmv: 6.21
        },
        {
          name: "退市拉夏",
          code: "603157",
          similarity: 0.35,
          eps: -0.14,
          cfps: -0.19,
          capf: 3.49,
          undp: -4.01,
          capi: 1.46,
          cmv: 3.02
        }
      ],
      chartOptions: {
        // echarts配置项
        title: {
          text: "柱形图示例",
        },
        tooltip: {},
        legend: {
          data: [],
        },
        xAxis: {
          data: ["每股收益", "每股公积金", "每股未分配利润"], // 模拟的x轴数据
        },
        yAxis: {},
        series: [],
      },
      chartData: {
        // 模拟的图表数据
        行业均值: [5, 20, 36],
        行业中值: [6, 15, 25],
        行业四分位: [4, 18, 32],
        行业排名: [1, 3, 2,],
      },
    }
  },
  computed: {
    previewSceneId() {
      return this.$store.state.scene.previewSceneId
    },
  },
  mounted() {

    getSceneById(this.previewSceneId).then(res => {
      console.log(res)
      let sceneData = res.data.data
      let pictureList = sceneData.pictureList
      pictureList.forEach(item => {
        this.pictures[item.id - 1].checked = true
      })
      this.initEcharts();
      this.initChart(); // 初始化图表
      this.updateChart(); // 更新图表
    })
  },
  methods: {
    changeChosenPictures() {
      this.pictures.forEach(item => {
        if (item.checked) {
          this.chosenPictures.push(item)
        }
      })
      this.$store.dispatch('scene/setPictureList', this.chosenPictures)
    },
    initChart() {
      // 初始化图表

      this.chart = echarts.init(this.$refs.echart);
      this.chart.setOption(this.chartOptions);
    },
    updateChart() {
      // 更新图表
      let series = []; // 存储图表系列数据
      let legend = []; // 存储图表图例数据
      for (let value of this.checkedValues) {
        // 遍历选中的复选框
        legend.push(value); // 将复选框的值作为图例数据
        series.push({
          // 将对应的数据作为系列数据
          name: value,
          type: "bar",
          data: this.chartData[value],
        });
      }
      this.chartOptions.legend.data = legend; // 更新图表图例数据
      this.chartOptions.series = series; // 更新图表系列数据
      this.chart.setOption(this.chartOptions, true); // 设置图表配置项，并覆盖之前的配置项
    },
    initEcharts() {
      // 基本柱状图
      const option1 = {
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [
          {
            type: "bar", //形状为柱状图
            data: this.yData
          }
        ]
      };
      const sharePriceChart = echarts.init(document.getElementById("sharePriceChart"));
      sharePriceChart.setOption(option1);


    },
  }
})
</script>

<template>
  <div class="tab-content">
    <div v-show="pictures[0].checked">
      <div>
        <el-row gutter=5>
          <el-col span=6 style="height: 500px;">
            <el-card style="height: 100%;">
              <div slot="header" class="clearfix" style="text-align: center; ">
                <span>今日评价</span>
              </div>
              <div style="text-align: center;">
                <el-progress type="dashboard" :percentage="percentage"
                             :color="colors"
                             style="margin-top: 50PX;"></el-progress>
                <p style="font-size: 20px; margin-top: 20px;">打败了<span style="color: red;">97.5%</span>的股票
                </p>
                <p>次日涨幅率<span style="color: red">45.15%</span></p>
                <p><span
                  style="font-size:15px">操作建议：近期消息面一般，主力资金有接入迹象，短期呈现震荡趋势，市场关注意愿一般</span>
                </p>

              </div>

            </el-card>
          </el-col>
          <el-col span=10>
            <el-card style="margin-bottom: 10px;" v-show="pictures[1].checked">
              <div slot="header" class="clearfix" style="text-align: center;">
                <span>涨跌预测</span>
              </div>
              <div>
                <span style="font-size: 25px; margin-right: 40px;">次日：</span> 上涨概率<span
                style="color: red; margin-right: 15px;">45.5% </span> 平均涨幅 <span
                style="color: red">0.15%</span>
                <el-divider></el-divider>
                <span style="font-size: 25px; margin-right: 40px;">后日：</span> 上涨概率<span
                style="color: red; margin-right: 15px;">45.5% </span> 平均涨幅 <span
                style="color: red">0.15%</span>
              </div>
            </el-card>
            <el-card v-show="pictures[2].checked">
              <div slot="header" class="clearfix" style="text-align: center;">
                <span>股价预测</span>
              </div>

              <div class="echart" id="sharePriceChart" :style="sharePriceChartStyle"></div>

            </el-card>
          </el-col>
          <el-col span=8>
            <el-card v-show="pictures[3].checked">
              <div slot="header" class="clearfix" style="text-align: center;">
                <span>个股综合排名</span>
              </div>
              <div>
                <el-tag>
                  行业内
                </el-tag>
                <el-table
                  :data="intraTableData"
                  stripe
                  style="width: 100%">
                  <el-table-column
                    prop="id"
                    label="排名"
                  >
                  </el-table-column>
                  <el-table-column
                    prop="name"
                    label="股票名"
                  >
                  </el-table-column>
                  <el-table-column
                    prop="grade"
                    label="综合评分">
                  </el-table-column>
                  <el-table-column
                    prop="change"
                    label="变动趋势"
                  >
                    <template #default="{row}">
                      <span :style="{color: 'red'}">{{ row.change }}</span>
                    </template>
                  </el-table-column>
                </el-table>
                <el-divider></el-divider>
                <el-tag type="danger">
                  全行业
                </el-tag>
                <el-table
                  :data="intraTableData"
                  stripe
                  style="width: 100%">
                  <el-table-column
                    prop="id"
                    label="排名"
                  >
                  </el-table-column>
                  <el-table-column
                    prop="name"
                    label="股票名"
                  >
                  </el-table-column>
                  <el-table-column
                    prop="grade"
                    label="综合评分">
                  </el-table-column>
                  <el-table-column
                    prop="change"
                    label="变动趋势">
                    <template #default="{row}">
                      <span :style="{color: 'red'}">{{ row.change }}</span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

    </div>

    <div v-show="pictures[4].checked0">

      <div style="text-align: center;">
        <p>历史最相似时刻：2021/12/08 相似度：<span style="color: red;">95.13%</span></p>
      </div>


      <el-table :data="historyData" style="width: 100%" border>
        <el-table-column prop="name" label="股票名称" width="180"></el-table-column>
        <el-table-column prop="code" label="股票代码" width="180"></el-table-column>
        <el-table-column prop="similarity" label="相似度" sortable></el-table-column>
        <el-table-column prop="eps" label="每股收益" sortable></el-table-column>
        <el-table-column prop="cfps" label="每股现金流" sortable></el-table-column>
        <el-table-column prop="capf" label="每股公积金" sortable></el-table-column>
        <el-table-column prop="undp" label="每股未分配利润" sortable></el-table-column>
        <el-table-column prop="capi" label="流通股本" sortable></el-table-column>
        <el-table-column prop="cmv" label="流通市值" sortable>
        </el-table-column>
      </el-table>

    </div>
    <div >
      <div>
        <div v-show="pictures[5].checked">
        <el-tag style="font-size: 20px;">场景相关相似个股</el-tag>
        <el-table :data="otherData" style="width: 100%" border>
          <el-table-column prop="name" label="股票名称" width="180"></el-table-column>
          <el-table-column prop="code" label="股票代码" width="180"></el-table-column>
          <el-table-column prop="similarity" label="相似度" sortable></el-table-column>
          <el-table-column prop="eps" label="每股收益" sortable></el-table-column>
          <el-table-column prop="cfps" label="每股现金流" sortable></el-table-column>
          <el-table-column prop="capf" label="每股公积金" sortable></el-table-column>
          <el-table-column prop="undp" label="每股未分配利润" sortable></el-table-column>
          <el-table-column prop="capi" label="流通股本" sortable></el-table-column>
          <el-table-column prop="cmv" label="流通市值" sortable>
          </el-table-column>
        </el-table>
        </div>
        <div v-show="pictures[6].checked">
        <el-tag style="font-size: 20px;">场景无关相似个股</el-tag>
        <el-table :data="otherData0" style="width: 100%" border>
          <el-table-column prop="name" label="股票名称" width="180"></el-table-column>
          <el-table-column prop="code" label="股票代码" width="180"></el-table-column>
          <el-table-column prop="similarity" label="相似度" sortable></el-table-column>
          <el-table-column prop="eps" label="每股收益" sortable></el-table-column>
          <el-table-column prop="cfps" label="每股现金流" sortable></el-table-column>
          <el-table-column prop="capf" label="每股公积金" sortable></el-table-column>
          <el-table-column prop="undp" label="每股未分配利润" sortable></el-table-column>
          <el-table-column prop="capi" label="流通股本" sortable></el-table-column>
          <el-table-column prop="cmv" label="流通市值" sortable>

          </el-table-column>
        </el-table>

      </div>

    </div>

  </div>
  </div>
</template>

<style scoped lang="scss">

</style>
