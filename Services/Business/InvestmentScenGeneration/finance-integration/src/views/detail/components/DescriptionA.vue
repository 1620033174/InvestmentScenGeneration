<template>
    <el-descriptions 
      class="margin-top" 
      :column="2" 
      :size="size" 
      :contentStyle="CS" :label-style="LS ">
        <el-descriptions-item label="作者">{{ author }}</el-descriptions-item>
        <el-descriptions-item label="调仓时间">{{ adjustTime }}</el-descriptions-item>
        <el-descriptions-item label="风格">{{ style }}</el-descriptions-item>
        <el-descriptions-item label="创建日期">{{ foundDate }}</el-descriptions-item>
        <el-descriptions-item label="调仓周期">{{ adjustPeriod }}</el-descriptions-item>
        <el-descriptions-item label="下个调仓">{{ adjustDateNext }}</el-descriptions-item>
        <el-descriptions-item label="回测日期">{{ backtest }}</el-descriptions-item>
        <el-descriptions-item label="持仓股票">{{ holdNum }}</el-descriptions-item>
      </el-descriptions>
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
    // 父组件传递过来的数据
      descripData: {
        type: Object,
        required: true
      }
    },
    computed: {
      author() {
        return this.descripData.author;
      },
      adjustTime() {
        return this.descripData.adjustTime;
      },
      // 其他变量类似
    },

    data() {
      return {
        CS: {  //标签列
          'text-align': 'left',  //左对齐
          'min-width': '200px',   //最小宽度
          'word-break': 'break-all',  //过长时自动换行
          'width': '100px'
        },
        LS: {  //内容列
          'color': '#000',
          'text-align': 'left',
          'font-weight': '600',
          'height': '40px',
          'word-break': 'break-all',
          //'background-color': 'rgba(0,155,226, 0.1)',
          'min-width': '100px',
          'word-break': 'keep-all',
          'width': '100px'
    }
        }
    },
    

    
    }
  </script>

