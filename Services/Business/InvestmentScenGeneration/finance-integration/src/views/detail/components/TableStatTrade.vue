
<!--交易统计（另一可选项为收益统计）-->
<!--交易统计（另一可选项为收益统计）-->
<!--交易统计（另一可选项为收益统计）-->
<!--交易统计（另一可选项为收益统计）-->
<!--交易统计（另一可选项为收益统计）-->


<template>
    <el-table
      :class="className"
      :data="tableData"
      border
      :style="{height:height,width:width}"
      max-height="200">
      <el-table-column
        prop="annualTurnoverRate"
        label="年换手率">
      </el-table-column>
      <el-table-column
        prop="averageProceed"
        label="平均交易收益">
      </el-table-column>
      <el-table-column
        prop="winRate"
        label="赢率">
      </el-table-column>
      <el-table-column
        prop="winRateMonthAve"
        label="月平均赢率">
      </el-table-column>
      <el-table-column
        prop="exchangeNum"
        label="换股次数">
      </el-table-column>
      <el-table-column
        prop="holdNum"
        label="平均持仓股票数">
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