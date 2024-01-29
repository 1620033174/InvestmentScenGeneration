<template>
  <div>
    <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
      <el-tab-pane label="选股算法选择" name="first">
        <choose-algorithm/>
<!--        <template>-->
<!--          <div class="app" style="margin-left: 30px;">-->

<!--            <div class="container1">-->
<!--              <div class="left">-->

<!--                <div class="search">-->
<!--                  <el-input-->
<!--                    placeholder="请输入选股算法名或id"-->
<!--                    v-model="keyword"-->
<!--                    @clear="reset"-->
<!--                    @change="checkReset"-->
<!--                    style="display: inline-block; width: 80%"-->
<!--                  ></el-input>-->
<!--                  <el-button-->
<!--                    type="primary"-->
<!--                    @click="search"-->
<!--                    style="display: inline-block"-->
<!--                  >-->
<!--                    搜索-->
<!--                  </el-button>-->
<!--                </div>-->


<!--                <div class="list" style="text-align: center;">-->
<!--                  <el-table :data="algorithms" @row-click="selectAlgorithm" highlight-current-row>-->
<!--                    <el-table-column prop="id" label="算法id"></el-table-column>-->
<!--                    <el-table-column prop="name" label="算法名"></el-table-column>-->
<!--                  </el-table>-->
<!--                </div>-->
<!--              </div>-->
<!--              <div class="right">-->
<!--                <el-card v-if="selectedAlgorithm" :header="selectedAlgorithm.name"-->
<!--                         style="margin-left: 10%; width: 50%;">-->
<!--                  <p style="font-weight: 100;">算法id: {{ selectedAlgorithm.id }}</p>-->
<!--                  <p style="font-weight: 100;">选股逻辑: {{ selectedAlgorithm.logic }}</p>-->
<!--                  <p style="font-weight: 100;">算法详细信息: {{ selectedAlgorithm.info }}</p>-->
<!--                </el-card>-->
<!--              </div>-->
<!--            </div>-->


<!--            <div class="table">-->
<!--              <el-table :data="stocks" border style="width: 100%">-->
<!--                <el-table-column fixed prop="name" label="股票名称"></el-table-column>-->
<!--                <el-table-column fixed prop="code" label="股票代码"></el-table-column>-->
<!--                <el-table-column v-for="factor in factors" :key="factor.id" :prop="factor.id" :label="factor.name"-->
<!--                                 sortable></el-table-column>-->
<!--              </el-table>-->
<!--            </div>-->
<!--          </div>-->
<!--        </template>-->
      </el-tab-pane>
      <!--静态描述填写-->
      <el-tab-pane label="静态信息配置" name="second">
<!--        <div>-->
<!--          <el-form :model="form" label-width="120px">-->
<!--            <el-form-item label="投资场景名" >-->
<!--              <el-input v-model="form.name" placeholder="请输入投资场景"></el-input>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="投资场景主题">-->
<!--              <el-select v-model="form.theme" placeholder="请选择" filterable="true" allow-create="true">-->
<!--                <el-option-->
<!--                  v-for="item in themes"-->
<!--                  :key="item.value"-->
<!--                  :label="item.label"-->
<!--                  :value="item.value"-->
<!--                >-->
<!--                </el-option>-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="投资场景描述">-->
<!--              <vue-editor v-model="form.description"></vue-editor>-->
<!--            </el-form-item>-->
<!--            <el-form-item label="投资场景标签">-->
<!--              <el-transfer-->
<!--                v-model="tagsValue"-->
<!--                :data="tags"-->
<!--                :props="{ key: 'id', label: 'name' }"-->
<!--                :titles="['标签列表', '已选标签']"-->
<!--                @change="handleChange"-->
<!--                filterable-->
<!--                :render-content="renderContent"-->
<!--              >-->
<!--                &lt;!&ndash; 左侧列表插槽 &ndash;&gt;-->
<!--                <template #left-footer>-->
<!--                  <el-button-->
<!--                    type="primary"-->
<!--                    size="mini"-->
<!--                    @click="handleAddTag"-->
<!--                    style="margin-left: 29%;"-->
<!--                  >-->
<!--                    新增标签-->
<!--                  </el-button>-->
<!--                </template>-->
<!--                &lt;!&ndash; 左侧列表每一项插槽 &ndash;&gt;-->
<!--                <template #left="{ option }">-->
<!--                  <span>{{ option.name }}</span>-->
<!--                  <el-button-->
<!--                    type="text"-->
<!--                    icon="el-icon-edit"-->
<!--                    @click.stop="handleEditTag(option)"-->
<!--                  ></el-button>-->
<!--                </template>-->
<!--              </el-transfer>-->

