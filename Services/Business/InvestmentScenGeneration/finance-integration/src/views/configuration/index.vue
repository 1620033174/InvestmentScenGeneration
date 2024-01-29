<template>
  <div class="view" >
    <header class="page-header" v-show="page === 1">
      <span>算法列表</span>
    </header>
    <header class="page-header" v-show="page === 2">
      <div>
<!--        <span @click="page = 1">-->
<!--          <i class="el-icon-back" />-->
<!--          <span class="text-base">返回</span>-->
<!--        </span>-->
        <el-divider direction="vertical" />
        <span>{{ algorithm.name }}</span>
      </div>
      <div class="text-base">操作指南</div>
    </header>
    <hr />
    <div class="page" v-show="page === 1">
      <div class="text-lg font-bold" style="text-align: center">
        待完成算法
      </div>
      <div style="width: 70vw; margin: auto">
        <table-list @handleEdit="handleEdit" />
      </div>
    </div>
    <div class="page2" v-show="page === 2">
      <div class="left-area">
        <div class="editor-bar">
<!--          <el-button plain @click="drawer = true">更多配置</el-button>-->
          <el-select v-model="themeValue" placeholder="" class="select">
            <el-option
              v-for="item in themes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          <!-- 为了支持保存按钮的Loading,必须额外新建一个状态monacoButtonState独立于monacoOptions.readOnly -->
          <!-- "save", "edit","loading" -->
          <el-button
            type="primary"
            plain
            @click="handleClickSaveOrEditButton"
            :loading="monacoButtonState === 'loading'"
          >
                保存算法</el-button>
        </div>
        <!-- TODO: 因子作为变量, 且加入代码提示 -->
        <!-- <div class="test-bar">{{ drawer }}</div> -->
        <monaco
          v-loading="pageLoading"
          :theme="themeValue"
          :monacoDefaultValue="monacoValue"
          :monacoOptions="monacoOptions"
          @save="handleSaveAlgorithmContent"
        />
      </div>
      <div class="right-area">
        <div style="display: flex; justify-content: space-between">
          <div>
<!--            <el-date-picker-->
<!--              v-model="dateRange"-->
<!--              type="daterange"-->
<!--              value-format="yyyy-MM-dd"-->
<!--              unlink-panels-->
<!--              align="right"-->
<!--              range-separator="至"-->
<!--              start-placeholder="开始日期"-->
<!--              end-placeholder="结束日期"-->
<!--              :picker-options="pickerOptions"-->
<!--              style="width: 250px"-->
<!--            />-->
<!--            <span class="add-on" style="padding:0px;margin:0px">￥</span>-->
<!--            <el-input-->
<!--              placeholder="请输入内容"-->
<!--              v-model="initialCash"-->

<!--              style="width: 125px;"-->
<!--              clearable>-->
<!--            </el-input>-->
            <!--<el-button @click="handleTest">测试</el-button> -->
          </div>
          <!-- TODO: 添加运行过程中按钮的Loading状态 -->
          <div style="margin-left:0px">

          </div>

          <el-button
            type="primary"
            icon="el-icon-caret-right"
            @click="handleExecuteAlgorithm"
            :loading="chooseButtonLoading"
          >
            选股</el-button
          >
        </div>
        <!-- <div>选择股票池之类的东西</div> -->
        <div v-if="initToShow === '0'" >
