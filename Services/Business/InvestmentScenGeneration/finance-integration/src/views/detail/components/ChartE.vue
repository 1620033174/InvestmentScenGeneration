<template>
    <el-table
      :class="className"
      :data="tableData"
      border
      :style="{height:height,width:width}">
      <el-table-column
        prop="year"
        label="年份">
      </el-table-column>
      <el-table-column
        prop="strategyprofit"
        label="策略收益">
      </el-table-column>
      <el-table-column
        prop="baseprofit"
        label="基准收益">
      </el-table-column>
      <el-table-column
        prop="strategydrawdown"
        label="策略最大回撤">
      </el-table-column>
      <el-table-column
        prop="basedrawdown"
        label="基准最大回撤">
      </el-table-column>
      <el-table-column
        prop="informationratio"
        label="信息比率">
      </el-table-column>
      <el-table-column
        prop="volatility"
        label="波动率">
      </el-table-column>
      <el-table-column
        prop="sharperatio"
        label="夏普比率">
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