<!--              &lt;!&ndash; 新增标签对话框 &ndash;&gt;-->
<!--              <el-dialog-->
<!--                title="新增标签"-->
<!--                append-to-body-->
<!--                :visible.sync="addTagDialogVisible"-->
<!--                width="30%"-->
<!--                :before-close="handleAddTagDialogClose"-->
<!--              >-->
<!--                <el-form :model="addTagForm" ref="addTagForm" label-width="80px">-->
<!--                  <el-form-item label="标签名称" prop="name">-->
<!--                    <el-input v-model="addTagForm.name"></el-input>-->
<!--                  </el-form-item>-->
<!--                </el-form>-->
<!--                <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="addTagDialogVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="handleAddTagConfirm">确 定</el-button>-->
<!--      </span>-->
<!--              </el-dialog>-->

<!--              &lt;!&ndash; 编辑标签对话框 &ndash;&gt;-->
<!--              <el-dialog-->
<!--                title="编辑标签"-->
<!--                :visible.sync="editTagDialogVisible"-->
<!--                width="30%"-->
<!--                :before-close="handleEditTagDialogClose"-->
<!--              >-->
<!--                <el-form :model="editTagForm" ref="editTagForm" label-width="80px">-->
<!--                  <el-form-item label="标签名称" prop="name">-->
<!--                    <el-input v-model="editTagForm.name"></el-input>-->
<!--                  </el-form-item>-->
<!--                </el-form>-->
<!--                <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="editTagDialogVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="handleEditTagConfirm">确 定</el-button>-->
<!--      </span>-->
<!--              </el-dialog>-->
<!--            </el-form-item>-->
<!--            <el-form-item>-->
<!--              <el-button type="primary" @click="saveForm">保存</el-button>-->
<!--              <el-button type="primary" @click="preview">预览</el-button>-->
<!--              <el-dialog-->
<!--                title="静态信息预览"-->
<!--                :visible.sync="staticDialogVisible"-->
<!--                width="50%">-->
<!--                <div class="preview" style="margin-left: 120px;">-->
<!--                  <h3 style="align-self: center;">场景名：{{ form.name }}</h3>-->
<!--                  <p>场景主题：{{ form.theme }}</p>-->
<!--                  <p>场景描述：-->
<!--                    <div v-html=" form.description "></div>-->
<!--                  </p>-->
<!--                  <p>场景标签：-->
<!--                    <el-tag v-for="(tag) in selectedTags" :key="tag.id">{{ tag.name }}</el-tag>-->
<!--                  </p>-->
<!--                </div>-->
<!--                <span slot="footer" class="dialog-footer">-->
<!--        <el-button @click="staticDialogVisible = false">关闭</el-button>-->

<!--      </span>-->
<!--              </el-dialog>-->
<!--            </el-form-item>-->
<!--          </el-form>-->
<!--        </div>-->
        <info-config/>
      </el-tab-pane>

<!--      <el-tab-pane label="个股画像配置" name="third">-->
<!--        <portrait-config/>-->


<!--      </el-tab-pane>-->
      <el-tab-pane label="投资场景预览与生成" name="fifth">
        <scene-generation/>
