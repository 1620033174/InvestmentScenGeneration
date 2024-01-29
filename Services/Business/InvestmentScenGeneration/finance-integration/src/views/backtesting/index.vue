<template>
  <div class="view">
    <div class="page">
      <div
        style="z-index: 999;position: absolute; width: 95%; top: 0; flex-flow: column; display: flex; justify-content: center; align-items: center">
        <h3>算法回测分析</h3>
        <div class="pre-selected-bar">
          <div style="display: flex; justify-content: center; width: 100%;">
            <div>
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                align="right"
                unlink-panels
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :picker-options="pickerOptions"
                style="width: 250px"
              />
              初始资金：
              <el-input-number
                v-model="initialCapital"
                :min="0"
                :max="100000000"
                :step="10000"
                style="width: 150px">
              </el-input-number>
              <el-button
                style="margin: 2rem"
                type="primary"
                icon="el-icon-caret-right"
                @click="handleRun"
                :disabled="selectedList.length === 0"
              >
                运行
              </el-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-show="showChart"
         style="margin: 30px; width: 100%; left: 0;  margin-top: 90%;
          display: flex; flex-flow: column; justify-content: center; align-items: center">
      <div style="display: flex;  justify-content: center; width: 100% ">
        <div
          style="color: black; font-weight: bold; margin: 5px"
          v-for="(info, idx) in resultInfos"
          :key="idx"
          v-if="resultInfos.length > 0"
        >{{ info.name }}收益:
          <el-tag>{{ info.yieldRate }}%</el-tag>
        </div>
      </div>
      <line-chart :chartInput="chartInput"/>
      <!--          <line-chart :chartInput="chartInput" />-->
      <div style="display: flex; justify-content: flex-start; margin-top: 10%">
        <div
          style="color: black; font-weight: bold; margin: 5px"
          v-for="(info, idx) in resultInfos"
          :key="idx"
          v-if="resultInfos.length > 0"
        >
          {{ info.name }}最大回撤:
          <el-tag type="danger">{{ info.maximum }}</el-tag>
        </div>
      </div>
      <chart-a :chart-data="chartData" :strategy="strategyData"/>
    </div>
    <!-- dialog等 -->
    <el-dialog
      :title="activeStep === 0 ? '预选列表' : '选择最优算法'"
      :visible.sync="dialogVisible"
      width="50%"
      append-to-body
      :before-close="handleClose"
      destroy-on-close
    >
      <el-table
        :data="preselectedList"
        stripe
        style="width: 100%"
        empty-text="预选列表中暂无内容"
      >
        <el-table-column prop="id" label="id" width="80"></el-table-column>
        <el-table-column prop="name" label="算法名" width="180">
        </el-table-column>
        <el-table-column align="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              v-if="activeStep === 0"
            >删除
            </el-button
            >
            <el-button size="mini" type="primary" v-if="activeStep === 1"
            >确定
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer" v-if="activeStep === 0">
        <el-button
          type="primary"
          :disabled="preselectedList.length === 0"
          @click="
            dialogVisible = false;
            page = 2;
            activeStep = 1;
          "
        >开始回测</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import SearchList from "./components/SearchList.vue";
import PreselectedList from "./components/PreselectedList.vue";
import LineChart from "@/components/Charts/LineChart.vue";
import AlgorithmPreview from "@/components/AlgorithmPreview";
import {
  executeAlgorithmContentByFileName,
  getAlgorithmById,
} from "@/api/algorithm";
import ChartA from "@/views/detail/components/ChartA.vue";

