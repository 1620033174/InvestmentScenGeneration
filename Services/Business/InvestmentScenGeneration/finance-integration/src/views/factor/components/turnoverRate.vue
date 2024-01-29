<template>
  <div id="myChart1" :style="{width: '100%', height: '250px'}" />
</template>
<script>
import {getVariancePic} from "@/api/factor";

export default {
  mounted() {
    this.drawLine()
  },
  props: {
    factorname: {
      type: String,
      default: '每股营业收入'
    },
    stockPool: {
      type: String,
      default: '沪深300'
    },
    stockYear: {
      type: String,
      default: '2020'
    },
    Theme: {
      type: String,
      default: '行业龙头'
    }
  },
  methods: {
    async drawLine() {
      let res = await getVariancePic({stockPool: this.stockPool,
        stockYear: this.stockYear, Theme: this.Theme, factorname: this.factorname})
      const myChart = this.$echarts.init(document.getElementById('myChart1'))
      if (res.code === 20000){
        let varianceData = res.data.variance_list
        let xaxisData = res.data.date_list
        myChart.setOption({
          title: {
            text: ''
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['方差']
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
            data: xaxisData
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '方差',
              type: 'line',
              stack: 'Total',
              data: varianceData,
              smooth: true
            }
          ]
        })
      } else {
        myChart.setOption({
          title: {
            text: ''
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            data: ['IC', '22日移动平均']
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
              name: 'IC',
              type: 'line',
              stack: 'Total',
              data: [120, 132, 101, 134, 90, 230, 210],
              smooth: true
            },
            {
              name: '22日移动平均',
              type: 'line',
              stack: 'Total',
              data: [220, 182, 191, 234, 290, 330, 310],
              smooth: true
            }
          ]
        })
      }
    }
  }
}
</script>