<!--        <div>-->
<!--          <div class="preview" style="margin-left: 30px;">-->

<!--            <h3 style="align-self: center;">场景名：{{ form.name }}</h3>-->
<!--            <p>场景主题：{{ form.theme }}</p>-->
<!--            <p>场景描述：-->
<!--              <div v-html=" form.description "></div>-->
<!--            </p>-->

<!--            <p>场景标签：-->
<!--              <el-tag v-for="(tag) in selectedTags" :key="tag.id">{{ tag.name }}</el-tag>-->
<!--            </p>-->

<!--          </div>-->
<!--          <div style="margin-left: 30px;">-->
<!--            <p>入选股票池：</p>-->
<!--            <div class="table">-->
<!--              <el-table :data="stocks" border style="width: 100%">-->
<!--                <el-table-column fixed prop="name" label="股票名称"></el-table-column>-->
<!--                <el-table-column fixed prop="code" label="股票代码"></el-table-column>-->
<!--                <el-table-column v-for="factor in factors" :key="factor.id" :prop="factor.id"-->
<!--                                 :label="factor.name"></el-table-column>-->
<!--              </el-table>-->
<!--            </div>-->
<!--          </div>-->
<!--          <div style="display: flex; flex-wrap: wrap;margin-left: 30px">-->
<!--            <p><strong>选择的增值服务：</strong></p>-->
<!--            <div-->
<!--              v-for="(item, index) in checkedRowsService"-->
<!--              :key="index"-->
<!--              style="margin: 10px"-->
<!--            >-->
<!--              <el-button>{{ item.name }}</el-button>-->
<!--            </div>-->
<!--          </div>-->

<!--          <div style="text-align: center; margin-top: 30px;">-->
<!--            <el-button @click="publish" type="primary" plain>生成场景并发布</el-button>-->
<!--          </div>-->
<!--        </div>-->
      </el-tab-pane>

    </el-tabs>
  </div>
</template>
<script>
import {VueEditor} from "vue2-editor";
import infoConfig from "./infoConfig/index.vue"
import sceneGeneration from "./sceneGeneration/index.vue";
import chooseAlgorithm from "@/views/scenecreate/chooseAlgorithm/index.vue";
import * as echarts from "echarts";
import portraitConfig from "@/views/scenecreate/portraitConfig/index.vue";

