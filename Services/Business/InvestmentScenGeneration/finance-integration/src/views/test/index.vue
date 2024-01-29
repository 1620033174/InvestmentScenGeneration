<template>
  <div class="container">
    <span>分类：</span>
    <el-cascader
      ref="xzCascader"
      placeholder="请选择"
      :options="options1"
      :props="props"
      filterable
      :show-all-levels="false"
      @change="handleMallCatergoryChange"
      collapse-tags
      collapse-tags-tooltip
      clearable>
    </el-cascader>
  </div>




</template>

<script>
import {getFactorInfo } from "@/api/factor";
export default {
  name: "coupon",
  components: {
  },
  data () {
    return {
      options1:[],
      // 自定义 props
      props: {multiple: true, label: 'label', value: 'value', children: 'children' }
    }
  },
  mounted () {
  },
  created() {
      this.handleGetFactorsInfo();
  },
  methods: {
    handleGetFactorsInfo(){
      console.log("hello");
      console.log(this.stockPool);
      getFactorInfo().then((response)=> {
        console.log(response.data);
        this.options1 = response.data;
      });
    },
    handleMallCatergoryChange () {
      if (this.options1.length != 0) {
        const textArr = [];
        // let arr = this.$refs['cascaderMallCatergory'].getCheckedNodes()[0].pathLabels
        let arr = this.$refs['xzCascader'].getCheckedNodes();
        console.log(this.$refs['xzCascader'].getCheckedNodes()[0].hasChildren);
        console.log(arr);
        arr.forEach((i) => {
          if(i.hasChildren==false){
            textArr.push(i.pathLabels[1]);
          }

          // textArr.push(i.pathLabels);
        });
        console.log(textArr);
        // console.log('mallCatergoryCode', this.mallCatergoryCode)
        // console.log('arr', arr)
      }
    }
  }
}
</script>

<style lang="less" scoped>
.container {
}
</style>
