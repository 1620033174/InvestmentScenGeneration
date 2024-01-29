<template>
    <el-table
      :class="className"
      :data="tableData"
      border
      :style="{height:height,width:width}">
      <el-table-column
        prop="no"
        label="序号"
        width="180">
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
        prop="selldate"
        label="卖出日期"
        width="180">
      </el-table-column>
      <el-table-column
        prop="purchaseprice"
        label="买入价格"
        width="180">
      </el-table-column>
      <el-table-column
        prop="sellprice"
        label="卖出价格"
        width="180">
      </el-table-column>
      <el-table-column
        prop="rate"
        label="涨幅"
        width="180">
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