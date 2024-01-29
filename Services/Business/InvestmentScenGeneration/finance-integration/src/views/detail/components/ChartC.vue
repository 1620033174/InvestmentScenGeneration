
<!--C.最大回撤分布-->
<!--C.最大回撤分布-->
<!--C.最大回撤分布-->
<!--C.最大回撤分布-->
<!--C.最大回撤分布-->


<template>
    <div :class="className" :style="{height:height,width:width}" />
</template>
  
<script>
  import * as echarts from 'echarts'
  import chartResize from './mixins/chart-resize'
  export default {
    mixins:[chartResize],  //自适应大小
    props: {
      className: {
        type: String,
        default: 'chart'
      },
      width: {
        type: String,
        default: '100%'
      },
      height: {
        type: String,
        default: '350px'
      },
      // 父组件传递过来的图表数据
      chartData: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
        // Echarts实例
        chart: null
      }
    },
    watch: {
      /* 如果图表数据是后台获取的，监听父组件中的数据变化，重新触发Echarts */
      chartData: {
        deep: true,
        handler(val) {
          this.setOptions(val)
        }
      }
    },
    mounted() {
      /* 图表初始化 */
      this.$nextTick(() => {
        console.log(this.chartData) // 打印chartData
        this.initChart()
      })
    },
    beforeDestroy() {
      if (!this.chart) {
        return
      }
      /* 释放图表实例 */
      this.chart.dispose()
      /* dispose 会释放内部占用的一些资源和事件绑定，但是解除实例的引用我们是做不到的，所以需要重新赋值为null */
      this.chart = null
    },
    methods: {
      initChart() {
        this.chart = echarts.init(this.$el)
        this.setOptions(this.chartData)
      },
      setOptions({ year,basedrawdown, strategydrawdown } = {}) {
        this.chart.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          grid: {
            top: 35,
            left: '2%',
            right: '2%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: year,
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              type: 'value'
            }
          ],
          series: [
            {
              name: '基准最大回撤',
              type: 'bar',
              data: basedrawdown,
  
            },
            {
              name: '策略最大回撤',
              type: 'bar',
              data: strategydrawdown,
  
            }
          ]
        })
      }
      
    }
  }
  </script>
  