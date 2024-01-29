
<!--
  超额收益分析的标题
-->
<template>
<div>
  <div class="dashboard-container">
    <div class="dashboard-text">超额收益分析</div>
    <!--分割线-->
    <el-divider></el-divider>
  </div>


   <!--第一行容器-->
  <el-container>
  <el-main>
    <!--日期选择器-->
    <!--下面为<span>标签添加了一个类名date-picker-title-->
    <div class="block">
      <span class="demonstration date-picker-title">日期选择：</span>
      <el-date-picker
        class="date-picker inline-block"
        v-model="date"
        type="daterange"
        align="left"
        unlink-panels
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions">
      </el-date-picker>
  
      <span class="demonstration select-title">基准：</span>
      <el-select class="select inline-block" v-model="baseline" placeholder="请选择">
        <el-option
          v-for="(item,index) in options"
          :key="'option'+index"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
 
    </div>
  </el-main>
  </el-container>
  
  <!--第二行容器-->

  

</div>
</template>




<script>
import { mapGetters } from 'vuex'

export default {

  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },

  data() {

      return {

        //日期选择器
        pickerOptions: {
          shortcuts: [{
            text: '本月',
            onClick(picker) {
              picker.$emit('pick', [new Date(), new Date()]);
            }
            }, 
            {text: '今年至今',
            onClick(picker) {
              const end = new Date();
              const start = new Date(new Date().getFullYear(), 0);
              picker.$emit('pick', [start, end]);
            }
            }, 
            {text: '最近六个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setMonth(start.getMonth() - 6);
              picker.$emit('pick', [start, end]);
            }
            }]
        },

        //下拉单选框的选项
        options: [
        { value: 'Option1', label: '选项1' },
        { value: 'Option2', label: '选项2' },
        { value: 'Option3', label: '选项3' }
      ],
      
        date: '',
        baseline: ''
      };
      
    }


}


</script>

  <style lang="scss" scoped>
  .dashboard {
	&-container {
  margin: 30px;
	}
	&-text {
  font-size: 30px;
  line-height: 46px;
	}
  }

  //日期选择器标题缩进设置
  .date-picker-title {
    margin-left: 30px;
  }


  //inline-block设置，这个属性的作用是把日期选择和选择器显示在同一行
  .inline-block {
    display: inline-block;
    width: 250px;
  }
  //日期选择器下方框体被字体挡住，所以把日期选择器和选择器同时设置下方边距
  //但是发现对齐是按照字体的，明天需要知道如何按照框线对齐
  //明天还要知道如何根据框线设置对齐
  //还要知道如何设置布局（布局可以帮我分栏吗，好痛苦）
  .date-picker {
    padding-bottom: 5px;
  }
  .select {
    padding-bottom: 5px;
  }

  </style>