export default {
  components: {
    VueEditor,
    infoConfig,
    sceneGeneration,
    chooseAlgorithm,
    portraitConfig
  },
  props: {
    selectScene: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      tabTitle: ['综合评价与区间预测', '场景相关基础信息', '股票自身历史经验', '其他个股相似经验'],
      cur: 0, //默认选中第一个tab

      tabTitle0: ['选股算法选择', '静态信息配置', '个股画像配置', '增值服务配置', '投资场景预览与生成'],
      cur0: 0, //默认选中第一个tab
      xData: ["最低目标价", "最高目标价", "目标均价", "当前价",], //横坐标
      yData: [23, 24, 18, 22], //数据
      sharePriceChartStyle: {float: "left", width: "600px", height: "400px"}, //图表样式
      chart1: null, // echarts实例
      dialogVisible: false,
      selectedAlgorithm: {},
      selectedAlgorithm1: "1", // 当前选中的选股算法
      factors: [], // 当前选股算法的选股因子列表
      stocks: [], // 当前选股算法对应的股票列表
      activeName: "first",
      activeName1: "third-1",
      activeName2: "third-4-1",
      radioValue: "综合评价与区间预测",
      percentage: 97.5,
      colors: [
        {color: '#EE0000', percentage: 100}
      ],
      form: {
        name: "",
        theme: "",
        description: "",
        tags: [],
      },
      factorData: [
        {
          price: "6.901",
          name: "每股收益(元)",
          grade: "1|10",
          checked: false,
        },
        {
          price: "31.325",
          name: "每股未分配利润(元)",
          grade: "1|10",
          checked: false,
        },
        {
          price: "5.374",
          name: "每股现金流(元)",
          grade: "3|10",
          checked: false,
        },
        {
          price: "1.02",
          name: "流通股本(亿元)",
          grade: "1|10",
          checked: false,
        },
        {
          price: "5.610",
          name: "每股公积金(元)",
          grade: "6|10",
          checked: false,
        },
        {
          price: "57.00",
          name: "流通市值(亿元)",
          grade: "2|10",
          checked: false,
        },
      ],

      intraTableData: [
        {
          id: "1",
          name: "东方财富",
          grade: "95",
          change: "+0.95%",
        },
        {
          id: "2",
          name: "中科创达",
          grade: "92",
          change: "+0.82%",
        },
        {
          id: "3",
          name: "宝信软件",
          grade: "87",
          change: "+0.65%",
        },
      ],
      themes: [
        {value: "科技", label: "科技"},
        {value: "金融", label: "金融"},
        {value: "教育", label: "教育"},
        {value: "医疗", label: "医疗"},
      ],
      tags: [
        {id: 1, name: "创新"},
        {id: 2, name: "高效"},
        {id: 3, name: "可靠"},
        {id: 4, name: "安全"},
      ],
      otherData: [
        {
          name: "平安银行",
          code: "000001",
          similarity: 0.95,
          eps: 1.23,
          cfps: 1.56,
          capf: 0.78,
          undp: 0.45,
          capi: 174.32,
          cmv: 285.67
        },
        {
          name: "万科A",
          code: "000002",
          similarity: 0.92,
          eps: 2.34,
          cfps: 2.89,
          capf: 1.23,
          undp: 0.67,
          capi: 113.45,
          cmv: 198.76
        },
        {
          name: "国农科技",
          code: "000004",
          similarity: 0.88,
          eps: -0.12,
          cfps: -0.34,
          capf: -0.56,
          undp: -0.89,
          capi: 12.34,
          cmv: 9.87
        }
      ],
      otherData0: [
        {
          name: "退市中新",
          code: "603996",
          similarity: 0.40,
          eps: -0.42,
          cfps: -0.03,
          capf: 1.86,
          undp: -10.69,
          capi: 3.00,
          cmv: 9.87
        },
        {
          name: "赫美集团",
          code: "002356",
          similarity: 0.39,
          eps: -0.12,
          cfps: 0.02,
          capf: 1.50,
          undp: -6.17,
          capi: 5.27,
          cmv: 6.21
        },
        {
          name: "退市拉夏",
          code: "603157",
          similarity: 0.35,
          eps: -0.14,
          cfps: -0.19,
          capf: 3.49,
          undp: -4.01,
          capi: 1.46,
          cmv: 3.02
        }
      ],
      tagsValue: [],
      // 新增标签对话框是否可见
      addTagDialogVisible: false,
      // 新增标签表单数据
      addTagForm: {
        name: "",
      },
      // 编辑标签对话框是否可见
      editTagDialogVisible: false,
      // 编辑标签表单数据
      editTagForm: {
        id: "",
        name: "",
      },
      staticDialogVisible: false,
      dialogVisible1: false,
      options: [
        {
          value: "选项1",
          label: "高送转",
        },
        {
          value: "选项2",
          label: "送转高",
        },
        {
          value: "选项3",
          label: "送高转",
        },
        {
          value: "选项4",
          label: "转送高",
        },
        {
          value: "选项5",
          label: "高转送",
        },
      ],
      static: {
        sceneName: "",
        sceneTheme: "",
        sceneDesc: "",
        sceneTable: "",
      },

      data: [
        {
          key: 1,
          label: "备选项1",
        },
        {
          key: 2,
          label: "备选项2",
        },
        {
          key: 3,
          label: "备选项3",
        },
        {
          key: 4,
          label: "备选项4",
        },
        {
          key: 5,
          label: "备选项5",
        },
      ],
      newsData: [{
        time: '03-04',
        desc: "本公司研发自主独立技术",
      },
        {
          time: '03-04',
          desc: "本公司研发自主独立技术",
        },
        {
          time: '03-04',
          desc: "本公司研发自主独立技术",
        },

      ],
      selectedRows: [],
      algorithms: [
        // 模拟的选股算法列表
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
      ],
      checkList: ['复选框 A'],
      info: "",
      factor: "",
      logic: "",
      id: "",
      label: "",
      selectedAlgorithm2: null,
      shareData: [],
      sortFactor: null,
      sortOrder: null,
      value: "",
      // serviceData: [
      //   {name: "在线决策", function: " 专业业务人员在线为您提供决策建议 ", provider: "零羽", checked: false},
      //   {name: "组合推荐", function: " 投资大师为您提供股票组合建议 ", provider: "阿瑾", checked: false},
      //   {name: "风险控制", function: "  专业业务人员最大程度为您削减风险损失  ", provider: "甘泉", checked: false},
      //   {name: "人工投顾", function: " 专业投资顾问为您提供投资建议 ", provider: "信手拈云", checked: false},
      // ],
      checkedRowsService: [],
      checkedRowsShare: [],
      checkedRowsFactor: [],
      rowData: [],
      keyword: "",

      checkedValues: ["行业均值", "行业中值"], // 默认选中的复选框
      chart: null, // echarts实例
      chartOptions: {
        // echarts配置项
        title: {
          text: "柱形图示例",
        },
        tooltip: {},
        legend: {
          data: [],
        },
        xAxis: {
          data: ["每股收益", "每股公积金", "每股未分配利润"], // 模拟的x轴数据
        },
        yAxis: {},
        series: [],
      },
      chartData: {
        // 模拟的图表数据
        行业均值: [5, 20, 36],
        行业中值: [6, 15, 25],
        行业四分位: [4, 18, 32],
        行业排名: [1, 3, 2,],
      },

    };
  },
  mounted() {
    this.initEcharts();
    this.selectedAlgorithm = this.algorithm[0];

    this.initChart(); // 初始化图表
    this.updateChart(); // 更新图表

  },
  computed: {
    selectedTags() {
      return this.tags.filter((tag) => this.tagsValue.includes(tag.id));
    },
  },
  watch: {
    value(val) {
      let option = this.algorithm.find((item) => item.value === val);
      if (option) {
        this.info = option.info;
        this.id = option.id;
        this.label = option.label;

        this.logic = option.logic;
      } else {
        this.info = "";
        this.id = "";
        this.label = "";

        this.logic = "";
      }
    },
  },

  methods: {
    initChart() {
      // 初始化图表

      this.chart = echarts.init(this.$refs.echart);
      this.chart.setOption(this.chartOptions);
    },
    updateChart() {
      // 更新图表
      let series = []; // 存储图表系列数据
      let legend = []; // 存储图表图例数据
      for (let value of this.checkedValues) {
        // 遍历选中的复选框
        legend.push(value); // 将复选框的值作为图例数据
        series.push({
          // 将对应的数据作为系列数据
          name: value,
          type: "bar",
          data: this.chartData[value],
        });
      }
      this.chartOptions.legend.data = legend; // 更新图表图例数据
      this.chartOptions.series = series; // 更新图表系列数据
      this.chart.setOption(this.chartOptions, true); // 设置图表配置项，并覆盖之前的配置项
    },
    initEcharts() {
      // 基本柱状图
      const option1 = {
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [
          {
            type: "bar", //形状为柱状图
            data: this.yData
          }
        ]
      };
      const sharePriceChart = echarts.init(document.getElementById("sharePriceChart"));
      sharePriceChart.setOption(option1);


    },
    handleClickShare(row) {
      this.dialogVisible = true;
      // 获取并展示个股画像数据
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then((_) => {
          done();
        })
        .catch((_) => {
        });
    },
    handleSelectionChange(selection, row) {
      this.listSelect = selection;
      this.listSelectId = selection.map((item) => item.code);
    },
    //选中行的数据
    handleCheckService(row) {
      //选中时触发该方法，更新数组checkedRowsService
      if (row.checked) {
        this.checkedRowsService.push(row);
      } else {
        this.checkedRowsService = this.checkedRowsService.filter(
          (item) => item !== row
        );
      }
    },
    preview() {
      this.staticDialogVisible = true;
    },
    handleCheckShare(row) {
      //选中时触发该方法，更新数组checkedRowsShare
      if (row.checked) {
        this.checkedRowsShare.push(row);
      } else {
        this.checkedRowsShare = this.checkedRowsShare.filter(
          (item) => item !== row
        );
      }
    },
    handleCheckFactor(row) {
      //checkedRowsFactor
      if (row.checked) {
        this.checkedRowsFactor.push(row);
      } else {
        this.checkedRowsFactor = this.checkedRowsFactor.filter(
          (item) => item !== row
        );
      }
    },
    handleChange(newVal) {
      console.log("穿梭框变化后的值：", newVal);
    },
    // 处理新增标签按钮点击事件
    handleAddTag() {
      this.addTagDialogVisible = true;
    },
    // 处理新增标签对话框关闭事件
    handleAddTagDialogClose() {
      this.$refs.addTagForm.resetFields();
    },
    // 处理新增标签确认事件
    handleAddTagConfirm() {
      this.$refs.addTagForm.validate((valid) => {
        if (valid) {
          // 模拟生成一个新的id，实际应用中应该由后台返回
          const newId = Math.max(...this.tags.map((item) => item.id)) + 1;
          // 将新的标签添加到左侧列表中
          this.tags.push({
            id: newId,
            name: this.addTagForm.name,
          });
          // 关闭对话框并重置表单
          this.addTagDialogVisible = false;
          this.$refs.addTagForm.resetFields();
          this.$message.success("新增标签成功");
        } else {
          return false;
        }
      });
    },
    // 处理编辑标签按钮点击事件
    handleEditTag(option) {
      this.editTagDialogVisible = true;
      // 将当前选中的标签数据赋值给编辑表单
      this.editTagForm.id = option.id;
      this.editTagForm.name = option.name;
    },
    // 处理编辑标签对话框关闭事件
    handleEditTagDialogClose() {
      this.$refs.editTagForm.resetFields();
    },
    // 处理编辑标签确认事件
    handleEditTagConfirm() {
      this.$refs.editTagForm.validate((valid) => {
        if (valid) {
          // 找到要编辑的标签在左侧列表中的索引
          const index = this.tags.findIndex(
            (item) => item.id === this.editTagForm.id
          );
          if (index !== -1) {
            // 更新左侧列表中的数据
            this.tags[index].name = this.editTagForm.name;
            // 关闭对话框并重置表单
            this.editTagDialogVisible = false;
            this.$refs.editTagForm.resetFields();
            this.$message.success("编辑标签成功");
          } else {
            this.$message.error("找不到要编辑的标签");
          }
        } else {
          return false;
        }
      });
    },

    renderContent(h, option) {
      return (
        <span>
          {option.name}
          <el-button
            type="text"
            icon="el-icon-edit"
            onClick={(e) => {
              e.stopPropagation();
              this.handleEditTag(option);
            }}
          ></el-button>
        </span>
      );
    },
    save() {
      // 保存当前选股算法
      this.$confirm("是否应用当前选股算法?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$message({
            type: "success",
            message: "保存成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    saveForm() {
      //保存当前表单数据
      this.$confirm("提交当前表单?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$message({
            type: "success",
            message: "保存成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
    fetchData() {
      // 根据当前选中的选股算法生成模拟数据
      switch (this.selectedAlgorithm1) {
        case "1":
          this.factors = [
            // 模拟的选股因子列表
            {id: "factor1", name: "每股收益（元）"},
            {id: "factor2", name: "每股公积金（元）"},
            {id: "factor3", name: "每股未分配利润（元）"}
          ];
          this.stocks = [
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
          ];
          this.rowData = [
            "每股收益（元）", "每股公积金（元）", "每股未分配利润（元）"
          ]
          break;
        case "2":
          this.factors = [
            // 模拟的选股因子列表
            {id: "factor4", name: "当前股价（元）"},
            {id: "factor5", name: "流通股本（亿元）"}
          ];
          this.stocks = [
            // 模拟的股票列表
            {
              name: "股票D",
              code: "000004",
              factor4: 0.6,
              factor5: 0.8
            },
            {
              name: "股票E",
              code: "000005",
              factor4: 0.7,
              factor5: 0.7
            },
            {
              name: "股票F",
              code: "000006",
              factor4: 0.8,
              factor5: 0.6
            }
          ];
          this.rowData = [
            "当前股价（元）", "流通股本（亿元）"
          ]

          break;
        case "3":
          this.factors = [
            // 模拟的选股因子列表
            {id: "factor6", name: "流通市值（亿元）"}
          ];
          this.stocks = [
            // 模拟的股票列表
            {
              name: "云内动力",
              code: "000903.XSHE",
              factor6: 127.1167
            },
            {
              name: "星网宇达",
              code: "002829.XSHE",
              factor6: 63.8491
            },
            {
              name: "青岛中程",
              code: "300208.XSHE",
              factor6: 100.1299
            }
          ];
          this.rowData = [
            "市值（亿元）"
          ]
          break;
      }
    },
    search() {
      // 假设这里是调用后端接口来搜索选股算法
      this.algorithms = this.algorithms.filter(
        (item) =>
          item.name.includes(this.keyword) || item.id.includes(this.keyword)
      );
    },
    selectAlgorithm(row) {
      this.selectedAlgorithm = row;
      this.selectedAlgorithm1 = row.id
      this.fetchData()
    },
    reset() {
      // 假设这里是从后端重新获取选股算法列表
      this.algorithms = [
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
      ];
    },
    checkReset() {
      if (this.keyword === "") {
        this.reset();
      }

    },

    sortTable(factor) {
      // 这里可以根据选股因子对表格数据进行排序
      // 这里只是简单地实现了一个升序或降序的切换
      if (this.sortFactor === factor) {
        this.sortOrder = this.sortOrder === "asc" ? "desc" : "asc";
      } else {
        this.sortFactor = factor;
        this.sortOrder = "asc";
      }
      this.shareData.sort((a, b) => {
        if (this.sortOrder === "asc") {
          return a[factor] - b[factor];
        } else {
          return b[factor] - a[factor];
        }
      });
    },
    handleClick(tab) { // 添加一个tab点击事件
      if (tab.name === "third-3") { // 如果点击的是第二个tab
        this.$nextTick(() => { // 等待dom更新后
          this.chart.resize(); // 调用echarts的resize方法
        });
      }
    },

    publish() {
      this.$confirm("是否发布?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        center: true,
      })
        .then(() => {
          this.$message({
            type: "success",
            message: "发布成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
  },
};
</script>
<style scoped>

.container2 {
  width: 800px;
  margin: 0 auto;
}

.change-column.cell {
  color: red;
}

.card-checkbox {
  float: right;
}

th,
td {
  border: 1px black;
  padding: 10px;
}

.container1 {
  display: flex;
  height: 100%;
}

.left {
  width: 40%;
  overflow-y: auto;
}

.right {
  width: 60%;
}

.checkbox-group {
  margin-top: 20px;
}

ul1 {
  display: flex;
}

li1 {
  border: 1px solid #ccc;
  margin: 10px;
  width: 200px;
  font-size: 20px;
  text-align: center;
  line-height: 40px;
}
</style>