export default {
  components: {
    ChartA,
    PreselectedList,
    SearchList,
    LineChart,
    AlgorithmPreview,
  },
  props: {
    selectedAlgorithms: {
      type: Array,
      default: () => [],
      required: true,
    },
  },
  watch: {
    // selectedAlgorithms: {
    //   handler: function (newVal) {
    //     this.preselectedList = []
    //     this.preselectedList.push(...newVal);
    //     this.selectedList = []
    //     console.log("selectedAlgorithms", newVal)
    //   },
    //   deep: true,
    //   immediate: true,
    // },
  },
  mounted() {
    this.preselectedList.push(...this.selectedAlgorithms);
    this.selectedList.push(...this.selectedAlgorithms);
    this.handleRun()
  },
  data() {
    return {
      dialogVisible: false,
      page: 2,
      strategyData: [],
      activeStep: 0,
      dateRange: [new Date(2023, 0, 1), new Date(2023, 4, 16)],
      preselectedList: [],
      selectedList: [],
      chartData: {},
      initialCapital: 10000,
      algorithm: {
        id: "0",
      },
      showChart: false,
      chartInput: {
        dates: [],
        lines: [],
      },
      resultInfos: [],
      pickerOptions: {
        disabledDate(date) {
          const year = date.getFullYear();
          const month = date.getMonth() + 1;
          const day = date.getDate();
          // 设置禁用日期范围
          return (
            !(year === 2023 && month === 1) &&
            !(year === 2023 && month === 2) &&
            !(year === 2023 && month === 3) &&
            !(year === 2023 && month === 4) &&
            !(year === 2023 && month === 5 && day <= 16)
          );
        },
      },
    };
  },
  computed: {
    preselectedListButtonType() { // 预选列表按钮类型
      if (this.algorithm.id === "0") return 0; //无按钮
      const idx = this.preselectedList.findIndex(
        (v) => v.id === this.algorithm.id
      );
      if (idx === -1) return 1; // 加入预选列表按钮
      return 2; // 移出预选列表按钮
    },
  },
  methods: {
    updateSelectedList(newValue) {
      this.selectedList = newValue;
    },
    handleRun() {
      this.chartInput.lines = []; // 清空先前运行的结果
      this.resultInfos = []; // 清空先前运行的结果
      console.log("selectedList", this.selectedList)
      console.log("preselectedList", this.preselectedList)
      this.selectedList.forEach((val) => {
        // const name = this.preselectedList.find((cur) => cur.id === val.id).name;
        const name = val.name;
        console.log(name)
        executeAlgorithmContentByFileName({
          fileName: val.id,
          startdate: this.dateRange[0],
          enddate: this.dateRange[1],
          initialCash: this.initialCapital
        }).then((response) => {
          this.chartInput.dates = response.data.data.dates;
          this.chartData = response.data.data;
          this.strategyData.push({name, strategy: response.data.data.strategyProfit})
          this.chartInput.lines.push({
            name: name,
            data: response.data.data.result,
          });
          this.resultInfos.push({
            name: name,
            yieldRate: response.data.data.yieldRate.toFixed(3),
            endValue: response.data.data.endValue,
            maximum: response.data.data.maximum.toFixed(3),
          });
        });
      });
      this.showChart = true;
      console.log(this.strategyData)
    },
    handleOpenSelectBestDialog() { // 打开选择最优算法的对话框
      this.dialogVisible = true;
    },
    handleAddToPreSelected() { // 将算法加入预选列表
      const idx = this.preselectedList.findIndex(
        (v) => v.id === this.algorithm.id
      );
      // 防止重复
      if (idx === -1 && this.algorithm.id !== "0") {
        this.preselectedList.push({
          id: this.algorithm.id,
          name: this.algorithm.name,
        });
      }
    },
    handleRemoveFromPreSelected() { // 将算法从预选列表中移除
      const idx = this.preselectedList.findIndex(
        (v) => v.id === this.algorithm.id
      );
      this.preselectedList.splice(idx, 1);
    },
    handleClickItem(algorithmId) { // 点击算法列表中的算法
      getAlgorithmById({id: algorithmId}).then((response) => {
        this.algorithm = response.data.data;
      });
    },
    handleShowPreselectedList() { // 显示预选列表
      this.dialogVisible = true;
    },
    handleClose() {
      this.dialogVisible = false;
    },
    handleDelete(index, row) {
      // console.log(index, row);
      this.preselectedList.splice(index, 1);
    },
  },
};
</script>

<style lang="scss" scoped>
.el-select .el-input {
  width: 130px;
  background-color: aqua;
}

.view {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  overflow-y: auto;
  /* background-color: antiquewhite; */
}

.step-bar {
  width: 80%;
}

.page {
  display: flex;
  width: 100%;
  justify-content: flex-start;
  align-items: center;
  flex-flow: column;
  height: 100%;
  overflow-y: auto;
}

.left-area {
  /* background-color: antiquewhite; */
}

.input-with-select {
  /* background-color: aquamarine; */
  padding: 10px;
}

.right-area {
  width: 40vw;
  margin: 2rem;
}

.result-area {
  height: 30vw;

  .label {
    margin: 0;
    width: 100px;
  }
}

.pre-selected-bar {
  padding: 0;
  background-color: white;
  width: 100%;
}

.warning-card {
  margin: 10px;
  padding: 10px;
  border: #f6e1ac solid 1px;
  border-radius: 10px;
  width: 370px;
  background-color: #fcf5e6;
  @include text-sm;

  ol {
    margin-top: 4px;
  }

  .el-icon-warning-outline {
    cursor: pointer;
  }
}

.space {
  display: flex;
  width: 100%;
  justify-content: flex-start;
}
</style>