<!--          <el-card style="margin-top:30px;height:350px;width:660px">-->
<!--            <div id="dailybar-watermark" style="text-align:center;line-height: 100px;">-->
<!--              <span style="color: #DDDDDD;">点击”运行回测"进行回测</span>-->
<!--            </div>-->
<!--          </el-card>-->
          <el-table max-height="370" :data="tableValue" >
            <el-table-column v-for="(item, index) in tableColumns" :key="index"
                             :label="item.label" :prop="item.prop">

            </el-table-column>
          </el-table>
        </div>
        <div v-if="initToShow === '1'">
          <el-menu  class="el-menu-chart" mode="horizontal" >
          <el-menu-item index="1" @click.native="showChartA" >最大回撤分布</el-menu-item>
          <el-menu-item index="2" @click.native="showChartB">收益曲线</el-menu-item>
          <el-menu-item index="3" @click.native="showChartc">test</el-menu-item>
        </el-menu>
        </div>


        <!--A.收益曲线折线图-->
        <div class="ChartA" v-if="chartToShow === 'a'">

          <chart-a :chartData="chartAData" />
        </div>
        <div  v-if="chartToShow === 'b'">
            <div
                style="display: flex; justify-content: space-around; margin: 20px"
                class="text-sm"
              >
                <div style="color: #8ba2cc">策略收益 <br />{{ yieldRate }}%</div>
                <div style="color: #cd9597">
                  交易结束后的资金量 <br />{{ endValue }}
                </div>
            </div>
            <line-chart :chartInput="chartInput" />

        </div>
        <div v-if="chartToShow === 'c'">
           <h1 >Vu</h1>
        </div>
        <div style="margin-top:60px; width: 40%; position: absolute; right: 10%; bottom: 25px ">
          <console :consoleContent="consoleValue" />
        </div>
      </div>
      <!-- 不直接占据空间的部分,比如drawer -->
      <drawer

        :drawer.sync="drawer"
        :algorithm="algorithm"
        v-if="page === 2"
        @changeAlgorithmDescription="handleUpdateAlgorithmDescription"
      />
    </div>
  </div>
</template>

<script>
import Monaco from "./components/Monaco";
import Drawer from "./components/Drawer";
import LineChart from "@/components/Charts/LineChart";
import TableList from "./components/TableList.vue";
import Console from "./components/Console.vue";
import ChartA from "./components/ChartA";
import ChartB from "./components/ChartB";

import {
  executeAlgorithmContentByFileName, executeAlgorithmContentZongheByFileName,
  getAlgorithmById,
  getAlgorithmContentByFileName, getAlgorithmContentZongHeByFileName,
  updateAlgorithmContent, updateAlgorithmContentZongHe,
} from "@/api/algorithm";

