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
        default: 'chartA'
      },
      width: {
        type: String,
        default: '1000px'
      },
      height: {
        type: String,
        default: '700px'
      },
      // 父组件传递过来的图表数据
      chartData: {
        type: Object,
        required: true
      },
      strategy:{
        type: Array,
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
          this.setOptions(val, this.strategy)
        }
      },
      strategy: {
        deep: true,
        handler(val) {
          this.setOptions(this.chartData, val)
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
        this.setOptions(this.chartData, this.strategy)
      },
      setOptions({ time,shanghaiIndex, strategyProfit } = {}, strategy = []) {
        this.chart.clear()
        console.log(strategy)
        this.chart.setOption({
          title: {
            text: '最大回撤分布',
            left: 'center'
          },
          legend: {
            x: 'left',
            orient: 'vertical',
            // padding: [0, 50, 50, 50]
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'line' ,// 默认为直线，可选为：'line' | 'shadow'
              formatter: '{value} %'
            },

          },
          dataZoom: [
            {
              type: 'inside',
              start: 0,
              end: 100,
              zoomOnMouseWheel: true,
              // 添加onDataZoom事件
              onDataZoom: function(params) {
                // 获取当前显示的数据范围
                var startValue = params.startValue;
                var endValue = params.endValue;
                // 获取当前显示的数据
                var shanghaiIndexData = chartAData.shanghaiIndex.slice(startValue, endValue + 1);
                var strategyProfitData = chartAData.strategyProfit.slice(startValue, endValue + 1);
                // 计算当前显示的第一个数据与0的差值
                var shanghaiIndexDiff = 0 - shanghaiIndexData[0];
                var strategyProfitDiff = 0 - strategyProfitData[0];
                // 根据比例计算其他数据的百分比
                var newShanghaiIndexData = shanghaiIndexData.map(function(value) {
                  return value + shanghaiIndexDiff;
                });
                var newStrategyProfitData = strategyProfitData.map(function(value) {
                  return value + strategyProfitDiff;
                });
                // 更新图表数据
                myChart.setOption({
                  series: [{
                    data: newShanghaiIndexData
                  }, {
                    data: newStrategyProfitData
                  }]
                });

              }
            },
            {
              show: true,
              type: 'slider',
              top: '90%',
              start: 50,
              end: 100,
              zoomOnMouseWheel: false
            }
          ],
          grid: {
            top: '30%',
            left: '2%',
            right: '2%',
            bottom: '10%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              data: time,
              axisTick: {
                alignWithLabel: true
              }
            }
          ],
          yAxis: [
            {
              type: 'value',
              axisLabel: {
                show: true,
                interval: 'auto',
                formatter: '{value} %'
              },
              show: true
            }
          ],
          series: [
            {
              name: '上证指数',
              type: 'line',
              data: shanghaiIndex,

            },
            ...strategy.map((item, index) => {
              return {
                name: item.name,
                type: 'line',
                data: item.strategy,
              }
            }),
            // {
            //   name: '本策略收益',
            //   type: 'line',
            //   data: strategyProfit,
            //
            // }
          ]
        })
      }

    }
  }
  </script>
