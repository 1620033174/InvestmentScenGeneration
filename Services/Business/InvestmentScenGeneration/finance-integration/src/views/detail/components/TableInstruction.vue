<template>
    <el-table
      :class="className"
      :data="tableData"
      border
      :style="{height:height,width:width}">
      <el-table-column
        prop="no"
        label="序号"
        width="100">
      </el-table-column>
      <el-table-column
        prop="sharename"
        label="股票名">
      </el-table-column>
      <el-table-column
        prop="purchasedate"
        label="买入日期">
      </el-table-column>
      <el-table-column
        prop="signal"
        label="信号"
        width="180">
      </el-table-column>
      <el-table-column
        prop="price"
        label="参考价">
      </el-table-column>
      <el-table-column
        prop="scale"
        label="目标持仓比例">
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