export default {
  components: {
    Monaco,
    LineChart,
    Drawer,
    Console,
    TableList,
    ChartA,
  },
  props:{
    row: {
      type: Object,
      default: () => {},
    }
  },
  data() {
    return {
      awesome: true,
      pageLoading: false,
      chooseButtonLoading: false,
      endValue: "", // 最终的资金量
      yieldRate: "", // 收益率
      initialCash: 100000, //初始资金
      dateRange: [new Date(2023, 0, 1), new Date(2023, 4, 16)],
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
      tableValue: [],
      tableColumns: [],
      page: 2,
      drawer: false,
      themeValue: "vs-dark",
      themes: [
        { value: "vs-dark", label: "暗色主题" },
        { value: "light", label: "亮色主题" },
      ],
      algorithm: {
        id: "",
        author: "",
        createAt: "",
        name: "",
      },
      monacoValue: "", // 设置之后Monaco的值会立即变化
      savedAlgorithmContentValue: "", //保存算法时会修改此值,然后发送到flask后端更改对应文件
      consoleValue: "暂未运行",
      monacoOptions: {
        readOnly: false,
        foldingStrategy: "indentation", // 代码可分小段折叠
      },
      monacoButtonState: "edit", // "edit","loading" ||| "edit"=>"save" "save"=>"loading"=>"edit"
      chartInput: {
        dates: [],
        // dates: ["2023-01-01", new Date(2023, 1, 19)],
        // lines: [{ name: "A", data: [1, 2, 3] }],
        lines: [],
      },
      chartAData: {
        time: [

        ],
        shanghaiIndex: [

        ],
        strategyProfit: [

        ],
      },
      chartToShow : '',
      initToShow :'0',
    };
  },
  mounted() {
    this.monacoValue = '#'+this.row.logicDescription + '\n'
    this.handleEdit(0, this.row);
  },
  methods: {
    showChartA() {
        console.log("AAAAA")
        this.chartToShow = 'a';
    },
    showChartB() {
      console.log("BBBBB")
      console.log(this.endValue)
      this.chartToShow = 'b';
    },
    showChartC() {
      this.chartToShow = 'c';
    },
    showD() {
      console.log("DDD")
      this.chartToShow = 'd';
    },
    handleTest() {
      console.log(this.dateRange, "!");
    },
    handleEdit(index, row) {
      this.pageLoading = true;
      getAlgorithmById({ id: row.id }).then((response) => {
        this.algorithm = response.data.data;
        getAlgorithmContentZongHeByFileName({
          fileName: this.algorithm.algorithmFilePath,
        }).then((response) => {
          this.monacoValue = this.monacoValue + response.data.data;
          console.log("get monacoValue", this.monacoValue);
          this.pageLoading = false;
          // this.page = 2; // TODO：page该文即时响应并添加loading状态给monaco
        }).catch((error) => {
          this.$message.error("获取算法内容失败");
          this.pageLoading = false;
        });
      });

    },
    handleExecuteAlgorithm() {
      if (this.monacoButtonState !== "edit") {
        // 按钮不是edit = monaco编辑器处于编辑状态
        this.$message({
          message: "请先保存算法再运行",
          type: "warning",
        });
      } else {
        this.chooseButtonLoading = true;
        executeAlgorithmContentZongheByFileName({
          fileName: this.algorithm.algorithmFilePath
        }).then((response) => {
          if (response.data.code === 20000){
            this.$message({
              message: "运行失败",
              type: "error",
            });
            this.consoleValue = '运行失败';
            this.tableValue = [];
            this.tableColumns = [];
            this.chooseButtonLoading = false;
            return;
          }
          this.tableColumns = response.data.columns;
          this.tableValue = response.data.rows;
          this.consoleValue = '运行成功';
        });
        this.chooseButtonLoading = false;
      }
    },
    handleClickSaveOrEditButton() {
      // if (!this.monacoOptions.readOnly) {
      //   //只读为假 == 可编辑->只读 == 点击保存按钮
      //   this.monacoButtonState = "loading";
      // } else {
      //   //只读为真 == 只读->可编辑 == 点击编辑按钮
      //   this.monacoButtonState = "save";
      // }
      // this.monacoOptions.readOnly = !this.monacoOptions.readOnly;
      this.monacoOptions.readOnly = true;
    },
    handleSaveAlgorithmContent(value) {
      this.savedAlgorithmContentValue = value;
      this.monacoButtonState = "loading";
      updateAlgorithmContentZongHe({
        content: this.savedAlgorithmContentValue,
        fileName: this.algorithm.algorithmFilePath,
      }).then((response) => {
        // 移除loading状态
        this.monacoButtonState = "edit";
        this.$message({
          message: "保存成功",
          type: "success",
        });
        console.log(response.data);
      });
    },
    handleUpdateAlgorithmDescription(value) {
      this.algorithm.algorithmDescription = value;
    },
  },
};
</script>

<style lang="scss" scoped>
.page-header {
  @include text-lg;
  font-weight: bold;
  margin: 10px;
  display: flex;
  justify-content: space-between;
}
.view {
}
.type-area {
  width: 110px;
}

.page2 {
  display: flex;
}
.search-bar {
  width: 300px;
}
.left-area {
  width: 45%;
  margin-right: 5%;
}
.right-area {
  width: 45%;
  margin-right: 5%;
}
.select {
  width: 110px;
}
.editor-bar {
  margin-bottom: 10px;
  margin-left: 15px;
  display: flex;
  gap: 10px;
  /* justify-content: space-between; */
}
</style>
