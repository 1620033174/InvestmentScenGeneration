<template>
    <div id="myChart" :style="{width: '100%', height: '250px'}" />
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
        default: ''
      },
      stockPool: {
        type: String,
        default: ''
      },
      stockYear: {
        type: String,
        default: ''
      },
      Theme: {
        type: String,
        default: ''
      }
    },
    methods: {
      async drawLine() {
        let res = await getVariancePic({stockPool: '沪深300',
          stockYear: '2020', Theme: '行业龙头', factorname: '每股营业收入'})
        const myChart = this.$echarts.init(document.getElementById('myChart'))
        if (res.code === 20000){
          let standardData = res.data.standard_deviation_list
          let meanData = res.data.mean_list
          let xaxisData = res.data.date_list
          myChart.setOption({
            title: {
              text: ''
            },
            tooltip: {
              trigger: 'axis'
            },
            legend: {
              data: ['标准差','均值']
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
                name: '标准差',
                type: 'line',
                stack: 'Total',
                data: standardData,
                smooth: true
              },
              {
                name: '均值',
                type: 'line',
                stack: 'Total',
                data: meanData,
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
