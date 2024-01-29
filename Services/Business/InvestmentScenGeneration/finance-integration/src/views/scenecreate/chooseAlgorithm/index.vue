<script>


import {getAllAlgorithm} from "@/api/algorithm";
import {getStockByAlgorithmId} from "@/api/scene";

export default({
  data() {
    return {
      keyword: "", // 搜索关键词
      algorithms: [
        {
          name: "低估值高增长",
          id: "1",
          logic: "选取市盈率低于行业平均水平，且营业收入增长率高于行业平均水平的股票",
          info: "该算法基于价值投资的理念，寻找被市场低估，但具有较强成长性的股票，适合中长期持有",
        },
        {
          name: "高分红稳健",
          id: "2",
          logic: "选取股息率高于行业平均水平，且净利润增长率稳定的股票",
          info: "该算法基于收益投资的理念，寻找能够持续分红，且业绩稳健的股票，适合长期持有",
        },
        {
          name: "低波动低换手",
          id: "3",
          logic: "选取波动率低于行业平均水平，且换手率低于行业平均水平的股票",
          info: "该算法基于低风险投资的理念，寻找市场波动小，且不受热钱影响的股票，适合短期持有",
        },
      ], // 算法列表
      selectedAlgorithm: {}, // 选中的算法
      selectedAlgorithms: [], // 选中的算法
      stockTableData: [], // 股票列表
      chooseDate: "2020", // 选股日期
      stocks: [
        // 模拟的股票列表
        {
          name: "股票A",
          code: "000001",
          factor1: 0.8,
          factor2: 0.6,
          factor3: 0.7
        },
        {
          name: "股票B",
          code: "000002",
          factor1: 0.9,
          factor2: 0.5,
          factor3: 0.8
        },
        {
          name: "股票C",
          code: "000003",
          factor1: 0.7,
          factor2: 0.7,
          factor3: 0.6
        }
      ], // 股票列表
      factors: [], // 因子列表
    }
  },
  mounted() {
      getAllAlgorithm({name: this.keyword}).then(res => {
        this.algorithms = res.data.data
      })
  },
  methods: {
    search() {
      // 搜索算法
      getAllAlgorithm({name: this.keyword}).then(res => {
        this.algorithms = res.data.data
      })
    },
    selectAlgorithm(val) {
      if (val.length > 3){
        this.limitFn(val)
        this.$message.warning('最多只能选择三个算法')
        return
      }
      if (val.length === 0){
        this.selectedAlgorithms = []
        this.selectedAlgorithm = {}
        this.stockTableData = []
        return
      }
      this.selectedAlgorithms = val
      this.selectedAlgorithm = val[val.length - 1]
      this.stockTableData = []
      this.selectedAlgorithms.forEach(algorithm => {
        getStockByAlgorithmId({id: algorithm.id, date: this.chooseDate}).then(res => {
          this.stockTableData.push(res.data.data.data)
        })
      })
      this.$store.dispatch('scene/setStockTableData', this.stockTableData)
      this.$store.dispatch('scene/setSelectedAlgorithms', this.selectedAlgorithms)
      // 选中算法
      console.log(this.stockTableData)
    },
    limitFn(list) {
      // this.$refs.algorithmTable.clearSelection(); //用于多选表格，清空用户的选择
      this.$refs.algorithmTable.toggleRowSelection(list[list.length-1], false); //用于多选表格，选中用户选择的第一项

    },
    dateChange() {
      // 选股日期改变
      this.stockTableData = []
      this.selectedAlgorithms.forEach(algorithm => {
        getStockByAlgorithmId({id: algorithm.id, date: this.chooseDate}).then(res => {
          this.stockTableData.push(res.data.data.data)
        })
      })
      this.$store.dispatch('scene/setStockTableData', this.stockTableData)
      this.$store.dispatch('scene/setSelectedAlgorithms', this.selectedAlgorithms)
      this.$store.dispatch('scene/Date', this.chooseDate)
    },
  }
})
</script>

<template>
  <div class="app" style="margin-left: 30px;">

    <div class="container1">
      <div class="left">

        <div class="search">
          <el-input
            placeholder="请输入选股算法名或id"
            v-model="keyword"
            @change="checkReset"
            style="display: inline-block; width: 80%"
          ></el-input>
          <el-button
            type="primary"
            @click="search"
            style="display: inline-block"
          >
            搜索
          </el-button>
        </div>


        <div class="list" style="text-align: center;">
          <el-table :data="algorithms" ref="algorithmTable"
                    @selection-change="selectAlgorithm" max-height="400">
            <el-table-column
              type="selection"
              width="55">
            </el-table-column>
            <el-table-column prop="id" label="算法id"></el-table-column>
            <el-table-column prop="name" label="算法名"></el-table-column>
          </el-table>
        </div>
      </div>
      <div class="right">
        <el-card style="margin-left: 30%; margin-top: 10%; width: 70%;">
          <span>选股日期：</span>
          <el-select size="small" v-model="chooseDate" @change="dateChange">
            <el-option :label="'2020'" value="2020"></el-option>
            <el-option :label="'2021'" value="2021"></el-option>
            <el-option :label="'2022'" value="2022"></el-option>
          </el-select>
        </el-card>
        <el-card v-if="selectedAlgorithm" header="选择的算法"
                 style="margin-left: 30%; width: 70%;">
          <p style="font-weight: 100;">算法id: {{ selectedAlgorithm.id }}</p>
          <p style="font-weight: 100;">算法名称：{{ selectedAlgorithm.name }}</p>
          <p style="font-weight: 100;">选股逻辑: {{ selectedAlgorithm.logicDescription }}</p>
          <p style="font-weight: 100;">算法详细信息: {{ selectedAlgorithm.algorithmDescription }}</p>
        </el-card>
      </div>
    </div>


    <div v-for="(o, index) in stockTableData" class="table-list">
      <h3 >{{selectedAlgorithms[index].name}}股票池</h3>
      <el-table max-height="400"  :data="o.rows" border
                style="width: 90%; margin: 0.5rem">
        <el-table-column v-for="item in o.columns"  :key="item.id" :prop="item.prop"
                         :label="item.label"
                         sortable></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped lang="scss">
.container1 {
  display: flex;
  height: 100%;
}
.right {
  width: 50%;
}
.left{
  width: 40%;
}
.list{
  width: 100%;
}
.table-list{
  margin: 0.5rem;
}
</style>
