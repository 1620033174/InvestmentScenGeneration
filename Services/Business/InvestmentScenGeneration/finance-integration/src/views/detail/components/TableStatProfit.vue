
<!--收益统计（另一可选项为交易统计）-->
<!--收益统计（另一可选项为交易统计）-->
<!--收益统计（另一可选项为交易统计）-->
<!--收益统计（另一可选项为交易统计）-->
<!--收益统计（另一可选项为交易统计）-->


<template>
    <el-table
      :class="className"
      :data="tableData"
      border
      :style="{height:height,width:width}"
      max-height="200">
      <el-table-column
        prop="group"
        label="投资组合">
      </el-table-column>
      <el-table-column
        prop="yearYield"
        label="年化收益">
      </el-table-column>
      <el-table-column
        prop="sharpeRatio"
        label="夏普比率">
      </el-table-column>
      <el-table-column
        prop="maxDrawdown"
        label="最大回撤">
      </el-table-column>
      <el-table-column
        prop="volatility"
        label="收益波动率">
      </el-table-column>
      <el-table-column
        prop="infoRatio"
        label="信息比率">
      </el-table-column>
      <el-table-column
        prop="beta"
        label="Beta">
      </el-table-column>
      <el-table-column
        prop="alpha"
        label="Alpha">
      </el-table-column>
      <el-table-column
        prop="days"
        label="创建天数">
      </el-table-column>
      <el-table-column
        prop="yieldSinceInception"
        label="自创建日收益">
      </el-table-column>
    </el-table>
</template>
  
<script>
  export default {
    props: {
      className: {
        type: String,
        default: 'table'
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
      tableData: {
        type: Object,
        required: true
      }
    },
    data() {
      return {
          
        }
    },
    

    
    }
  </script>